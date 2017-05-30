/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameobjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Array;
import gold.daniel.main.GameEngine;

/**
 *`
 * @author wrksttnpc
 */
public class World extends Entity
{
    /*THESE ARE PARALLEL STRUCTURES*/
    TiledMap map;
    Tile[][] tiles;
    /*******************************/
    
    
    TiledMapRenderer tmr;
    
    Array<Entity> entities;
    Array<Entity> toAddToScene;
    Array<Entity> toRemoveFromScene;
    
    Player player;
    
    GameEngine engine;
    

    
    boolean updating = false;
    
    
    /**
     * 
     * @param map
     * @param tmr
     * @param engine
     * @param s
     * @param sh 
     */
    public World(TiledMap map, OrthogonalTiledMapRenderer tmr, GameEngine engine, SpriteBatch s, ShapeRenderer sh)
    {
        super(s, sh);
        this.map = map;
        this.engine = engine;
        this.tmr = tmr;

        x = 0;
        y = 0;
        width = Tile.SIZE * map.getProperties().get("width", Integer.class);
        height = Tile.SIZE * map.getProperties().get("height", Integer.class);
        
        entities = new Array<Entity>();
        toAddToScene = new Array<Entity>();
        toRemoveFromScene = new Array<Entity>();
        
        createTiles();
        spawnEntities();
    }

    /**
     * creates tiles from tiledmap passed through constructor
     * this exists so the constructor doesn't seem too long
     * 
     * probably should use the TiledMap properties, but I like rolling
     * my own structure for tiles for simplicity
     */
    private void createTiles()
    {
        tiles = new Tile[map.getProperties().get("width", Integer.class)][map.getProperties().get("height", Integer.class)];
        TiledMapTileLayer layer = (TiledMapTileLayer)map.getLayers().get("collision");
        for (int i = 0; i < tiles.length; i++)
        {
            for (int j = 0; j < tiles[i].length; j++)
            {
                int tileX = (int)(i * layer.getTileWidth());
                int tileY = (int)(j * layer.getTileHeight());
                
                boolean isSolid = false;
                if(layer.getCell(i, j) != null)
                {
                    isSolid = layer.getCell(i, j).getTile().getProperties().get("isSolid") != null;
                }
                tiles[i][j] = new Tile(s, sh, tileX, tileY, isSolid);
                addEntity(tiles[i][j]);
            }
        }
    }
    
    /**
     * Takes the entity markers from the TMX file and creates them.
     */
    private void spawnEntities()
    {
        TiledMapTileLayer layer = (TiledMapTileLayer)map.getLayers().get("spawn");
        for (int i = 0; i < tiles.length; i++)
        {
            for (int j = 0; j < tiles[i].length; j++)
            {
                Cell cell = layer.getCell(i, j);
                if(cell != null)
                {
                    Entity temp = null;
                    
                    /*
                     * There might be a more simple way to write this block?
                     * as we compare to null every time we cannot use a switch
                     * and every entity may have a different constructor
                     * 
                     *
                     * I believe we would need to organize the data
                     * diffrently in the TMX file, and use values instead of 
                     * null checks. This would mean adding every property to 
                     * every tile however, and that would be a pain
                     */
                    if(cell.getTile().getProperties().get("player") != null)
                    {
                        temp = new Player(i * Tile.SIZE, j * Tile.SIZE, 
                                s, sh, engine.getNextController());
                        
                        this.player = (Player)temp;
                    }
                    else if(cell.getTile().getProperties().get("robot") != null)
                    {
                        temp = new Robot(i * Tile.SIZE, j * Tile.SIZE, s, sh);
                    }
                    else if(cell.getTile().getProperties().get("tank") != null)
                    {
                        temp = new Tank(i * Tile.SIZE, j * Tile.SIZE, s, sh);
                        
                    }
                    addEntity(temp);
                }
            }
        }
    }
    
    /**
     * retrieves the player object.
     * we need it pretty often so this reference is really useful.
     * @return 
     */
    public Player getPlayer()
    {
        return player;
    }
    
    /**
     * 
     * @param obj to add
     */
    public void addEntity(Entity obj)
    {
        if(obj == null) return;
        if(updating)toAddToScene.add(obj);
        else entities.add(obj);
    }
    
    /**
     * 
     * @param obj to remove
     */
    public void removeEntity(Entity obj)
    {
        if(obj == null) return;
        if(updating) toRemoveFromScene.add(obj);
        else entities.removeValue(obj, true);
    }
    
    /**
     * update the world plz.
     * 
     * updating entities, adding, and removing done in here
     * 
     * this is turning into a clusterfuck of a method
     * 
     * @param deltaTime 
     */
    public void update(float deltaTime)
    {
        updating = true;
        
        Array<Robot> robots = new Array<Robot>();
        Array<Bullet> bullets = new Array<Bullet>();
        Array<Tank> tanks = new Array<Tank>();
        
        //updates all currently active entities
        for(Entity entity : entities)
        {
            entity.update(this, deltaTime);
            //prepare to remove from scene
            if(!entity.isAlive)
            {
                toRemoveFromScene.add(entity);
            }
            //adds entities to their respective lists
            //used for iterating for entity interactions
            else
            {
                if(entity instanceof Robot)
                {
                    robots.add((Robot)entity);
                }
                else if(entity instanceof Bullet)
                {
                    bullets.add((Bullet)entity);
                }
                else if(entity instanceof Tank)
                {
                    tanks.add((Tank)entity);
                }
            }
        }
        
        //handle bullet mechanics when colliding with enemies
        for(Bullet bullet : bullets)
        {
            //iterate over robots to check bullet collision
            for(Robot robot : robots)
            {
                if(bullet.isColliding(robot))
                {
                    bullet.isAlive = false;
                    robot.damage(bullet.getDamage());
                    robot.spawnParticles(this, bullet.x, bullet.y, bullet.angle);
                    //these are for the freeze effect and camera shake
                    //maybe move these into their respective entites later?
                    //as we have to write this here for every interaction
                    if(robot.isAlive())
                    {
                        engine.sleep(3);
                        engine.shake(3);
                    }
                    else
                    {
                        engine.sleep(7);
                        engine.shake(7);
                    }
                }
            }
            //iterates over tanks to check bullet collision
            for(Tank tank : tanks)
            {
                if(bullet.isColliding(tank))
                {
                    bullet.isAlive = false;
                    tank.damage(bullet.getDamage());
                    tank.spawnParticles(this, bullet.x, bullet.y, bullet.angle);
                    //these are for the freeze effect and camera shake
                    //maybe move these into their respective entites later?
                    //as we have to write this here for every interaction
                    //in this loop, looks ugly
                    if(tank.isAlive())
                    {
                        engine.sleep(3);
                        engine.shake(3);
                    }
                    else
                    {
                        engine.sleep(10);
                        engine.shake(15);
                    }

                }
            }
        }
        //robot player colision
        for(Robot robot : robots)
        {
            if(player.isColliding(robot))
            {
                player.damage(1);
                player.handleMoveCollisionResponse(robot);
            }
        }
        //tank player collision
        for(Tank tank : tanks)
        {
            if(tank.isColliding(player))
            {
                player.handleMoveCollisionResponse(tank);
            }
        }
        
        updating = false;
        
        
        //adds entities flagged to be added to scene
        if(toAddToScene.size > 0)
        {
            entities.addAll(toAddToScene);
            toAddToScene.clear();
        }
        
        //removes entities taht are flagged to be removed from scene
        entities.removeAll(toRemoveFromScene, true);
        toRemoveFromScene.clear();
    }

    /**
     * render calls
     */
    @Override
    public void draw()
    {
        tmr.render();
        Player temp = null;
        s.begin();
        for(Entity entity : entities)
        {
            if(entity instanceof Player)
            {
                //this is okay because there should only ever be 1 player instance
                temp = (Player)entity;
                player = temp;
            }
            else
            {
                entity.draw();
            }
        }
        s.end();
        //draw this entity over top everything else
        if(temp != null) temp.draw();
        
    }

    @Override
    public void dispose()
    {
        
    }
    
    /**
     * get neighboring tiles of the centre of the gameobject.
     * calls getCollisionTiles(x,y) 
     * 
     * @param obj
     * @return 
     */
    public Array<Tile> getCollisionTiles(Entity obj)
    {
        return getCollisionTiles((int)(obj.x + obj.width / 2), (int)(obj.y + obj.height / 2));
    }
    
    /**
     * Pass X & Y world coordinates.
     * get the neighboring tiles of the point(X,Y)
     * 
     * used to get surrounding tiles for collision detection
     * 
     * 
     * @param x 
     * @param y
     * @return 
     */
    public Array<Tile> getCollisionTiles(int x, int y)
    {
        Array<Tile> result = new Array<Tile>(4);
        
        int tileX = x / Tile.SIZE;
        int tileY = y / Tile.SIZE;
        
        if(tileX >= 0 && tileX < tiles.length - 1 &&
           tileY >= 0 && tileY < tiles[0].length - 1 && 
           tiles[tileX][tileY].isSolid)
        {
            result.add(tiles[tileX][tileY]);
        }
        
        if(tileX - 1 >= 0 &&
           tiles[tileX - 1][tileY].isSolid)
        {
            result.add(tiles[tileX - 1][tileY]);
        }
        
        if(tileX + 1 < tiles.length - 1 && tileX + 1 >= 0 && 
           tiles[tileX + 1][tileY].isSolid)
        {
            result.add(tiles[tileX + 1][tileY]);
        }
        
        if(tileY - 1 >= 0 &&
           tiles[tileX][tileY - 1].isSolid)
        {
            result.add(tiles[tileX][tileY - 1]);
        }
        
        if(tileY + 1 < tiles[0].length - 1 && tileY + 1 >= 0 &&
           tiles[tileX][tileY + 1].isSolid)
        {
            result.add(tiles[tileX][tileY + 1]);
        }

        
        return result;
    }

    /**
     * Gets all entities of type ?.
     * 
     * useful for checking against types. - No shit.
     *
     * 
     * @param type 
     * @return 
     */
    Array<?> getEntityType(Class<?> type)
    {
        Array<Entity> result = new Array<Entity>();
        
        
        for(int i = 0; i < entities.size; i++)
        {
            Entity obj = entities.get(i);
            if(obj.getClass() == type)
            {
                result.add(obj);
            }
        }
        return result;
    }
}
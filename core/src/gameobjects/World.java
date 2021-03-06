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
        
        tiles = engine.createTiles(map, this);
        spawnEntities();
    }
  
    /**
     * Takes the entity markers from the TMX file and creates them.
     */
    private void spawnEntities()
    {
        TiledMapTileLayer layer = (TiledMapTileLayer)map.getLayers().get("spawn");
        if(layer != null)
        {
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
                         * 
                         * Maybe we could change by making an "entity" field
                         * and making the value a string with the entity type? 
                         * but this could get large as the number of types increases
                         * 
                         * 
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
        
        //maybe make these a private member? we would avoid allocating every 
        //frame this way
        Array<Robot> robots = new Array<Robot>();
        Array<Bullet> bullets = new Array<Bullet>();
        Array<Tank> tanks = new Array<Tank>();
        ////////////////////////////////////////////////////////////////
    
        
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
            Array<Character> toIterate = new Array<Character>();
            if(bullet.getWeapon().getTypes().contains(Robot.class, true))
            {
                toIterate.addAll(robots);
            }
            if(bullet.getWeapon().getTypes().contains(Tank.class, true))
            {
                toIterate.addAll(tanks);
            }
            if(bullet.getWeapon().getTypes().contains(Player.class, true))
            {
                toIterate.add(player);
            }
            for(Character entity : toIterate)
            {
                if(bullet.isColliding(entity))
                {
                    bullet.isAlive = false;
                    entity.damage(bullet.getDamage());
                    entity.spawnParticles(this, bullet.x, bullet.y, bullet.angle);
                    //these are for the freeze effect and camera shake
                    //maybe move these into their respective entites later?
                    //as we have to write this here for every interaction7
                    
                    //NOTE: entity should never have access to engine
                    if(entity.isAlive())
                    {
                        engine.sleep(3);
                        engine.shake(entity.getHitShake());
                    }
                    else
                    {
                        engine.sleep(7);
                        engine.shake(entity.getDeathShake());
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
                //this is 'okay' because there should only ever be 1 player instance
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
     * get neighboring tiles of the centre of the entity.
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
     * Gets all entities of type T.
     * 
     * useful for checking against types. - No shit.
     *
     * 
     * @param <T>
     * @param type 
     * @return 
     */
    public <T> Array<T> getEntityType(Class<T> type)
    {
        Array<T> result = new Array<T>();
        
        for(int i = 0; i < entities.size; i++)
        {
            T obj = (T)entities.get(i);
            if(obj.getClass() == type)
            {
                result.add(obj);
            }
        }
        return result;
    }
}
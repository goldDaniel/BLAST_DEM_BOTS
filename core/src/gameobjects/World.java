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
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

/**
 *`
 * @author wrksttnpc
 */
public class World extends GameObject
{
    TiledMap map;
    Tile[][] tiles;
    TiledMapRenderer tmr;
    
    Array<GameObject> entities;
    Array<GameObject> toAddToScene;
    Array<GameObject> toRemoveFromScene;
    
    
    Player player;
    
    boolean updating = false;
    
    
    /**
     * 
     * @param map
     * @param tmr
     * @param s
     * @param sh 
     */
    public World(TiledMap map, OrthogonalTiledMapRenderer tmr, SpriteBatch s, ShapeRenderer sh)
    {
        super(s, sh);
        this.map = map;
        this.tmr = tmr;

        x = 0;
        y = 0;
        width = Tile.SIZE * map.getProperties().get("width", Integer.class);
        height = Tile.SIZE * map.getProperties().get("height", Integer.class);
        
        entities = new Array<GameObject>();
        toAddToScene = new Array<GameObject>();
        toRemoveFromScene = new Array<GameObject>();
        
        createTiles();
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
        for (int i = 0; i < tiles.length; i++)
        {
            for (int j = 0; j < tiles[i].length; j++)
            {
                TiledMapTileLayer layer = (TiledMapTileLayer)map.getLayers().get("collision");
                int tileX = (int)(i * layer.getTileWidth());
                int tileY = (int)(j * layer.getTileHeight());
                
                boolean isSolid = false;
                if(layer.getCell(i, j) != null)
                {
                    isSolid = layer.getCell(i, j).getTile().getProperties().get("isSolid") != null;
                }
                tiles[i][j] = new Tile(s, sh, tileX, tileY, isSolid);
                entities.add(tiles[i][j]);
            }
        }
    }
    
    /**
     * 
     * @param obj to add
     */
    public void addEntity(GameObject obj)
    {
        if(obj == null) return;
        if(updating)toAddToScene.add(obj);
        else entities.add(obj);
    }
    
    /**
     * 
     * @param obj to remove
     */
    public void removeEntity(GameObject obj)
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
     * @param deltaTime 
     */
    public void update(float deltaTime)
    {
        updating = true;
        
        Array<Robot> robots = new Array<Robot>();
        Array<Bullet> bullets = new Array<Bullet>();
        
        for(GameObject entity : entities)
        {
            entity.update(this, deltaTime);
            if(!entity.isAlive)
            {
                toRemoveFromScene.add(entity);
            }
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
            }
        }
        
        for(Robot robot : robots)
        {
            for(Bullet bullet : bullets)
            {
                if(bullet.isColliding(robot))
                {
                    bullet.isAlive = false;
                    removeEntity(bullet);
                    robot.damage(bullet.getDamage());
                    
                    addEntity(new Particle(bullet.x, bullet.y, 4, 4, 
                            12, 200f, -90 - bullet.angle, s, sh));
                }
            }
            
            if(player != null)
            {
                if(robot.isColliding(player))
                {
                    player.damage(1);
                }
            }
            
        }
        
        updating = false;
        
        
        if(toAddToScene.size > 0)
        {
            entities.addAll(toAddToScene);
            toAddToScene.clear();
        }
        
        entities.removeAll(toRemoveFromScene, true);
        toRemoveFromScene.clear();
    }

    @Override
    public void draw()
    {
        tmr.render();
        Player temp = null;
        s.begin();
        for(GameObject entity : entities)
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
    public Array<Tile> getCollisionTiles(GameObject obj)
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
        Array<GameObject> result = new Array<GameObject>();
        
        
        for(int i = 0; i < entities.size; i++)
        {
            GameObject obj = entities.get(i);
            if(obj.getClass() == type)
            {
                result.add(obj);
            }
        }
        return result;
    }
}

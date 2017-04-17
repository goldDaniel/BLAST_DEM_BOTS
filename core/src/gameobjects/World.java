/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
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
    
    
    boolean updating = false;
    
    
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
    
    public void addEntity(GameObject obj)
    {
        if(obj == null) return;
        if(updating)toAddToScene.add(obj);
        else entities.add(obj);
    }
    public void removeEntity(GameObject obj)
    {
        if(obj == null) return;
        if(updating) toRemoveFromScene.add(obj);
        else entities.removeValue(obj, true);
    }
    
    public void update(float deltaTime)
    {
        updating = true;
        for(GameObject entity : entities)
        {
            entity.update(this, deltaTime);
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
        for(GameObject entity : entities)
        {
            if(entity instanceof Player)
            {
                temp = (Player)entity;
            }
            else
            {
                entity.draw();
            }
        }
        if(temp != null) temp.draw();
    }

    @Override
    public void dispose()
    {
    }
    
    /**
     * Pass X & Y world coordinates.
     * get the neighboring tiles of the point(X,Y)
     * 
     * used to get surrounding tiles for collision detection
     * 
     * TODO: write method with (GameObeect) if needed?
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
        
        if(tileX + 1 < tiles.length - 1 &&
           tiles[tileX + 1][tileY].isSolid)
        {
            result.add(tiles[tileX + 1][tileY]);
        }
        
        if(tileY - 1 >= 0 &&
           tiles[tileX][tileY - 1].isSolid)
        {
            result.add(tiles[tileX][tileY - 1]);
        }
        
        if(tileY + 1 < tiles[0].length - 1 &&
           tiles[tileX][tileY + 1].isSolid)
        {
            result.add(tiles[tileX][tileY + 1]);
        }

        
        return result;
    }
}

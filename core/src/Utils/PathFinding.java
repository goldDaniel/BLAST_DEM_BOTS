/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import gameobjects.Tile;
import gameobjects.World;

/**
 *
 * @author wrksttnpc
 */
public class PathFinding
{
    TiledMap map;
    
    
    
    public PathFinding(TiledMap map)
    {
        this.map = map;
    }
    
    public void calculate()
    {
        int mapWidth = map.getProperties().get("width", Integer.class);
         TiledMapTileLayer layer = (TiledMapTileLayer)map.getLayers().get("collision");
        for (int i = 0; i < result.length; i++)
        {
            for (int j = 0; j < result[i].length; j++)
            {
                int tileX = (int)(i * layer.getTileWidth());
                int tileY = (int)(j * layer.getTileHeight());
                
                boolean isSolid = false;
                if(layer.getCell(i, j) != null)
                {
                    isSolid = layer.getCell(i, j).getTile().getProperties().get("isSolid") != null;
                }
            }
        }
    }
    
    public Tile[][] createTiles(TiledMap map, World world)
    {
        Tile[][] result = new Tile[map.getProperties().get("width", Integer.class)]
                        [map.getProperties().get("height", Integer.class)];
        TiledMapTileLayer layer = (TiledMapTileLayer)map.getLayers().get("collision");
        for (int i = 0; i < result.length; i++)
        {
            for (int j = 0; j < result[i].length; j++)
            {
                int tileX = (int)(i * layer.getTileWidth());
                int tileY = (int)(j * layer.getTileHeight());
                
                boolean isSolid = false;
                if(layer.getCell(i, j) != null)
                {
                    isSolid = layer.getCell(i, j).getTile().getProperties().get("isSolid") != null;
                }
            }
        }
        return result;
    }
}

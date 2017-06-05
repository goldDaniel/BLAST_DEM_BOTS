/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import gameobjects.Tile;
import gold.daniel.main.GameEngine;

/**
 *
 * @author wrksttnpc
 */
public class PathFinding
{
    TiledMap map;
    
    PathNode[][] nodes;
    
    PathNode start;
    PathNode end;
    

       
    Array<PathNode> closed;
    Array<PathNode> open;
    
    
    int size = Tile.SIZE;
    
    public PathFinding(TiledMap map)
    {
        this.map = map;
        open = new Array<PathNode>();
        closed = new Array<PathNode>();
        init();
    }
    
    private void init()
    {
        /*
        This should really get its own method, but lazy 
        */
        int mapWidth = map.getProperties().get("width", Integer.class);
        int mapHeight = map.getProperties().get("height", Integer.class);
        
        nodes = new PathNode[mapWidth][mapHeight];
        
        TiledMapTileLayer layer = (TiledMapTileLayer)map.getLayers().get("collision");
        for (int i = 0; i < mapWidth; i++)
        {
            for (int j = 0; j < mapHeight; j++)
            {
                boolean isSolid = false;
                if(layer.getCell(i, j) != null)
                {
                    TiledMapTile tile = layer.getCell(i, j).getTile();
                    
                    
                    if(tile.getProperties().containsKey("start"))
                    {
                        start = nodes[i][j] = new PathNode(i, j, isSolid);
                    }
                    else if(tile.getProperties().containsKey("end"))
                    {
                        end = nodes[i][j] = new PathNode(i, j, isSolid);
                    }
                    else if(tile.getProperties().containsKey("isSolid"))
                    {
                        isSolid = true;
                    }
                }
                if(nodes[i][j] == null)
                {
                    nodes[i][j] = new PathNode(i, j, isSolid);
                }
            }
        }
        for (int i = 0; i < nodes.length; i++)
        {
            for (int j = 0; j < nodes[i].length; j++)
            {
                setNeighbors(i, j);
            }
        }
    }
    
    private void setNeighbors(int i, int j)
    {
        nodes[i][j].neighbors.clear();
        if(i - 1 >= 0)
        {
            if(!nodes[i - 1][j].isSolid)
            {
                nodes[i][j].neighbors.add(nodes[i - 1][j]);
            }
            
            if(j + 1 <= nodes[i].length - 1)
            {
                if(!nodes[i - 1][j + 1].isSolid)
                {
                    nodes[i][j].neighbors.add(nodes[i - 1][ j + 1]);
                }
            }
            if(j - 1 >= 0)
            {
                if(!nodes[i - 1][j - 1].isSolid)
                {
                    nodes[i][j].neighbors.add(nodes[i - 1][ j - 1]);
                }
                
            }
        }
        if(i + 1 <= nodes.length - 1)
        {
            
            if(!nodes[i + 1][j].isSolid)
            {
                nodes[i][j].neighbors.add(nodes[i + 1][j]);
            }
            if(j + 1 <= nodes[i].length - 1)
            {
                if(!nodes[i + 1][j + 1].isSolid)
                {
                    nodes[i][j].neighbors.add(nodes[i + 1][ j + 1]);
                }
            }
            if(j - 1 >= 0)
            {
                if(!nodes[i + 1][j - 1].isSolid)
                {
                    nodes[i][j].neighbors.add(nodes[i + 1][ j - 1]);
                }
            }
        }
        if(j - 1 >= 0)
        {
            if(!nodes[i][j - 1].isSolid)
            {
                nodes[i][j].neighbors.add(nodes[i][j - 1]);
            }
        }
        if(j + 1 <= nodes[0].length - 1)
        {
            if(!nodes[i][j + 1].isSolid)
            {
                nodes[i][j].neighbors.add(nodes[i][j + 1]);
            }
        }
    }

    private int calculateDistanceBetweenNodes(PathNode a, PathNode b)
    {
        return (a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y);
    }
    

    public Array<Vector2> calculate()
    {
        if(end == null) return null;
        
        start.g = 0;
        start.h = calculateDistanceBetweenNodes(start, end);
        start.f = start.h;
        
        open.clear();
        closed.clear();
        open.add(start);
        
        //while there are still nodes to traverse
        while(true)
        {
            PathNode curr = null;
            
            if(open.size == 0)
            {
                return null;
            }
            
            for(PathNode node : open)
            {
                if(curr == null || node.f < curr.f)
                {
                    curr = node;
                }
            }
            if(curr == end)
            {
                break;
            }
            
            open.removeValue(curr, true);
            closed.add(curr);
            
            
            for(PathNode neighbor : curr.neighbors)
            {
                int g = curr.g + neighbor.cost;
                
                if(g < neighbor.g)
                {
                    open.removeValue(neighbor, true);
                    closed.removeValue(neighbor, true);
                }
                
                if(!open.contains(neighbor, true) && !closed.contains(neighbor, true))
                {
                    neighbor.g = g;
                    neighbor.h = calculateDistanceBetweenNodes(neighbor, end);
                    neighbor.f =  neighbor.g + neighbor.h;
                    neighbor.parent = curr;
                    
                    open.add(neighbor);
                }
            }
        }
        
        Array<Vector2> result = new Array<Vector2>();

        PathNode curr = end;
        while(curr.parent != null)
        {
            result.add(new Vector2(curr.x, curr.y));
            curr = curr.parent;
        }
        result.reverse();
        
        
        return result;
    }
    public void update(GameEngine engine)
    {
        int i = (int)engine.getMouse().x / Tile.SIZE;
        int j = (int)engine.getMouse().y / Tile.SIZE;
        if(i < 0)
        {
            i = 0;
        }
        else if(i > nodes.length - 1)
        {
            i = nodes.length - 1;
        }
        
        if(j < 0)
        {
            j = 0;
        }
        if(j > nodes[i].length - 1)
        {
            j = nodes[i].length - 1;
        }
        end = nodes[i][j];
    }
}
class PathNode
{
    int x;
    int y;

    int f;
    int g;
    int h;
    int cost = 10;
    
    boolean isSolid;
    
    Array<PathNode> neighbors;
    PathNode parent;
    
    public PathNode(int x, int y, boolean isSolid)
    {
        this.x = x * 16 + 8;
        this.y = y * 16 + 8;
        this.isSolid = isSolid;
        neighbors = new Array<PathNode>();
    }
}

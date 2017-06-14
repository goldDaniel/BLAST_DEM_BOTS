/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import gameobjects.Entity;
import gameobjects.Tile;

/**
 * The whole purpose of this class is to find 
 * a list of points for an entity to traverse.
 * 
 * handling where the entity is moving to should be handled 
 * in its respective class
 * 
 * @author wrksttnpc
 */
public class PathFinding
{
    TiledMap map;
    
    PathNode[][] nodes;
    
    PathNode start;
    PathNode end;
    
    Array<Vector2> finalPath;
       
    
    Array<PathNode> closed;
    Array<PathNode> open;
    

    public PathFinding(TiledMap map, Entity entity, Entity target)
    {
        this.map = map;
        open = new Array<PathNode>();
        closed = new Array<PathNode>();
        init();
        
        start = nodes[(int)entity.getX() / Tile.SIZE][(int)entity.getY() / Tile.SIZE];
        end = nodes[(int)target.getX() / Tile.SIZE][(int)entity.getY() / Tile.SIZE];
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
                    if(tile.getProperties().containsKey("isSolid"))
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

        }
        if(i + 1 <= nodes.length - 1)
        {
            
            if(!nodes[i + 1][j].isSolid)
            {
                nodes[i][j].neighbors.add(nodes[i + 1][j]);
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
        result.add(new Vector2(start.x, start.y));
        result.reverse();
        
        finalPath = result;
        return result;
    }
    
    /**
     * 
     * @param entity 
     */
    public void update(Entity entity)
    {
        update(entity.getX() + entity.getWidth() / 2, entity.getY() + entity.getHeight() / 2);
    }
    
    /**
     * updates path from given XY coordinates
     * @param x
     * @param y 
     */
    public void update(float x, float y)
    {
        int i = (int)x / Tile.SIZE;
        int j = (int)y / Tile.SIZE;
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
    
    public void draw(ShapeRenderer sh)
    {
        if(finalPath != null)
        {
            for(int i = 0; i < finalPath.size - 1; i++)
            {
                sh.line(finalPath.get(i), finalPath.get(i + 1));
            }
        }
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
        this.x = x * Tile.SIZE + Tile.SIZE  / 2;
        this.y = y * Tile.SIZE + Tile.SIZE  / 2;
        this.isSolid = isSolid;
        neighbors = new Array<PathNode>();
    }
}

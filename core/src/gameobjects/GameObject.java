/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameobjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 *
 * @author wrksttnpc
 */
public abstract class GameObject
{
    protected Vector2 position;
    protected float x;
    protected float y;
    
    protected float width;
    protected float height;
    
    
    protected boolean isAlive;
    
    SpriteBatch s;
    ShapeRenderer sh;
    
    //used to hold player surrounding tiles, should be at most 5 (4 directions + current)
    /*
        010
        111
        010
    */
    Array<Tile> collisionTiles;
    
    static Rectangle rect1 = new Rectangle();
    static Rectangle rect2 = new Rectangle();
    
    
           
    
    public GameObject(SpriteBatch s, ShapeRenderer sh)
    {
        this.s = s;
        this.sh = sh;
        isAlive = true;
        position = new Vector2();
        collisionTiles = new Array<Tile>(5);
    }
    
    public void update(World world, float deltaTime)
    {
        position.x = x;
        position.y = y;
        collisionTiles.clear();
    }
    public abstract void draw();
    public abstract void dispose();
    
    public Vector2 getPosition()
    {
        return position;
    }
    
    public float getX()
    {
        return x;
    }
    
    public float getY()
    {
        return y;
    }
    
    public float getWidth()
    {
        return width;
    }
    
    public float getHeight()
    {
        return height;
    }
    
    protected void handleTileCollisionResponse(Tile tile)
    {
        if(isColliding(tile))
        {
            boolean left = x < tile.x;
            boolean above = y > tile.y;
            
            float horizontalDif;
            float verticalDif;
            
            if(left)
            {
                horizontalDif = x + width - tile.x;
            }
            else
            {
                horizontalDif = tile.x + tile.width- x;
            }
            
            if(above)
            {
                verticalDif = tile.y + tile.height - y;
            }
            else
            {
                verticalDif = y + height - tile.y;
            }
            
            //DO HORIZONTALdd
            if(horizontalDif < verticalDif)
            {
                if(left)
                {
                    x = tile.x - width;
                }
                else
                {
                    x = tile.x + tile.width;
                }
            }
            //DO VERTICAL
            else
            {
                if(above)
                {
                    y = tile.y + tile.height;
                }
                else
                {
                    y = tile.y - height;
                }
            }
        }
    }
 
    
    public boolean isColliding(GameObject obj)
    {
        rect1.x = this.x;
        rect1.y = this.y;
        rect1.width = this.width;
        rect1.height = this.height;
        
        rect2.x = obj.x;
        rect2.y = obj.y;
        rect2.width = obj.width;
        rect2.height = obj.height;
        
        return rect1.overlaps(rect2) || rect2.overlaps(rect1);
    }
}

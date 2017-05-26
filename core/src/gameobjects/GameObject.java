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
    
    /**
     *  calculates angle from center of character to a point X,Y
     * @param x
     * @param y
     * @return 
     */
    protected float calculateAngleToPoint(float x, float y)
    {
        Vector2 temp = new Vector2(this.x + width / 2, this.y + height / 2);
        return temp.sub(x, y).angle();
    }
    
    /**
     * makes entity go to proper position after colliding with an object.
     * maybe move this method the the World class? 
     * @param obj 
     */
    protected void handleMoveCollisionResponse(GameObject obj)
    {
        //broad check if colliding
        if(isColliding(obj))
        {
            //decide what side the object is on relative to the tile
            
            //horizontal side
            boolean left = x < obj.x;
            //vertical side
            boolean above = y > obj.y;
            
            //holds how deep the object is inside the tile on each axis
            float horizontalDif;
            float verticalDif;
            
            //determine the differences for depth
            if(left)
            {
                horizontalDif = x + width - obj.x;
            }
            else
            {
                horizontalDif = obj.x + obj.width- x;
            }
            
            if(above)
            {
                verticalDif = obj.y + obj.height - y;
            }
            else
            {
                verticalDif = y + height - obj.y;
            }
            
            //DO HORIZONTAL
            if(horizontalDif < verticalDif)
            {
                if(left)
                {
                    x = obj.x - width;
                }
                else
                {
                    x = obj.x + obj.width;
                }
            }
            //DO VERTICAL
            else
            {
                if(above)
                {
                    y = obj.y + obj.height;
                }
                else
                {
                    y = obj.y - height;
                }
            }
        }
    }
 
    /**
     * checks collision with gameObject
     * @param obj
     * @return 
     */
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
    
    /**
     * checks collision with rectangle.
     * @param rect
     * @return 
     */
    public boolean isColliding(Rectangle rect)
    {
        rect1.x = this.x;
        rect1.y = this.y;
        rect1.width = this.width;
        rect1.height = this.height;
        
        return rect1.overlaps(rect) || rect.overlaps(rect1);
    }
}

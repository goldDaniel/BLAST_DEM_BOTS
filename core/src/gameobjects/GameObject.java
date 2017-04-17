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
    
    SpriteBatch s;
    ShapeRenderer sh;
    
    static Rectangle rect1 = new Rectangle();
    static Rectangle rect2 = new Rectangle();
    
    public GameObject(SpriteBatch s, ShapeRenderer sh)
    {
        this.s = s;
        this.sh = sh;
        position = new Vector2();
    }
    
    public void update(World world, float deltaTime)
    {
        position.x = x;
        position.y = y;
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameobjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

/**
 *
 * @author wrksttnpc
 */
public class Bullet extends Entity
{
    float angle;
    float speed;

    int damage = 1;
    
    TextureRegion texture;
    
    
    public Bullet(SpriteBatch s, ShapeRenderer sh, float x, float y, float angle, float speed, Texture texture)
    {
        super(s, sh);
        this.speed = speed;
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.texture = new TextureRegion(texture);
        width = texture.getWidth();
        height = texture.getHeight();
    }

    @Override
    public void update(World world, float deltaTime)
    {
        super.update(world, deltaTime);
        
        x += speed * MathUtils.cosDeg(angle) * deltaTime;
        y += speed * MathUtils.sinDeg(angle) * deltaTime;
        
        if(x <= world.x || x >= world.x + world.width ||
           y <= world.y || y >= world.y + world.height)
        {
            isAlive = false;
            world.removeEntity(this);
        }
        
        if(isAlive)
        {
            collisionTiles.addAll(world.getCollisionTiles(this));
            for(Tile tile : collisionTiles)
            {
                handleMoveCollisionResponse(tile);
            }
            
            if(!isAlive)
            {
                int temp = MathUtils.random(25);
                    world.addEntity(new Particle(x, y, 4, 4, 
                        15, speed - MathUtils.random(150, 250), 
                        angle + 180 + temp, 
                        s, sh));
                    world.addEntity(new Particle(x, y, 4, 4, 
                        15, speed - MathUtils.random(150, 250), 
                        angle + 180 - temp, 
                        s, sh));
                
                world.removeEntity(this);
            }
        }
    }
    
    @Override
    public void draw()
    {
        s.draw(texture, x - width / 2, y - height / 2, width / 2, height / 2, width, height, 1, 1, angle);
    }
    
    public int getDamage()
    {
        return damage;
    }
    
    @Override
    protected void handleMoveCollisionResponse(Entity obj)
    {
        //so wall collision is less strict
        Rectangle rect = new Rectangle(x + width / 2 - 2, y + height / 2 - 2, 4, 4);
        if(obj.isColliding(rect))
        {
            isAlive = false;
        }
    }

    @Override
    public void dispose()
    {
    }
}

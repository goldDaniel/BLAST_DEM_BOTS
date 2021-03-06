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
import weapons.Weapon;

/**
 *
 * @author wrksttnpc
 */
public class Bullet extends Entity
{
    //angle and speed to move at
    float angle;
    float speed;

    //the amount of damage done by the bullet
    int damage = 1;
    
    TextureRegion texture;
    Weapon weapon;
    
    
    /**
     * 
     * @param s
     * @param sh
     * @param x
     * @param y
     * @param damage
     * @param angle
     * @param speed
     * @param texture
     * @param weapon 
     */
    public Bullet(SpriteBatch s, ShapeRenderer sh, float x, float y, int damage, float angle, float speed, Texture texture,
            Weapon weapon)
    {
        super(s, sh);
        this.speed = speed;
        this.x = x;
        this.y = y;
        this.damage = damage;
        this.angle = angle;
        this.texture = new TextureRegion(texture);
        this.weapon = weapon;
        width = texture.getWidth();
        height = texture.getHeight();
    }

    @Override
    public void update(World world, float deltaTime)
    {
        //always call super.update()
        super.update(world, deltaTime);
        
        //moves the bullet
        x += speed * MathUtils.cosDeg(angle) * deltaTime;
        y += speed * MathUtils.sinDeg(angle) * deltaTime;
        
        //if the bullet is outside world bounds we can remove it
        if(x <= world.x || x >= world.x + world.width ||
           y <= world.y || y >= world.y + world.height)
        {
            isAlive = false;
            world.removeEntity(this);
        }
        
        //if the bullet is alive then do its updates
        if(isAlive)
        {
            //checks for colliding tiles
            collisionTiles.addAll(world.getCollisionTiles(this));
            for(Tile tile : collisionTiles)
            {
                handleMoveCollisionResponse(tile);
            }
            
            //if no logner alive, spawn the death particles
            if(!isAlive)
            {
                float randDif = MathUtils.random(speed / 2);
                int temp = MathUtils.random(25);
                    world.addEntity(new Particle(x, y, 4, 4, 
                        15, speed - randDif, 
                        angle + 180 + temp, 
                        s, sh));
                    world.addEntity(new Particle(x, y, 4, 4, 
                        15, speed - randDif, 
                        angle + 180 - temp, 
                        s, sh));
                
                world.removeEntity(this);
            }
        }
    }
    
    public Weapon getWeapon()
    {
        return weapon;
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
    
    /**
     * special hitbox params and different behaviour demand an overrride.
     * @param obj 
     */
    @Override
    protected void handleMoveCollisionResponse(Entity obj)
    {
        //so wall collision is less strict
        Rectangle rect = new Rectangle(x + width / 2 - 2, y + height / 2 - 2, 4, 4);
        if(obj.isColliding(rect))
        {
            super.handleMoveCollisionResponse(obj);
            isAlive = false;
        }
    }

    @Override
    public void dispose()
    {
    }
}

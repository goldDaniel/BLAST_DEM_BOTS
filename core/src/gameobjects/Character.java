/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameobjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;

/**
 *
 * @author wrksttnpc
 */
public abstract class Character extends Entity
{
    protected float speed = 75f;

    protected float angle = 0;

    protected int health = 10;
    protected int healthMax = 10;
    
    public Character(SpriteBatch s, ShapeRenderer sh)
    {
        super(s, sh);
    }
    
    public abstract int getHitShake();
    public abstract int getDeathShake();
    
    @Override
    public void update(World world, float deltaTime)
    {
        if(health <= 0)
        {
            isAlive = false;
        }
    }
    
    public int getHealth()
    {
        return health;
    }
    
    public int getMaxHealth()
    {
        return healthMax;
    }
    
    public void damage(int amount)
    {
        health -= amount;
        if (health <= 0)
        {
            health = 0;
            isAlive = false;
        }
    }
    
    public boolean isAlive()
    {
        return health > 0;
    }

    @Override
    public void draw()
    {
    }

    @Override
    public void dispose()
    {
    }    
    
    /**
     * default particle spwaning for characters
     * @param world
     * @param x
     * @param y
     * @param angle 
     */
    public void spawnParticles(World world, float x, float y, float angle)
    {
        for (int i = 0; i < 10; i++)
        {
            world.addEntity(new Particle(x, y, 3, 3, 
                10 + MathUtils.random(10), 50f + MathUtils.random(200), 
                angle, 
                s, sh));
        }
    }
}

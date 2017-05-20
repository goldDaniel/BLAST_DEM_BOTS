/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameobjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 *
 * @author wrksttnpc
 */
public abstract class Character extends GameObject
{
    protected float speed = 75f;

    protected float angle = 0;

    protected int health = 10;
    protected int healthMax = 10;
    
    public Character(SpriteBatch s, ShapeRenderer sh)
    {
        super(s, sh);
    }
    
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
     

    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameobjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import gold.daniel.main.Textures;

/**
 *
 * @author wrksttnpc
 */
public class Particle extends Character
{

    Texture texture = Textures.PARTICLE;
    
    public Particle(float x, float y, int width, int height,
            int lifespan, float speed, float angle, SpriteBatch s, ShapeRenderer sh)
    {
        super(s, sh);
        
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.speed = speed;
        this.angle = angle;
        healthMax = health = lifespan;
       
    }
    
    @Override
    public void update(World world, float deltaTime)
    {
        super.update(world, deltaTime);
        
        x += speed * ((float)health / (float)healthMax) * MathUtils.cosDeg(angle) * deltaTime;
        y += speed * ((float)health / (float)healthMax) * MathUtils.sinDeg(angle) * deltaTime;
        
        health--;
    }

    @Override
    public void draw()
    {
        s.draw(texture, x, y, width, height);
    }

    @Override
    public void dispose()
    {
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameobjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
        float alpha = (float)health / (float)healthMax;
        if(alpha > 0.3f)
        {
            alpha = 1f;
        }
        //to allow for fade outs
        Color temp = Color.WHITE.cpy();
        temp.a = alpha;
        s.setColor(temp);
        s.draw(new TextureRegion(texture), x, y, width / 2, height / 2, 
                width, height, 1, 1, 180 * ((float)health / (float)healthMax));
        s.setColor(Color.WHITE);
    }

    @Override
    public void dispose()
    {
    }

    @Override
    public int getHitShake()
    {
        return 0;
    }

    @Override
    public int getDeathShake()
    {
        return 0;
    }
    
}

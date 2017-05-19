/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameobjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import gold.daniel.main.Textures;

/**
 *
 * @author wrksttnpc
 */
public class Particle extends Character
{

    Texture texture = Textures.PARTICLE;
    
    public Particle(float x, float y, int width, int height, int lifespan, SpriteBatch s, ShapeRenderer sh)
    {
        super(s, sh);
        
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        healthMax = health = lifespan;
       
    }
    
    public void update()
    {
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

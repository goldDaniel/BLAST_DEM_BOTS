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
import com.badlogic.gdx.utils.Array;

/**
 *
 * @author goldd
 */
public class Robot extends Character
{
    
    Texture texture;
    
    public Robot(float x, float y, SpriteBatch s, ShapeRenderer sh, Texture texture)
    {
        this(s, sh, texture);
        this.x = x;
        this.y = y;
    }
    
    /**
     * 
     * @param s
     * @param sh
     * @param texture 
     */
    public Robot(SpriteBatch s, ShapeRenderer sh, Texture texture)
    {
        super(s, sh);
        this.x = 100;
        this.y = 100;
        this.width = 32;
        this.height = 32;
        this.texture = texture;
        
        speed = 20f;
        angle = 0;
        
        healthMax = health = 10;
    }

    /**
     * 
     * @param world
     * @param deltaTime 
     */
    @Override
    public void update(World world, float deltaTime)
    {
        super.update(world, deltaTime);
        for(int i = 0; i < world.entities.size; i++)
        {
            if(world.entities.get(i) instanceof Player)
            {
                float playerX = world.entities.get(i).x;
                float playerY = world.entities.get(i).y;
                
                //offset for rotation 
                angle =  180 + calculateAngleToPoint(playerX, playerY);
                break;
            }
        }     
        collisionTiles = world.getCollisionTiles(this);
        
        x += speed * MathUtils.cosDeg(angle) * deltaTime;
        for(Tile tile : collisionTiles)
        {
            handleMoveCollisionResponse(tile);
        }
        y += speed * MathUtils.sinDeg(angle) * deltaTime;
        for(Tile tile : collisionTiles)
        {
            handleMoveCollisionResponse(tile);
        }
        collisionTiles.clear();
        
        if(!isAlive)
        {
            for (int i = 0; i < 100; i++)
            {
                world.addEntity(new Particle(x + width / 2, y + height / 2,  4, 4, 
                        MathUtils.random(25), MathUtils.random(600), MathUtils.random(360), s, sh));
            }
        }
        
        
    }

    /**
     * 
     */
    @Override
    public void draw()
    {
        s.draw(new TextureRegion(texture), x, y, width / 2, height / 2, width, height, 1, 1, angle);
    }

    @Override
    public void dispose()
    {

    }

}

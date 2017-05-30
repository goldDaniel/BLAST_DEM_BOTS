/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameobjects;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import gold.daniel.main.Sounds;
import gold.daniel.main.Textures;

/**
 *
 * @author wrksttnpc
 */
public class Tank extends Character
{
    
    static TextureRegion body = new TextureRegion(Textures.TANK_BODY);
    static TextureRegion cannon =  new TextureRegion(Textures.TANK_CANNON);
    
    static Sound explosion = Sounds.EXPLOSION_TANK;
    
    float cannonAngle;
    
    
    
    public Tank(float x, float y, SpriteBatch s, ShapeRenderer sh)
    {
        this(s, sh);
        this.x = x;
        this.y = y;
    }
    
    /**
     * 
     * @param s
     * @param sh
     */
    public Tank(SpriteBatch s, ShapeRenderer sh)
    {
        super(s, sh);
        this.x = 100;
        this.y = 100;
        this.width = body.getRegionWidth();
        this.height = body.getRegionHeight();
        
        speed = 20f;
        angle = 0;
        
        healthMax = health = 20;
    }
    
    @Override
    public void update(World world, float deltaTime)
    {        
        super.update(world, deltaTime);
        
        Player player = world.getPlayer();
        if(player != null)
        {
           float wantedAngle = calculateAngleToPoint(player.x, player.y);
           
           cannonAngle = MathUtils.lerpAngleDeg(cannonAngle, wantedAngle, 0.05f);
        }
        
        
        if(!isAlive)
        {
            explosion.play();
            for (int i = 0; i < 1000; i++)
            {
                world.addEntity(new Particle(x + width / 2, y + height / 2,  4, 4, 
                        MathUtils.random(25), MathUtils.random(600), MathUtils.random(360), s, sh));
            }
        }
    }
    
    @Override
    public void draw()
    {
        s.draw(body, x, y, width / 2, height / 2, width, height, 1, 1, angle);
        s.draw(cannon, x, y + 17f, 45f, cannon.getRegionHeight() / 2, cannon.getRegionWidth(), cannon.getRegionHeight(), 1, 1, cannonAngle);
    }
    
    @Override
    public void spawnParticles(World world, float x, float y, float angle)
    {
         for (int i = 0; i < 10; i++)
        {
            world.addEntity(new Particle(x, y, 3, 3, 
                10 + MathUtils.random(10), 50f + MathUtils.random(200), 
                180 + angle + MathUtils.random(-25, 25), 
                s, sh));
        }
    }
}

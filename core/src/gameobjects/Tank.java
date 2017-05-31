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
import com.badlogic.gdx.math.Vector2;
import gold.daniel.main.Sounds;
import gold.daniel.main.Textures;
import weapons.TankCannon;
import weapons.Weapon;

/**
 *
 * @author wrksttnpc
 */
public class Tank extends Character
{
    
    static TextureRegion bodyTex = new TextureRegion(Textures.TANK_BODY);
    static TextureRegion cannonTex =  new TextureRegion(Textures.TANK_CANNON);
    
    static Sound explosion = Sounds.EXPLOSION_TANK;
    
    float cannonX;
    float cannonY;
    float cannonAngle;
    
    Weapon cannon;
    
    
    public Tank(float x, float y, SpriteBatch s, ShapeRenderer sh)
    {
        this(s, sh);
        this.x = x;
        this.y = y;
        
        cannon = new TankCannon();
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
        this.width = bodyTex.getRegionWidth();
        this.height = bodyTex.getRegionHeight();
        
        speed = 20f;
        angle = 0;
        
        cannonX = x;
        cannonY = y + 17f;
        
        healthMax = health = 20;
    }
    
    @Override
    public void update(World world, float deltaTime)
    {        
        super.update(world, deltaTime);
        cannonX = x;
        cannonY = y + 17f;
        
        cannon.update();
        Player player = world.getPlayer();
        if(player != null)
        {
           float wantedAngle = calculateAngleToPoint(player.x + player.width / 2, player.y + player.height / 2);
           
           cannonAngle = MathUtils.lerpAngleDeg(cannonAngle, wantedAngle, 0.05f);
           
           world.addEntity(cannon.fireBullet(s, sh, 
                   cannonX + cannonTex.getRegionWidth() / 2, 
                   cannonY + cannonTex.getRegionHeight() / 2,
                   cannonAngle - 90));
        }
        
        
        if(!isAlive)
        {
            explosion.play();
            for (int i = 0; i < 250; i++)
            {
                world.addEntity(new Particle(x + width / 2, y + height / 2,  4, 4, 
                        MathUtils.random(25), MathUtils.random(600), MathUtils.random(360), s, sh));
            }
        }
    }
    
    @Override
    public void draw()
    {
        s.draw(bodyTex, x, y, width / 2, height / 2, width, height, 1, 1, angle);
        s.draw(cannonTex, cannonX, cannonY, 45f, cannonTex.getRegionHeight() / 2, cannonTex.getRegionWidth(), cannonTex.getRegionHeight(), 1, 1, cannonAngle);
    }
    /**
     *  calculates angle from center of character to a point X,Y
     * @param x
     * @param y
     * @return 
     */
    @Override
    protected float calculateAngleToPoint(float x, float y)
    {
        Vector2 temp = new Vector2(cannonX + cannonTex.getRegionWidth() / 2, cannonY + cannonTex.getRegionHeight() / 2);
        return temp.sub(x, y).angle();
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

    @Override
    public int getHitShake()
    {
        return 3;
    }

    @Override
    public int getDeathShake()
    {
        return 10;
    }
}

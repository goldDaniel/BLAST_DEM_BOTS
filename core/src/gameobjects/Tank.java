/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameobjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import gold.daniel.main.Textures;

/**
 *
 * @author wrksttnpc
 */
public class Tank extends Character
{
    
    TextureRegion body = new TextureRegion(Textures.TANK_BODY);
    TextureRegion cannon =  new TextureRegion(Textures.TANK_CANNON);
    
    
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
        
        healthMax = health = 50;
    }
    
    @Override
    public void update(World world, float deltaTime)
    {
        Player player = (Player)world.getEntityType(Player.class).first();
        Vector2 temp = new Vector2(cannonAngle, 0);
        temp.lerp(new Vector2(calculateAngleToPoint(player.x, player.y), 0), 0.02f);
        cannonAngle = temp.x;
        
        if(!isAlive)
        {
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
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package weapons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import gameobjects.Bullet;

/**
 *
 * @author goldd
 */
public class Weapon {
    
    boolean canShoot;
    
    float width;
    float height;
    
    float speed;
    
    float timer;
    float delay;
    
    Texture texture;
    
    Sound fireSound;
    
    public Weapon(float width, float height, float delay, float speed, Texture texture)
    {
        this.width = width;
        this.height = height;
        this.delay = delay;
        timer = delay;
        this.speed = speed;
        this.texture = texture;
        
        fireSound = Gdx.audio.newSound(Gdx.files.internal("audio/shoot-1.wav"));
        
        canShoot = true;
    }
    
    public void update()
    {
        if(timer-- <= 0)
        {
            timer = delay;
            canShoot = true;
        }
        else
        {
            timer--;
        }
    }
    
    public boolean canShoot()
    {
        return canShoot;
    }
    
    public Bullet fireBullet(SpriteBatch s, ShapeRenderer sh, float x, float y, float angle)
    {
        Bullet result = null;
        if(canShoot)
        {
            canShoot = false;
            timer = delay;
            result = new Bullet(s, sh, x, y, angle - 90, speed, texture);
            fireSound.play();
        }
        return result;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package weapons;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import gameobjects.Bullet;
import gold.daniel.main.Sounds;

/**
 *
 * @author goldd
 */
public class Weapon {
    
    boolean canShoot;
    
    float width;
    float height;
    
    int ammoCur;
    int ammoMax;
    
    float speed;
    
    float timer;
    float delay;
    
    Texture texture;
    
    static Sound  fireSound = Sounds.FIRE_SOUND;
    
    static Sound noAmmoSound = Sounds.NO_AMMO;
    
    public Weapon(float width, float height, int ammoMax, float delay, float speed, Texture texture)
    {
        if(texture != null)
        {
            this.width = texture.getWidth();
            this.height = texture.getHeight();
        }
        else
        {
        }
        this.ammoMax = ammoCur = ammoMax;
        this.delay = delay;
        timer = delay;
        this.speed = speed;
        this.texture = texture;
        
       
        
        
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
    
    public boolean hasAmmo()
    {
        return ammoCur > 0;
    }
    
    public int getCurrentAmmo()
    {
        return ammoCur;
    }
    
    public int getMaxAmmo()
    {
        return ammoMax;
    }
    
    public void reload()
    {
        ammoCur = ammoMax;
    }
    
    public Bullet fireBullet(SpriteBatch s, ShapeRenderer sh, float x, float y, float angle)
    {
        Bullet result = null;
        if(canShoot)
        {
            if(hasAmmo())
            {
                result = new Bullet(s, sh, x, y, angle - 90, speed, texture);
                fireSound.play();
                ammoCur--;
            }
            else
            {
                noAmmoSound.play(0.4f);
                
            }
            timer = delay;
            canShoot = false;
        }
        
        return result;
    }
}

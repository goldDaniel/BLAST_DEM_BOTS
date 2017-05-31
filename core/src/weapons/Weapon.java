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
import com.badlogic.gdx.utils.Array;
import gameobjects.Bullet;
import gold.daniel.main.Sounds;

/**
 *
 * @author goldd
 */
public class Weapon {
    
    boolean canShoot;
    
    int damage;
    
    float width;
    float height;
    
    float speed;
    
    float timer;
    float delay;
    
    Texture texture;
    
    static Sound  fireSound = Sounds.FIRE_SOUND;
    
    static Sound noAmmoSound = Sounds.NO_AMMO;
    
    Array<Class> canCollide;
    
    public Weapon(){}
    
    public Weapon(float width, float height, float delay, float speed, 
            int damage, Texture texture, Array<Class> canCollide)
    {
        if(texture != null)
        {
            this.width = texture.getWidth();
            this.height = texture.getHeight();
        }
        else
        {
        }
        this.delay = delay;
        timer = delay;
        this.speed = speed;
        this.damage = damage;
        this.texture = texture;
        this.canCollide = canCollide;
       
        
        
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
    
    public Array<Class> getTypes()
    {
        return canCollide;
    }

    public Bullet fireBullet(SpriteBatch s, ShapeRenderer sh, float x, float y, float angle)
    {
        Bullet result = null;
        if(canShoot)
        {
            result = new Bullet(s, sh, x, y, damage, angle - 90, speed, texture, this);
            fireSound.play();
            
            timer = delay;
            canShoot = false;
        }
        
        return result;
    }
}

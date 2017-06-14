/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package weapons;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import gameobjects.Player;
import gameobjects.Robot;
import gold.daniel.main.Textures;

/**
 *
 * @author wrksttnpc
 */
public class TankCannon extends Weapon
{
    
    public TankCannon(float width, float height, float delay, float speed,
            int damage, Texture texture, Array<Class> canCollide)
    {
        super(width, height, delay, speed, damage, texture, canCollide);
    }
    
    public TankCannon()
    {
        Array<Class> cc = new Array<Class>();
        cc.add(Player.class);
        cc.add(Robot.class);
        this.width = Textures.BULLET_4.getWidth();
        this.height = Textures.BULLET_4.getHeight();
        this.delay = 500f;
        this.speed = 200f;
        this.damage = 2;
        this.texture = Textures.BULLET_4;
        this.canCollide = cc;
    }
    
}

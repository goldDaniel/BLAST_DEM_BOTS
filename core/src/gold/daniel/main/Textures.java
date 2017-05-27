/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gold.daniel.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

/**
 *
 * @author wrksttnpc
 */
public class Textures
{
    public static Texture PLAYER = new Texture(Gdx.files.internal("characters/example.png"));
    
    public static Texture ROBOT_BODY = new Texture(Gdx.files.internal("characters/robot/body.png"));
    public static Texture ROBOT_HEAD = new Texture(Gdx.files.internal("characters/robot/head.png"));
    public static Texture ROBOT_WEAPON = new Texture(Gdx.files.internal("characters/robot/weapon.png"));
    
    public static Texture BULLET_12 = new Texture(Gdx.files.internal("weapons/bullets/4.png"));
    
    public static Texture TANK_BODY = new Texture(Gdx.files.internal("characters/tank-base.png"));
    public static Texture TANK_CANNON = new Texture(Gdx.files.internal("characters/tank-cannon.png"));
    
    public static Texture HEALTHBAR_END = new Texture(Gdx.files.internal("hud/part-1.png"));
    public static Texture HEALTHBAR_MID = new Texture(Gdx.files.internal("hud/part-2.png"));
    public static Texture HEALTHBAR_RECT = new Texture(Gdx.files.internal("hud/life-rectangle.png"));
 
    public static Texture AMMO_TEXTURE = new Texture(Gdx.files.internal("hud/ammo.png"));
    public static Texture AMMO_EMPTY_TEXTURE = new Texture(Gdx.files.internal("hud/ammo-empty.png"));
 
    public static Texture PARTICLE = new Texture(Gdx.files.internal("particle.png"));
}

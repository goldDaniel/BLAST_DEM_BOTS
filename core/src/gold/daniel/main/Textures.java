/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gold.daniel.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ObjectMap;

/**
 *
 * @author wrksttnpc
 */
public class Textures
{
    public final static Texture PLAYER = new Texture(Gdx.files.internal("characters/example.png"));
    
    public final static Texture ROBOT_BODY = new Texture(Gdx.files.internal("characters/robot/body.png"));
    public final static Texture ROBOT_HEAD = new Texture(Gdx.files.internal("characters/robot/head.png"));
    public final static Texture ROBOT_WEAPON = new Texture(Gdx.files.internal("characters/robot/weapon.png"));
    
    public final static Texture BULLET_12 = new Texture(Gdx.files.internal("weapons/bullets/4.png"));
    
    public final static Texture TANK_BODY = new Texture(Gdx.files.internal("characters/tank-base.png"));
    public final static Texture TANK_CANNON = new Texture(Gdx.files.internal("characters/tank-cannon.png"));
    
    public final static Texture HEALTHBAR_END = new Texture(Gdx.files.internal("hud/part-1.png"));
    public final static Texture HEALTHBAR_MID = new Texture(Gdx.files.internal("hud/part-2.png"));
    public final static Texture HEALTHBAR_RECT = new Texture(Gdx.files.internal("hud/life-rectangle.png"));
 
    public final static Texture AMMO_TEXTURE = new Texture(Gdx.files.internal("hud/ammo.png"));
    public final static Texture AMMO_EMPTY_TEXTURE = new Texture(Gdx.files.internal("hud/ammo-empty.png"));
 
    public final static Texture PARTICLE = new Texture(Gdx.files.internal("particle.png"));
    
    public final static Texture ARROW = new Texture(Gdx.files.internal("hud/arrow.png"));
    
    
    //LETTERS ARE 35x28 WIITH A SPACING OF 9
    public final static Texture FONT = new Texture(Gdx.files.internal("font.png"));
    
    
    
    public final static ObjectMap<Character, TextureRegion> CHARACTERS = new ObjectMap<Character, TextureRegion>();
    public final static ObjectMap<Integer, TextureRegion> NUMBERS = new ObjectMap<Integer, TextureRegion>();
    
    private final static TextureRegion[] CHARACTER_ARRAY= 
    {
        new TextureRegion(FONT, 0, 0, 35, 28), //A
        new TextureRegion(FONT, 44, 0, 35, 28),//B 
        new TextureRegion(FONT, 88, 0, 35, 28),//C
        new TextureRegion(FONT, 132, 0, 35, 28),//D
        new TextureRegion(FONT, 176, 0, 35, 28),//E
        new TextureRegion(FONT, 220, 0, 35, 28),//F
        new TextureRegion(FONT, 264, 0, 35, 28),//G
        new TextureRegion(FONT, 310, 0, 35, 28),//H
        new TextureRegion(FONT, 354, 0, 23, 28),//I
        new TextureRegion(FONT, 386, 0, 35, 28),//J 421
        new TextureRegion(FONT, 430, 0, 35, 28),//K
        new TextureRegion(FONT, 474, 0, 35, 28),//L
        new TextureRegion(FONT, 518, 0, 47, 28),//M
        new TextureRegion(FONT, 574, 0, 35, 28),//N
        
        new TextureRegion(FONT, 0, 42, 35, 28),//O
        new TextureRegion(FONT, 44, 42, 35, 28),//P
        new TextureRegion(FONT, 88, 42, 35, 28),//Q
        new TextureRegion(FONT, 132, 42, 35, 28),//R
        new TextureRegion(FONT, 176, 42, 35, 28),//S
        new TextureRegion(FONT, 220, 42, 35, 28),//T
        new TextureRegion(FONT, 264, 42, 35, 28),//U
        new TextureRegion(FONT, 310, 42, 35, 28),//V
        new TextureRegion(FONT, 354, 42, 47, 28),//W
        new TextureRegion(FONT, 410, 42, 35, 28),//X
        new TextureRegion(FONT, 454, 42, 35, 28),//Y
        new TextureRegion(FONT, 498, 42, 35, 28)//Z
    };
    
    private final static TextureRegion[] NUMBER_ARRAY = 
    {
        new TextureRegion(FONT, 542, 42, 35, 28),//0
        new TextureRegion(FONT, 584, 42, 25, 28),//1
        new TextureRegion(FONT, 0,  84, 35, 28),//2
        new TextureRegion(FONT, 44, 84, 35, 28),//3
        new TextureRegion(FONT, 88, 84, 35, 28),//4
        new TextureRegion(FONT, 132, 84, 35, 28),//5
        new TextureRegion(FONT, 176, 84, 35, 28),//6
        new TextureRegion(FONT, 220, 84, 35, 28),//7
        new TextureRegion(FONT, 264, 84, 47, 28),//8
        new TextureRegion(FONT, 310, 84, 35, 28),//9
    };
    
    public final static void loadMaps()
    {
        for(char i = 'a'; i <= 'z'; i++)
        {
            CHARACTERS.put(i, CHARACTER_ARRAY[i - 'a']);
        }
        for (int i = 0; i < 10; i++)
        {
            NUMBERS.put(i, NUMBER_ARRAY[i]);
        }
    }
}

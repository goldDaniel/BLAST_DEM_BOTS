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
    public static Texture player = new Texture("characters/example.png");
    
    public static Texture healthBarEnd = new Texture("hud/part-1.png");
    public static Texture healthBarMid = new Texture("hud/part-2.png");
    public static Texture healthRect = new Texture("hud/life-rectangle.png");
 
    public static Texture ammoTexture = new Texture(Gdx.files.internal("hud/ammo.png"));
    public static Texture noAmmoTexture = new Texture(Gdx.files.internal("hud/ammo-empty.png"));
}

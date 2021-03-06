/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gold.daniel.main;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 *
 * @author wrksttnpc
 */
public abstract class Screen
{
    
    public static final String LOADING = "LOADING";
    public static final String MAIN_MENU = "MAIN_MENU";
    public static final String GAME = "GAME";
    public static final String HOW_TO_PLAY = "HOW_TO_PLAY";
    public static final String OPTIONS = "OPTIONS";
    public static final String PATHFINDING = "PATHFINDING";
    
    protected GameEngine engine;
    
    protected SpriteBatch s;
    protected SpriteBatch hudBatch;
    protected ShapeRenderer sh;

    
    /**
     * Takes things needed by every screen.
     * @param engine
     * @param s
     * @param hudBatch
     * @param sh 
     */
    public Screen(GameEngine engine, SpriteBatch s, SpriteBatch hudBatch, ShapeRenderer sh)
    {
        this.engine = engine;
        this.s = s;
        this.hudBatch = hudBatch;
        this.sh = sh;
    }
    
    public abstract void load();
    
    public abstract void enter();
    
    public abstract void exit();
    
    public abstract void update(float deltaTime);
    
    public abstract void draw();
    
    public abstract void destroy();
    
    protected void drawString(String str, int x, int y, float scale)
    {
        String string = str.toLowerCase();
        float  distance = 0;
        for(int i = 0; i < string.length(); i++)
        {
            if(Textures.CHARACTERS.containsKey(string.charAt(i)))
            {
                TextureRegion tex = Textures.CHARACTERS.get(string.charAt(i));
                
                hudBatch.draw(tex,x + distance, y, 
                        tex.getRegionWidth() * scale, 
                        tex.getRegionHeight() * scale);
                distance += tex.getRegionWidth() * scale + 4;
            }
            else if(Character.isWhitespace(string.charAt(i)))
            {
                distance += 35f * scale;
            }
        }
    }
    
    protected void drawString(String str, int x, int y)
    {
        drawString(str, x, y, 1);
    }
}

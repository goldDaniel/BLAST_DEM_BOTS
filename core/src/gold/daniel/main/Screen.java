/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gold.daniel.main;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
}

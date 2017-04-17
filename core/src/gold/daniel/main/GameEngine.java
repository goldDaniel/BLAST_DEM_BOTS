/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gold.daniel.main;

import screens.HowToPlayScreen;
import screens.MainMenuScreen;
import screens.TestGameScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.viewport.FitViewport;


/**
 *
 * @author wrksttnpc
 */
public class GameEngine
{

    Screen currentScreen;

    FitViewport viewport;
    OrthographicCamera camera;

    SpriteBatch s;
    ShapeRenderer sh;

    BitmapFont fontDefault;
    
    ArrayMap<String, Screen> screens;
  
    
    
    private boolean switchScreens;
    
    private String nextScreen;
    
    public GameEngine()
    {

        camera = new OrthographicCamera();
        viewport = new FitViewport(Main.WIDTH, Main.HEIGHT, camera);
        viewport.apply();

        camera.position.x = Main.WIDTH / 2;
        camera.position.y = Main.HEIGHT / 2;

        s = new SpriteBatch();
        s.enableBlending();
        sh = new ShapeRenderer();
        
        Fonts.loadFonts();
       

        screens = new ArrayMap<String, Screen>();
        
        screens.put(Screen.MAIN_MENU, new MainMenuScreen(this, s, sh));
        screens.put(Screen.HOW_TO_PLAY, new HowToPlayScreen(this, s, sh));
        screens.put(Screen.TEST_GAME, new TestGameScreen(this, s, sh));
        
        currentScreen = new MainMenuScreen(this, s, sh);
        currentScreen.load();
    }

    /**
     * do engine things. updating camera, polling input. where calculations
     * should be done.
     */
    public void updateEngine()
    {
        camera.update();
       
    }

    /**
     * Like engine, but for when entities interact. What I want to do, is that
     * any time I write the same thing in 2 different scenes, it must be
     * re-factored into updateEngine()
     * @param deltaTime time since last frame divided by 0.01666f
     *                  multiply changes by it
     */
    public void updateGame(float deltaTime)
    {
        if (currentScreen != null)
        {
            currentScreen.update(deltaTime);
        }
        
    }

    /**
     * Render.
     */
    public void draw()
    {
        s.setProjectionMatrix(camera.combined);
        sh.setProjectionMatrix(camera.combined);
        if (currentScreen != null)
        {
            currentScreen.draw();
        }
    }

    /**
     * 
     * THIS METHOD IS NOT FOR DETECTING MULTIPLE KEYPRESSES AT THE SAME TIME.
     * This is to detect keys that do the same thing, i.e W and UP for navigating
     * a menu. 
     * 
     * if(THIS KEY OR THIS KEY) 
     * 
     * @param keycode keys wanting to check
     * @return 
     */
    public boolean isKeyJustPressed(int... keycode)
    {
        for (int code : keycode)
        {
            if (Gdx.input.isKeyJustPressed(code))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * 
     * THIS METHOD IS NOT FOR DETECTING MULTIPLE KEYPRESSES AT THE SAME TIME.
     * This is to detect keys that do the same thing, i.e W and UP for navigating
     * a menu. 
     * 
     * if(THIS KEY OR THIS KEY) 
     * 
     * @param keycode keys wanting to check
     * @return 
     */
    public boolean isKeyPressed(int... keycode)
    {
        for (int code : keycode)
        {
            if (Gdx.input.isKeyPressed(code))
            {
                return true;
            }
        }
        return false;
    }

    public void switchScreen(String curr, String nextScreen)
    {
        Gdx.app.log("SCREEN SWITCH", curr + " : " + nextScreen);
        
        currentScreen.exit();
        currentScreen.destroy();
        
        currentScreen = screens.get(nextScreen);
        
        currentScreen.load();
        currentScreen.enter();
    }
    
    public Vector2 getMouse()
    {
        Vector2 result = new Vector2();

        result.x = Gdx.input.getX();
        result.y = Main.HEIGHT - Gdx.input.getY();
        
        return result;
    }
    
    public void exit()
    {
        currentScreen.destroy();
        Gdx.app.exit();
    }

    public GameController getNextController()
    {
        GameController result = new GameController(this);
        
        return result;
    }
    
    public void resize(int width, int height)
    {
        viewport.update(width, height);
        viewport.apply();
    }

    public Vector2 getMouseCoords()
    {
        return new Vector2(Gdx.input.getX(), Gdx.input.getY());
    }
    
    public OrthographicCamera getCamera()
    {
        return camera;
    }
}

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
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.viewport.FitViewport;
import screens.OptionsScreen;


/**
 *
 * @author wrksttnpc
 */
public class GameEngine
{

    Screen currentScreen;

    FitViewport viewport;
    OrthographicCamera camera;
    
    FitViewport hudViewport;
    OrthographicCamera hudCamera;

    SpriteBatch hudBatch;
    
    SpriteBatch s;
    ShapeRenderer sh;

    BitmapFont fontDefault;
    
    ArrayMap<String, Screen> screens;
  
    int sleepTime = 0;
    boolean sleep = false;
    
    int shake = 0;
    boolean shakeDir = false;
    
    public GameEngine()
    {

        camera = new OrthographicCamera();
        hudCamera = new OrthographicCamera();
        
        viewport = new FitViewport(Main.WIDTH, Main.HEIGHT, camera);
        viewport.apply();

        camera.position.x = Main.WIDTH / 2;
        camera.position.y = Main.HEIGHT / 2;

        hudViewport = new FitViewport(Main.WIDTH, Main.HEIGHT, hudCamera);
        hudViewport.apply();
        
        
        hudCamera.position.x = Main.WIDTH / 2;
        hudCamera.position.y = Main.HEIGHT / 2;
        
        s = new SpriteBatch();
        s.enableBlending();
        
        hudBatch = new SpriteBatch();
        hudBatch.enableBlending();
        
        sh = new ShapeRenderer();
        
        
        Fonts.load();
       

        screens = new ArrayMap<String, Screen>();
        
        screens.put(Screen.MAIN_MENU, new MainMenuScreen(this, s, hudBatch, sh));
        screens.put(Screen.HOW_TO_PLAY, new HowToPlayScreen(this, s, hudBatch, sh));
        screens.put(Screen.TEST_GAME, new TestGameScreen(this, s, hudBatch, sh));
        screens.put(Screen.OPTIONS, new OptionsScreen(this, s, hudBatch, sh));
        
        currentScreen = screens.get(Screen.MAIN_MENU);
        currentScreen.load();
        camera.zoom = 0.5f;
    }

    /**
     * do engine things. updating camera, polling input. where calculations
     * should be done.
     */
    public void updateEngine()
    {
        if(!sleep)
        {
            hudCamera.update();
            camera.update();
            if(shake > 0)
            {
                shake--;
                shakeDir = !shakeDir;
            }
        }
        
       
    }

    /**
     * Like engine, but for when entities interact. What I want to do, is that
     * any time I write the same thing in 2 different scenes, it must be
     * re-factored into updateEngine()
     * @param deltaTime 
     */
    public void updateGame(float deltaTime)
    {
        if(!sleep)
        {
            if (currentScreen != null)
            {
                currentScreen.update(deltaTime);
            }
        }
        else
        {
            sleepTime--;
        }
        if(sleepTime <= 0)
        {
            sleep = false;
            sleepTime = 0;
        }
        if(isKeyPressed(Keys.NUM_1))
        {
            camera.zoom += 0.05f;
        }
        if(isKeyPressed(Keys.NUM_2))
        {
            camera.zoom -= 0.05f;
        }
    }

    /**
     * Render.
     */
    public void draw()
    {
        s.setProjectionMatrix(camera.combined);
        hudBatch.setProjectionMatrix(hudCamera.combined);
        sh.setProjectionMatrix(camera.combined);
        if (currentScreen != null)
        {
            currentScreen.draw();
        }
    }
    
    /**
     * CHECK IF MULTIPLE KEYS ARE PRESSED AT THE SAME TIME.
     * 
     * @param keycode keys wanting to check
     * @return 
     */
    public boolean areKeysPressed(int... keycode)
    {
        for(int code : keycode)
        {
            if(!Gdx.input.isKeyPressed(code))
            {
                return false;
            }
        }
        return true;
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
    
    public boolean isMouseButtonPressed(int... buttoncode)
    {
        boolean result = false;
        for(int code : buttoncode)
        {
            if(Gdx.input.isButtonPressed(code))
            {
                result = true;
            }
            
        }
        return result;
    }

    /**
     * used to change game states, moving from screen to screen.
     * @param curr
     * @param nextScreen 
     */
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

    /**
     * CURRENTLY GETS ONLY THE FIRST CONTROLLER.
     * 
     * TODO: multipleControllers?
     * @return 
     */
    public GameController getNextController()
    {
        GameController result = new GameController(this);
        
        return result;
    }
    
    public void resize(int width, int height)
    {
        viewport.update(width, height);
        viewport.apply();
        hudViewport.update(width, height);
        hudViewport.apply();
    }
    
    /**
     * USED TO PAUSE GAME FOR GIVEN AMOUNT OF FRAMES.
     * gives that "umph" effect
     * @param time 
     */
    public void sleep(int time)
    {
        sleep = true;
        sleepTime = time;
    }
    
    public void shake(int intensity)
    {
        shake += intensity;
    }
    
    public void doCameraShake()
    {
        if(shake > 0)
        {
            camera.position.x += shakeDir ? -shake : shake;
            camera.position.y += shakeDir ? shake : -shake;
        }
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gold.daniel.main;

import screens.HowToPlayScreen;
import screens.MainMenuScreen;
import screens.GameScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.viewport.FitViewport;
import gameobjects.Tile;
import gameobjects.World;
import screens.OptionsScreen;
import screens.PathFindingTestScreen;

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
                
        Textures.loadMaps();
        
        screens = new ArrayMap<String, Screen>();
        
        /*adds all the screens so we can switch between them
        //NOTE: This does not load all the objects in the scene! 
        //this just allows us to reference them, initializing should
        //be done as needed
        */
        screens.put(Screen.MAIN_MENU, new MainMenuScreen(this, s, hudBatch, sh));
        screens.put(Screen.HOW_TO_PLAY, new HowToPlayScreen(this, s, hudBatch, sh));
        screens.put(Screen.GAME, new GameScreen(this, s, hudBatch, sh));
        screens.put(Screen.OPTIONS, new OptionsScreen(this, s, hudBatch, sh));
        screens.put(Screen.PATHFINDING, new PathFindingTestScreen(this, s, hudBatch, sh));
        
        currentScreen = screens.get(Screen.MAIN_MENU);
        currentScreen.load();
    }

    /**
     * do engine things. updating camera. where engine calculations
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
                MathUtils.clamp(shake, 0, 20);
                shake--;
                shakeDir = !shakeDir;
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
        Vector3 values = new Vector3(getMouseCoords(), 0);
        values = viewport.unproject(values);
        return new Vector2(values.x, values.y);
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
    
    /**
     * Makes the camera jitter around at the current intensity.
     * This really starts to make the game feel alive
     */
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
    
     /**
     * 
     * probably should use the TiledMap properties, but I like rolling
     * my own structure for tiles for simplicity
     * @param map
     * @param world
     * @return 
     */
    public Tile[][] createTiles(TiledMap map, World world)
    {
        Tile[][] result = new Tile[map.getProperties().get("width", Integer.class)]
                        [map.getProperties().get("height", Integer.class)];
        TiledMapTileLayer layer = (TiledMapTileLayer)map.getLayers().get("collision");
        for (int i = 0; i < result.length; i++)
        {
            for (int j = 0; j < result[i].length; j++)
            {
                int tileX = (int)(i * layer.getTileWidth());
                int tileY = (int)(j * layer.getTileHeight());
                
                boolean isSolid = false;
                if(layer.getCell(i, j) != null)
                {
                    isSolid = layer.getCell(i, j).getTile().getProperties().get("isSolid") != null;
                }
                result[i][j] = new Tile(s, sh, tileX, tileY, isSolid);
                world.addEntity(result[i][j]);
            }
        }
        return result;
    }
}

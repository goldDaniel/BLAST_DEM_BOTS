/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import gameobjects.World;
import gameobjects.Player;
import gameobjects.Robot;
import gold.daniel.main.GameEngine;
import gold.daniel.main.Main;
import gold.daniel.main.Screen;
import gold.daniel.main.Textures;

/**
 *
 * @author wrksttnpc
 */
public class TestGameScreen extends Screen
{

    Player player;
    World world;
    OrthogonalTiledMapRenderer tmr;
    
    SpriteBatch hudBatch;
    
    Sound backgroundSong;
    
    boolean updating;
    
  
    
    public TestGameScreen(GameEngine engine, SpriteBatch s, ShapeRenderer sh)
    {
        super(engine, s, sh);
    }

    @Override
    public void load()
    {
        updating = true;
        TiledMap temp = new TmxMapLoader().load("maps/test.tmx");
        tmr = new OrthogonalTiledMapRenderer(temp);
        world = new World(temp, tmr, s, sh);
        player = new Player(s, sh, engine.getNextController());
        world.addEntity(player);
        
        Robot robot = new Robot(s, sh, new Texture(Gdx.files.internal("characters/robot/example.png")));
        world.addEntity(robot);
        
        backgroundSong = Gdx.audio.newSound(Gdx.files.internal("audio/game_background.wav"));
        
        hudBatch = new SpriteBatch();
        updating = false;
    }

    
    @Override
    public void update(float deltaTime)
    {
        updating = true;
        backgroundSong.resume();
        tmr.setView(engine.getCamera());
        if(engine.isKeyJustPressed(Keys.ESCAPE))
        {
            engine.switchScreen(TEST_GAME, MAIN_MENU);
            backgroundSong.stop();
            updating = !updating;
        }
        world.update(deltaTime);

        
        
        
        engine.getCamera().position.x = (int)(player.getX() + player.getWidth() / 2);
        //CAMERA MAP CLAMPING FOR X-AXIS
        if(engine.getCamera().position.x < world.getX() + Main.WIDTH / 2 * engine.getCamera().zoom)
        {
            engine.getCamera().position.x = world.getX() + Main.WIDTH / 2 * engine.getCamera().zoom;
        }
        else if(engine.getCamera().position.x > world.getX() + world.getWidth() - Main.WIDTH / 2 * engine.getCamera().zoom)
        {
            engine.getCamera().position.x = world.getX() + world.getWidth() - Main.WIDTH / 2 * engine.getCamera().zoom;
        }
        ////////////////////////////////////////
        
        //CAMERA MAP CLAMPING FOR Y-AXIS
        engine.getCamera().position.y = (int)(player.getY() + player.getHeight() / 2);
        if(engine.getCamera().position.y < world.getY() + Main.HEIGHT / 2 * engine.getCamera().zoom)
        {
            engine.getCamera().position.y = world.getY() + Main.HEIGHT / 2 * engine.getCamera().zoom;
        }
        else if(engine.getCamera().position.y > world.getY() + world.getHeight() - Main.HEIGHT / 2 * engine.getCamera().zoom)
        {
            engine.getCamera().position.y = world.getY() + world.getHeight() - Main.HEIGHT / 2 * engine.getCamera().zoom;
        }
        ///////////////////////////////////////////
        
        engine.getCamera().zoom = 0.5f;
        updating = false;
        
        

    }

    @Override
    public void draw()
    {
        world.draw();

        hudBatch.begin();
        ////DRAWING AMMO COUNTER
        for(int i = 0; i < player.getWeapon().getCurrentAmmo(); i++)
        {
            hudBatch.draw(Textures.ammoTexture, Textures.ammoTexture.getWidth() * i * 2 + 1*i, 0, Textures.ammoTexture.getWidth() * 2, Textures.ammoTexture.getHeight() * 2);
        }
        for(int i = player.getWeapon().getCurrentAmmo(); i < player.getWeapon().getMaxAmmo(); i++)
        {
            hudBatch.draw(Textures.noAmmoTexture, Textures.noAmmoTexture.getWidth() * i * 2 + 1*i, 0, Textures.noAmmoTexture.getWidth() * 2, Textures.noAmmoTexture.getHeight() * 2);
        }
        /////////////////////////
        hudBatch.end();
    }

    @Override
    public void destroy()
    {
        world.dispose();
        backgroundSong.dispose();
    }

    @Override
    public void enter()
    {
        updating = false;
        backgroundSong.play(0.1f);
    }
    @Override
    public void exit()
    {
        backgroundSong.stop();
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package screens;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import gameobjects.World;
import gameobjects.Player;
import gameobjects.Robot;
import gameobjects.Tank;
import gold.daniel.main.GameEngine;
import gold.daniel.main.Main;
import gold.daniel.main.Screen;
import gold.daniel.main.Sounds;
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
    
    Sound backgroundSong;
    
    
    boolean updating;
    
    public TestGameScreen(GameEngine engine, SpriteBatch s, SpriteBatch hudBatch, ShapeRenderer sh)
    {
        super(engine, s, hudBatch, sh);
    }

    @Override
    public void load()
    {
        updating = true;
        TiledMap temp = new TmxMapLoader().load("maps/test-large.tmx");
        tmr = new OrthogonalTiledMapRenderer(temp);
        world = new World(temp, tmr, engine, s, sh);
        player = new Player(s, sh, engine.getNextController());
        world.addEntity(player);

        int robotCount = 1;
        for (int i = 0; i < robotCount; i++)
        {
            world.addEntity(new Robot(100, 100, s, sh, Textures.ROBOT));
        }
        world.addEntity(new Tank(10, 100, s, sh));
        
        
        backgroundSong = Sounds.GAME_BACKGROUND;
        
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
        
        engine.getCamera().position.y = (int)(player.getY() + player.getHeight() / 2);
        //CAMERA MAP CLAMPING FOR Y-AXIS
        if(engine.getCamera().position.y < world.getY() + Main.HEIGHT / 2 * engine.getCamera().zoom)
        {
            engine.getCamera().position.y = world.getY() + Main.HEIGHT / 2 * engine.getCamera().zoom;
        }
        else if(engine.getCamera().position.y > world.getY() + world.getHeight() - Main.HEIGHT / 2 * engine.getCamera().zoom)
        {
            engine.getCamera().position.y = world.getY() + world.getHeight() - Main.HEIGHT / 2 * engine.getCamera().zoom;
        }
        ///////////////////////////////////////////
        
        engine.doCameraShake();
        
        //engine.getCamera().zoom = 0.5f;
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
            hudBatch.draw(Textures.AMMO_TEXTURE, Textures.AMMO_TEXTURE.getWidth() * i * 2 + 1*i, 0, Textures.AMMO_TEXTURE.getWidth() * 2, Textures.AMMO_TEXTURE.getHeight() * 2);
        }
        for(int i = player.getWeapon().getCurrentAmmo(); i < player.getWeapon().getMaxAmmo(); i++)
        {
            hudBatch.draw(Textures.AMMO_EMPTY_TEXTURE, Textures.AMMO_EMPTY_TEXTURE.getWidth() * i * 2 + 1*i, 0, Textures.AMMO_EMPTY_TEXTURE.getWidth() * 2, Textures.AMMO_EMPTY_TEXTURE.getHeight() * 2);
        }
        
        hudBatch.draw(Textures.HEALTHBAR_END, 
                0, 
                Textures.AMMO_EMPTY_TEXTURE.getHeight() * 2,
                Textures.HEALTHBAR_END.getWidth() * 2,
                Textures.HEALTHBAR_END.getHeight());
        int barLen = 10;
        for (int i = 0; i < barLen; i++)
        {
            hudBatch.draw(Textures.HEALTHBAR_MID, 
                    Textures.HEALTHBAR_END.getWidth() * 2 + i * Textures.HEALTHBAR_RECT.getWidth() * 2,
                    Textures.AMMO_EMPTY_TEXTURE.getHeight() * 2 + 1,
                    Textures.HEALTHBAR_MID.getWidth() * 2,
                    Textures.HEALTHBAR_MID.getHeight());
        }
        for (int i = 0; i < (float)barLen * ((float)player.getHealth() / (float)player.getMaxHealth()); i++)
        {
            hudBatch.draw(Textures.HEALTHBAR_RECT,
                    Textures.HEALTHBAR_END.getWidth() * 2 + i * Textures.HEALTHBAR_RECT.getWidth() * 2,
                    Textures.AMMO_EMPTY_TEXTURE.getHeight() * 2 + 6,
                    Textures.HEALTHBAR_RECT.getWidth() * 2,
                    Textures.HEALTHBAR_RECT.getHeight());
        }
        hudBatch.draw(Textures.HEALTHBAR_END, 
                barLen * Textures.HEALTHBAR_MID.getWidth() * 2 + Textures.HEALTHBAR_END.getWidth() * 2,
                Textures.AMMO_EMPTY_TEXTURE.getHeight() * 2,
                -Textures.HEALTHBAR_END.getWidth() * 2,
                Textures.HEALTHBAR_END.getHeight());
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

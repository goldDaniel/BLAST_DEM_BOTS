/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package screens;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import gameobjects.World;
import gameobjects.Player;
import gold.daniel.main.GameEngine;
import gold.daniel.main.Screen;

/**
 *
 * @author wrksttnpc
 */
public class TestGameScreen extends Screen
{

    Player player;
    World world;
    OrthogonalTiledMapRenderer tmr;
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
        
        updating = false;
    }

    @Override
    public void update(float deltaTime)
    {
        updating = true;
                
        tmr.setView(engine.getCamera());
        if(engine.isKeyJustPressed(Keys.ESCAPE))
        {
            engine.switchScreen(TEST_GAME, MAIN_MENU);
            updating = !updating;
        }
        world.update(deltaTime);

        
        
        engine.getCamera().position.x = (int)(player.getX() + player.getWidth() / 2);
        engine.getCamera().position.y = (int)(player.getY() + player.getHeight() / 2);
        
        engine.getCamera().zoom = 0.5f;
        updating = false;
        
        

    }

    @Override
    public void draw()
    {
        world.draw();
    }

    @Override
    public void destroy()
    {
        world.dispose();
        
    }

    @Override
    public void enter()
    {
        updating = false;
    }
    @Override
    public void exit()
    {

    }
}

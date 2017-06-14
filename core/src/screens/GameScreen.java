/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package screens;

import Utils.PathFinding;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Array;
import gameobjects.World;
import gameobjects.Player;
import gameobjects.Robot;
import gold.daniel.main.GameEngine;
import gold.daniel.main.Main;
import gold.daniel.main.Screen;
import gold.daniel.main.Sounds;
import gold.daniel.main.Textures;

/**
 *
 * @author wrksttnpc
 */
public class GameScreen extends Screen
{

    Player player;
    
    
    World world;
    
    Sound backgroundSong;
    
    float zoom = 0.5f;
    
    boolean updating;
    
    OrthogonalTiledMapRenderer tmr;
    
    float scale = 1f;
    
    public GameScreen(GameEngine engine, SpriteBatch s, SpriteBatch hudBatch, ShapeRenderer sh)
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
       
        backgroundSong = Sounds.GAME_BACKGROUND;
        player = world.getPlayer();
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
            engine.switchScreen(GAME, MAIN_MENU);
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
        
        engine.getCamera().zoom = zoom;
        
        if(engine.isKeyJustPressed(Keys.NUM_1))
        {
            zoom += 0.05f;
        }
        if(engine.isKeyJustPressed(Keys.NUM_2))
        {
            zoom -= 0.05f;
        }
        
        if(engine.isKeyJustPressed(Keys.NUM_3))
        {
            zoom = 0.5f;
        }
        updating = false;
    }

    @Override
    public void draw()
    {
        world.draw();

        Array<Robot> robots = world.getEntityType(Robot.class);
        
        sh.begin(ShapeRenderer.ShapeType.Line);
        Array<Color> colors = new Array<Color>();
        colors.add(Color.RED);
        colors.add(Color.BLUE);
        colors.add(Color.GREEN);
        colors.add(Color.CYAN);
        colors.add(Color.BLACK);
        colors.add(Color.PURPLE);
        colors.add(Color.BROWN);
        
        int i = 0;
        for(Robot robot : robots)
        {
            sh.setColor(colors.get(i));
            PathFinding pf = robot.getPath();
            if(pf != null)
            {
                pf.draw(sh);
            }
            i++;
            if(i > colors.size - 1) i = 0;
        }
        sh.end();
        
        hudBatch.begin();
        
        drawHealthBar();
        
        hudBatch.end();
    }

    @Override
    public void destroy()
    {
       
    }

    @Override
    public void enter()
    {
        updating = false;
        backgroundSong.loop(0.1f);
    }
    @Override
    public void exit()
    {
        backgroundSong.stop();
    }

    private void drawHealthBar()
    {
        hudBatch.draw(Textures.HEALTHBAR_END, 
                0, 
                0,
                Textures.HEALTHBAR_END.getWidth() * 2,
                Textures.HEALTHBAR_END.getHeight());

        //draw the midddle section of the life bar
        for (int i = 0; i < player.getMaxHealth(); i++)
        {
            hudBatch.draw(Textures.HEALTHBAR_MID, 
                    Textures.HEALTHBAR_END.getWidth() * 2 + i * Textures.HEALTHBAR_RECT.getWidth() * 2,
                    1,
                    Textures.HEALTHBAR_MID.getWidth() * 2,
                    Textures.HEALTHBAR_MID.getHeight());
        }
        //draws the red life-rectangles representing health remaining
        for (int i = 0; i < player.getHealth() * ((float)player.getHealth() / (float)player.getMaxHealth()); i++)
        {
            hudBatch.draw(Textures.HEALTHBAR_RECT,
                    Textures.HEALTHBAR_END.getWidth() * 2 + i * Textures.HEALTHBAR_RECT.getWidth() * 2,
                    6,
                    Textures.HEALTHBAR_RECT.getWidth() * 2,
                    Textures.HEALTHBAR_RECT.getHeight());
        }
        //draws the right-hand side healthbar end
        hudBatch.draw(Textures.HEALTHBAR_END, 
                (player.getMaxHealth() * Textures.HEALTHBAR_MID.getWidth() * 2) + Textures.HEALTHBAR_END.getWidth() * 2,
                0,
                -Textures.HEALTHBAR_END.getWidth() * 2,
                Textures.HEALTHBAR_END.getHeight());
    }
}

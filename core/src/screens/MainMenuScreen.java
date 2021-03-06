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
import com.badlogic.gdx.utils.Array;
import gold.daniel.main.*;

/**
 *
 * @author wrksttnpc
 */
public class MainMenuScreen extends Screen
{

    
    
    Array<String> menuOptions = new Array();


    float audioFade = 0;
    Sound menuSound;
    long menuSoundID;
    
    Sound navSound; 
    
    int currentOptionSelection;

    public MainMenuScreen(GameEngine engine, SpriteBatch s, SpriteBatch hudBatch, ShapeRenderer sh)
    {
        super(engine, s, hudBatch, sh);
    }

    @Override
    public void load()
    {
        menuOptions = new Array<String>();
        {
            menuOptions.add("start game");
            menuOptions.add("pathfinding test");
            menuOptions.add("how to play");
            menuOptions.add("exit");
        
        }

        menuSound = Sounds.MENU_SOUND;
        navSound = Sounds.NAV_SOUND;
        menuSoundID = menuSound.play(audioFade);
        currentOptionSelection = menuOptions.size - 1;
    }

    
    
    @Override
    public void update(float deltaTime)
    {
        engine.getCamera().zoom = 1;
        engine.getCamera().position.x = Main.WIDTH / 2;
        engine.getCamera().position.y = Main.HEIGHT / 2;
        if(engine.isKeyJustPressed(Keys.W, Keys.UP))
        {
            currentOptionSelection++;
            navSound.play(0.3f);
        }
        else if(engine.isKeyJustPressed(Keys.S, Keys.DOWN))
        {
            currentOptionSelection--;
            navSound.play(0.3f);
        }
            
        
        
        if(currentOptionSelection > menuOptions.size - 1) currentOptionSelection = 0;
        else if(currentOptionSelection < 0) currentOptionSelection = menuOptions.size - 1;
        
        if(engine.isKeyJustPressed(Keys.SPACE, Keys.ENTER))
        {
            switch (currentOptionSelection)
            {
                case 3:
                    engine.switchScreen(MAIN_MENU, GAME);
                    break;
                case 2:
                    engine.switchScreen(MAIN_MENU, PATHFINDING);
                    break;
                case 1:
                    engine.switchScreen(MAIN_MENU, HOW_TO_PLAY);
                    break;
                case 0:
                    engine.exit();
                    break;
                default:
                    break;
            }
        }
        
        if(audioFade < 0.6f)
        {
            audioFade += 0.0002;
        }
        menuSound.setVolume(menuSoundID, audioFade);
    }

    @Override
    public void draw()
    {
        hudBatch.begin();
       
        drawString("BLAST DEM", 0, 500, 2.2f);
        drawString("BOTS", 235, 430, 2.2f);
        for(int i = 0; i < menuOptions.size; i++)
        {
            int y = 100 + 75 * i;
            drawString(menuOptions.get(menuOptions.size - 1 - i), 50, y);
        }
        hudBatch.draw(Textures.ARROW, 650,100 +  75 * currentOptionSelection);

        hudBatch.end();
    }
    
    
    @Override
    public void destroy()
    {
        
    }

    @Override
    public void enter()
    {
        audioFade = 0;
        menuSound.setVolume(menuSoundID, audioFade);
    }

    @Override
    public void exit()
    {
        menuSound.stop();
    }
}

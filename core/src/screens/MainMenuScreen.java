/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package screens;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
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
            menuOptions.add("START GAME");
            menuOptions.add("HOW TO PLAY");
            menuOptions.add("OPTIONS");
            menuOptions.add("EXIT");
        
        }

        menuSound = Sounds.MENU_SOUND;
        navSound = Sounds.NAV_SOUND;
        menuSoundID = menuSound.play(audioFade);
    }

    
    
    @Override
    public void update(float deltaTime)
    {
        engine.getCamera().zoom = 1;
        engine.getCamera().position.x = Main.WIDTH / 2;
        engine.getCamera().position.y = Main.HEIGHT / 2;
        if(engine.isKeyJustPressed(Keys.W, Keys.UP))
        {
            currentOptionSelection--;
            navSound.play(0.3f);
        }
        else if(engine.isKeyJustPressed(Keys.S, Keys.DOWN))
        {
            currentOptionSelection++;
            navSound.play(0.3f);
        }
            
        
        
        if(currentOptionSelection > menuOptions.size - 1) currentOptionSelection = 0;
        else if(currentOptionSelection < 0) currentOptionSelection = menuOptions.size - 1;
        
        if(engine.isKeyJustPressed(Keys.SPACE, Keys.ENTER))
        {
            if(currentOptionSelection == 0)
            {
                engine.switchScreen(MAIN_MENU, TEST_GAME);
            }
            else if(currentOptionSelection == 1)
            {
                engine.switchScreen(MAIN_MENU, HOW_TO_PLAY);
            }
            else if(currentOptionSelection == 2)
            {
                engine.switchScreen(MAIN_MENU, OPTIONS);
            }
            else if(currentOptionSelection == 3)
            {
                engine.exit();
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
        Fonts.TITLE_FONT.setColor(Color.MAGENTA);
        Fonts.TITLE_FONT.draw(hudBatch, "MAIN MENU", Main.WIDTH / 2 - Fonts.TITLE_GLYPH_LAYOUT.width / 2, Main.HEIGHT - Fonts.TITLE_GLYPH_LAYOUT.height / 2);

        for (int i = 0; i < menuOptions.size; i++)
        {
            if(i == currentOptionSelection)
            {
                Fonts.OPTIONS_FONT.setColor(Color.MAROON);
            }
            else
            {
                Fonts.OPTIONS_FONT.setColor(Color.WHITE);
            }
            Fonts.OPTIONS_FONT.draw(hudBatch, menuOptions.get(i), Main.WIDTH / 2 - Fonts.TITLE_GLYPH_LAYOUT.width / 2, Main.WIDTH / 3- (i * 60));
        }

        hudBatch.end();
    }

    @Override
    public void destroy()
    {
        menuSound.dispose();
        navSound.dispose();
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

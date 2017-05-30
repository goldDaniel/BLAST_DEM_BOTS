/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package screens;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
            menuOptions.add("how to play");
            menuOptions.add("exit");
        
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
            if(currentOptionSelection == 2)
            {
                engine.switchScreen(MAIN_MENU, GAME);
            }
            else if(currentOptionSelection == 1)
            {
                engine.switchScreen(MAIN_MENU, HOW_TO_PLAY);
            }
            else if(currentOptionSelection == 0)
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
       
        for(int i = 0; i < menuOptions.size; i++)
        {
            String option = menuOptions.get(menuOptions.size - 1 - i);
            drawString(option, i);
        }
        Texture tex = Textures.ARROW;
        hudBatch.draw(tex, 550, 150 * currentOptionSelection);

        hudBatch.end();
    }

    private void drawString(String string, int i)
    { 
        for(int j = 0; j < string.length(); j++)
        {
            if(Textures.CHARACTERS.containsKey(string.charAt(j)))
            {
                TextureRegion tex = Textures.CHARACTERS.get(string.charAt(j));
                hudBatch.draw(tex, j * 47, 150 * i);
            }
        }
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

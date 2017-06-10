/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package screens;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import gold.daniel.main.GameEngine;
import gold.daniel.main.Screen;

/**
 *
 * @author wrksttnpc
 */
public class HowToPlayScreen extends Screen
{

    public HowToPlayScreen(GameEngine engine, SpriteBatch s, SpriteBatch hudBatch, ShapeRenderer sh)
    {
        super(engine, s, hudBatch, sh);
    }

    @Override
    public void load()
    {

    }

    @Override
    public void update(float deltaTime)
    {
        if(engine.isKeyJustPressed(Keys.ESCAPE)) engine.switchScreen(HOW_TO_PLAY, MAIN_MENU);
    }

    @Override
    public void draw()
    {
        hudBatch.begin();
        drawString("W A S D", 0, 500, 2);
        drawString("TO MOVE AROUND", 0, 450);
        
        drawString("MOUSE", 0, 325, 2);
        drawString("to aim", 0, 275);
        
        drawString("LEFT CLICK", 0, 150, 2);
        drawString("to shoot", 0, 100);
        
        drawString("press escape to return", 0, 0, 0.94f);
        hudBatch.end();
        
    }

    @Override
    public void destroy()
    {

    }

    @Override
    public void enter()
    {

    }

    @Override
    public void exit()
    {

    }
}

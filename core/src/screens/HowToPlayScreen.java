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
import gold.daniel.main.Main;
import gold.daniel.main.Screen;

/**
 *
 * @author wrksttnpc
 */
public class HowToPlayScreen extends Screen
{

    public HowToPlayScreen(GameEngine engine, SpriteBatch s, ShapeRenderer sh)
    {
        super(engine, s, sh);
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
        sh.begin(ShapeRenderer.ShapeType.Line);
        sh.rect(Main.WIDTH / 2, Main.HEIGHT / 2, 100, 100);
        sh.end();
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

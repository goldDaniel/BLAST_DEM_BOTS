/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameobjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 *
 * @author goldd
 */
public class Robot extends GameObject
{

    
    Texture texture;
    
    public Robot(SpriteBatch s, ShapeRenderer sh, Texture texture)
    {
        super(s, sh);
        this.texture = texture;
    }

    @Override
    public void update(World world, float deltatime)
    {
        super.update(world, deltatime);
    }

    @Override
    public void draw()
    {

    }

    @Override
    public void dispose()
    {

    }
}

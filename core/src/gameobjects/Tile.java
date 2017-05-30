/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameobjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 *
 * @author wrksttnpc
 */
public class Tile extends Entity 
{
    boolean isSolid;
    
    public static final int SIZE = 16;

    public Tile(SpriteBatch s, ShapeRenderer sh, int x, int y, boolean isSolid)
    {
        super(s, sh);
        this.x = x;
        this.y = y;
        this.width = SIZE;
        this.height = SIZE;
        this.isSolid = isSolid;
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

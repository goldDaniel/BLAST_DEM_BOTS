/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameobjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;

/**
 *
 * @author wrksttnpc
 */
public class Bullet extends GameObject
{
    float angle;
    float speed;

    public Bullet(SpriteBatch s, ShapeRenderer sh, float x, float y, float angle, float speed, Texture texture)
    {
        super(s, sh);
        this.speed = speed;
        this.x = x;
        this.y = y;
        this.angle = angle;
        width = height = 8;
    }

    @Override
    public void update(World world, float deltaTime)
    {
        x += speed * MathUtils.cosDeg(angle) * deltaTime;
        y += speed * MathUtils.sinDeg(angle) * deltaTime;
        
        collisionTiles.addAll(world.getCollisionTiles((int)(x + width / 2), (int)(y + height / 2)));
        
        for(Tile tile : collisionTiles)
        {
            handleTileCollisionResponse(tile);
        }
        
        if(!isAlive)
        {
            world.removeEntity(this);
        }
    }
    
    @Override
    public void draw()
    {
        sh.begin(ShapeRenderer.ShapeType.Filled);
        sh.circle(x + width / 2, y + height / 2, width);
        sh.end();
    }
    
    @Override
    protected void handleTileCollisionResponse(Tile tile)
    {
        if(isColliding(tile))
        {
            isAlive = false;
        }
    }

    @Override
    public void dispose()
    {
    }
}

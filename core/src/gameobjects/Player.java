/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameobjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import gold.daniel.main.GameController;
import gold.daniel.main.Textures;

/**
 * 
 *
 * @author wrksttnpc
 */
public class Player extends GameObject
{

    //used to see number of iterations when testing 
    //with outputs that are repetitive(so you can see when the output starts and stops)aa
    private float DEBUG_COUNTER = 0;
        
    GameController controller;

    float speed = 60f;

    float angle = 0;

    int health = 10;
    int healthMax = 10;

    Texture texture;

    //used to hold player surrounding tiles, should be at most 5 (4 directions + current)
    /*
        010
        111
        010
    */
    Array<Tile> collisionTiles;
            
            
    public Player(SpriteBatch s, ShapeRenderer sh, GameController controller)
    {
        super(s, sh);
        this.controller = controller;
        x = 100;
        y = 100;

        
        collisionTiles = new Array<Tile>();
        texture = Textures.player;
        width = height = texture.getWidth() * 0.60f;
    }

    @Override
    public void update(World world, float deltaTime)
    {
        super.update(world, deltaTime);
        if (!isAlive()) return;
        
        Vector2 movement = controller.getMoveDirection();
        
        
        collisionTiles.clear();
        collisionTiles.addAll(world.getCollisionTiles((int)(x + width / 2), (int)(y + height / 2)));
        
        x += speed * movement.x * deltaTime;
        for(Tile tile : collisionTiles)
        {
            if(isColliding(tile))
            {
                handleCollisionResponse(tile);
            }
        }
        y += speed * movement.y * deltaTime;
        for(Tile tile : collisionTiles)
        {
            if(isColliding(tile))
            {
                handleCollisionResponse(tile);
            }
        }
        
        
        angle = calculateAngleToMouse(x, y);
    }

    private void handleCollisionResponse(Tile tile)
    {
        if(isColliding(tile))
        {
            boolean left = x < tile.x;
            boolean above = y > tile.y;
            
            float horizontalDif;
            float verticalDif;
            
            if(left)
            {
                horizontalDif = x + width - tile.x;
            }
            else
            {
                horizontalDif = tile.x + tile.width- x;
            }
            
            if(above)
            {
                verticalDif = tile.y + tile.height - y;
            }
            else
            {
                verticalDif = y + height - tile.y;
            }
            
            //DO HORIZONTALdd
            if(horizontalDif < verticalDif)
            {
                if(left)
                {
                    x = tile.x - width;
                }
                else
                {
                    x = tile.x + tile.width;
                }
            }
            //DO VERTICAL
            else
            {
                if(above)
                {
                    y = tile.y + tile.height;
                }
                else
                {
                    y = tile.y - height;
                }
            }
        }
    }
    
    public void damage(int amount)
    {
        health -= amount;
        if (health < 0)
        {
            health = 0;
        }
    }

    public boolean isAlive()
    {
        return health > 0;
    }

    @Override
    public void draw()
    {
        s.begin();
        s.draw(new TextureRegion(texture), x, y, width / 2, height / 2, width, height, 1, 1, angle);
        s.end();

        sh.begin(ShapeRenderer.ShapeType.Line);
        //draw line to mouse
        sh.setColor(Color.RED);
        sh.line(x + width / 2, y + height / 2, controller.getMousePosition().x, controller.getMousePosition().y);
        ///////
        sh.rect(x, y, width, height);
        sh.end();
    }

    private float calculateAngleToMouse(float x, float y)
    {
        float result = 0;
        Vector2 temp = new Vector2(x + width / 2, y + height / 2);
        temp.sub(controller.getMousePosition());

        result = -90 + MathUtils.atan2(temp.y, temp.x) / MathUtils.degreesToRadians;
        
        return result;
    }

    @Override
    public void dispose()
    {

    }
}

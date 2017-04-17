/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameobjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

/**
 *
 * @author goldd
 */
public class Robot extends GameObject
{

    float angle;
    
    float speed;
    
    Texture texture;
    
    public Robot(SpriteBatch s, ShapeRenderer sh, Texture texture)
    {
        super(s, sh);
        this.x = 100;
        this.y = 100;
        this.width = 32;
        this.height = 32;
        this.texture = texture;
        
        speed = 20f;
        angle = 0;
    }

    @Override
    public void update(World world, float deltaTime)
    {
        super.update(world, deltaTime);
        
        for(int i = 0; i < world.entities.size; i++)
        {
            if(world.entities.get(i) instanceof Player)
            {
                float playerX = world.entities.get(i).x;
                float playerY = world.entities.get(i).y;
                
                System.out.println(playerX + playerY);
                angle = calculateAnglePoint(playerX, playerY);
            }
        }
        
        x += speed * MathUtils.cosDeg(angle) * deltaTime;
        y += speed * MathUtils.sinDeg(angle) * deltaTime;
        
    }

    @Override
    public void draw()
    {
        s.begin();
        s.draw(new TextureRegion(texture), x, y, width / 2, height / 2, width, height, 1, 1, angle);
        s.end();
    }

    @Override
    public void dispose()
    {

    }
    
    private float calculateAnglePoint(float x, float y)
    {
        Vector2 temp = new Vector2(this.x + width / 2, this.y + height / 2);
        temp.sub(x, y);

        return 180 + temp.angle();
    }
}

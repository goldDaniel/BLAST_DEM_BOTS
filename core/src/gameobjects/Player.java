/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import gold.daniel.main.GameController;
import gold.daniel.main.Textures;
import weapons.Weapon;

/**
 * 
 *
 * @author wrksttnpc
 */
public class Player extends GameObject
{

        
    GameController controller;

    float speed = 75f;

    float angle = 0;

    int health = 10;
    int healthMax = 10;

    Texture texture;

    Weapon weapon;
 
            
    public Player(SpriteBatch s, ShapeRenderer sh, GameController controller)
    {
        super(s, sh);
        this.controller = controller;
        x = 400;
        y = 400;

        weapon = new Weapon(8, 8, 10, 25f, 250f, new Texture(Gdx.files.internal("weapons/bullets/12.png")));
        
        
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
        
        collisionTiles.addAll(world.getCollisionTiles((int)(x + width / 2), (int)(y + height / 2)));
        
        x += speed * movement.x * deltaTime;
        for(Tile tile : collisionTiles)
        {
            handleTileCollisionResponse(tile);
        }
        y += speed * movement.y * deltaTime;
        for(Tile tile : collisionTiles)
        {
            handleTileCollisionResponse(tile);
        }
        
        
        angle = calculateAngleToMouse(x, y);

        weapon.update();
        handleWeapons(world);
    }
    
    public int getHealth()
    {
        return health;
    }
        
    public int getMaxHealth()
    {
        return healthMax;
    }

    public Weapon getWeapon()
    {
        return weapon;
    }
       
    private void handleWeapons(World world)
    {
        if(controller.isFireButtonPressed())
        {
            world.addEntity(weapon.fireBullet(s, sh, x + width / 2, y + height / 2, angle));
        }
        if(controller.isReloadPressed())
        {
            if(!weapon.hasAmmo())
            {
                weapon.reload();
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
         sh.end();
    }

    private float calculateAngleToMouse(float x, float y)
    {
        Vector2 temp = new Vector2(x + width / 2, y + height / 2);
        temp.sub(controller.getMousePosition());

        //-90 to compensate for image rotation 
        return -90 + temp.angle();
    }

    @Override
    public void dispose()
    {

    }
}

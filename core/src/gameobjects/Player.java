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
public class Player extends Character
{

        
    //Please try to keep all input related things here
    GameController controller;
   
    Texture texture;

    Weapon weapon;
            
    /**
     * 
     * @param s
     * @param sh
     * @param controller 
     */
    public Player(SpriteBatch s, ShapeRenderer sh, GameController controller)
    {
        super(s, sh);
        this.controller = controller;
        x = 400;
        y = 400;

        health = 1;
        
        weapon = new Weapon(8, 8, 10, 25f, 500f, Textures.BULLET_12);
        
        
        collisionTiles = new Array<Tile>();
        texture = Textures.PLAYER;
        width = height = ((texture.getWidth() + texture.getHeight()) / 2) * 0.60f;
    }

    /**
     * 
     * @param world
     * @param deltaTime 
     */
    @Override
    public void update(World world, float deltaTime)
    {
        super.update(world, deltaTime);
        if (!isAlive) return;
        
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
        
        
        angle = -90 + calculateAnglePoint(controller.getMousePosition().x, controller.getMousePosition().y);

        weapon.update();
        handleWeapons(world);
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


    @Override
    public void dispose()
    {

    }
}

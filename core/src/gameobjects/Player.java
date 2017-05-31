/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameobjects;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import gold.daniel.main.GameController;
import gold.daniel.main.Sounds;
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
    
    Sound hitSound = Sounds.HIT;
    
    final int HURT_FRAMES = 60;
    int frameCount = 0;
        
         
    
    public Player(float x, float y, SpriteBatch s, ShapeRenderer sh, GameController c)
    {
        this(s, sh, c);
        this.x = x;
        this.y = y;
    }
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

        health = healthMax = 10;
        
        Array<Class> canCollide = new Array<Class>();
        canCollide.add(Robot.class);
        canCollide.add(Tank.class);
        weapon = new Weapon(8, 8, 25f, 500f, 1, Textures.BULLET_8, canCollide);
        
        
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
        if (!isAlive)
        {
            float particleCount = 200;
            for (int i = 0; i < particleCount; i++)
            {
                float rand = MathUtils.random(-100, 100);
                world.addEntity(new Particle(x, y, 4, 4, 
                        200 + (int)rand, 300f + rand, 
                        ((float)i / (float)particleCount) * 360, s, sh));
            }
            
            return;
        }
        
        Vector2 movement = controller.getMoveDirection();
        
        collisionTiles.addAll(world.getCollisionTiles(this));
        
        /**
         * SEPERATE AXIS THEOREM.
         * move in 1 direction and check for collision
         * move in other direction and check for collision
         * 
         * its beautifully simple
         */
        x += speed * movement.x * deltaTime;
        for(Tile tile : collisionTiles)
        {
            handleMoveCollisionResponse(tile);
        }
        y += speed * movement.y * deltaTime;
        for(Tile tile : collisionTiles)
        {
            handleMoveCollisionResponse(tile);
        }
        
        if(frameCount > 0)
        {
            frameCount--;
        }
        
        angle = -90 + calculateAngleToPoint(controller.getMousePosition().x, controller.getMousePosition().y);

        weapon.update();
        handleWeapons(world);
    }   
    

    public Weapon getWeapon()
    {
        return weapon;
    }
    
    @Override
    public void damage(int damage)
    {
        if(frameCount <= 0)
        {
            super.damage(damage);
            frameCount = HURT_FRAMES;
            hitSound.play();
        }
        
    }
       
    private void handleWeapons(World world)
    {
        if(controller.isFireButtonPressed())
        {
            world.addEntity(weapon.fireBullet(s, sh, x + width / 2, y + height / 2, angle));
        }
        if(controller.isReloadPressed())
        {
            
        }
    }

    @Override
    public void draw()
    {
        Color temp = Color.WHITE.cpy();
        temp.a = 1f - (float)frameCount / (float)HURT_FRAMES;
        s.setColor(temp);
        s.begin();
        s.draw(new TextureRegion(texture), x, y, width / 2, height / 2, width, height, 1, 1, angle);
        s.end();
        s.setColor(Color.WHITE);

        sh.begin(ShapeRenderer.ShapeType.Line);
        //draw line to mouse
        sh.setColor(Color.RED);
        sh.line(x + width / 2, y + height / 2, controller.getMousePosition().x, controller.getMousePosition().y);
        ///////
         sh.end();
    }

    /**
     *
     * @param world
     * @param x
     * @param y
     * @param angle
     */
    @Override
    public void spawnParticles(World world, float x, float y, float angle)
    {   boolean toggle = false;
        for (int i = 0; i < 5; i++)
        {
            toggle = !toggle;
            float rand = MathUtils.random(40);
            float span = rand;
            if(toggle) rand = -rand;
            
            world.addEntity(new Particle(x, y, 4, 4, (int)span, 150 + rand, angle + rand, s, sh));
        }
    }

    @Override
    public void dispose()
    {

    }

    @Override
    public int getHitShake()
    {
        return 5;
    }

    @Override
    public int getDeathShake()
    {
        return 25;
    }
}

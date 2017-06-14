/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameobjects;

import Utils.PathFinding;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import gold.daniel.main.Sounds;
import gold.daniel.main.Textures;

/**
 *
 * @author goldd
 */
public class Robot extends Character
{

    static TextureRegion weapon = new TextureRegion(Textures.ROBOT_WEAPON);
    static TextureRegion body = new TextureRegion(Textures.ROBOT_BODY);
    static TextureRegion head = new TextureRegion(Textures.ROBOT_HEAD);
    
    static Sound explosion = Sounds.EXPLOSION;
    
    float headWidth;
    float headHeight;
    float headX;
    float headY;
    float headAngle = 0;
    
    
    float scale = 0.75f;
    
    PathFinding pf;
    Array<Vector2> path = null;
    
    Vector2 destination = null;
    int pathUpdateCount = 30;

    public Robot(float x, float y, SpriteBatch s, ShapeRenderer sh)
    {
        this(s, sh);
        this.x = x;
        this.y = y;
    }

    /**
     *
     * @param s
     * @param sh
     */
    public Robot(SpriteBatch s, ShapeRenderer sh)
    {
        super(s, sh);
        this.x = 100;
        this.y = 100;
        this.width = 32 * scale;
        this.height = 32 * scale;
        
        headWidth = headHeight = 16 * scale;
        
        
        speed = 20f;
        angle = 0;

        healthMax = health = 5;
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

        Player player = (Player) world.getPlayer();

        if(player != null)    
        {
            if(pf == null)
            {
                pf = new PathFinding(world.map, this, player);
            }
            if(pathUpdateCount <= 0)
            {
                pf.update(player);
                path = pf.calculate();
                if(path != null)
                {
                    destination = path.first();
                }
            }
            
            headAngle = 270 + calculateAngleToPoint(player.x, player.y);
            if(path != null)
            {
                angle = MathUtils.lerpAngleDeg(angle, 180 + calculateAngleToPoint(path.first().x, path.first().y), 0.05f);
            }
        }
        else
        {
            headAngle = angle;
        }

        collisionTiles = world.getCollisionTiles(this);

        x += speed * MathUtils.cosDeg(angle) * deltaTime;
        for (Tile tile : collisionTiles)
        {
            handleMoveCollisionResponse(tile);
        }
        y += speed * MathUtils.sinDeg(angle) * deltaTime;
        for (Tile tile : collisionTiles)
        {
            handleMoveCollisionResponse(tile);
        }
        
        headX = x + width / 2 - headWidth / 2;
        headY = y + height / 2 - headHeight / 2;
        
        collisionTiles.clear();

        if (!isAlive)
        {
            explosion.play();
            for (int i = 0; i < 100; i++)
            {
                world.addEntity(new Particle(x + width / 2, y + height / 2, 4, 4,
                        MathUtils.random(25), MathUtils.random(600), MathUtils.random(360), s, sh));
            }
        }

    }

    /**
     *
     */
    @Override
    public void draw()
    {
        s.draw(body, x, y, width / 2, height / 2, width, height, 1, 1, angle);
        s.draw(head, headX, headY, headWidth / 2, headHeight / 2, 
                headWidth, headHeight, 1, 1, headAngle);
    }

    @Override
    public void dispose()
    {

    }

    
    @Override
    public void spawnParticles(World world, float x, float y, float angle)
    {
        for (int j = 0; j < 10; j++)
        {
            world.addEntity(new Particle(x, y, 3, 3, 
                10 + MathUtils.random(10), 50f + MathUtils.random(200), 
                180 + angle + MathUtils.random(-35, 35), 
                s, sh));
        }
    }

    @Override
    public int getHitShake()
    {
        return 3;
    }

    @Override
    public int getDeathShake()
    {
        return 7;
    }
}

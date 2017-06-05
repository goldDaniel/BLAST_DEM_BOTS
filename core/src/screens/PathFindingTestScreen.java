/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package screens;

import Utils.PathFinding;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import gameobjects.Tile;
import gameobjects.World;
import gold.daniel.main.GameEngine;
import gold.daniel.main.Screen;

/**
 * THIS IS TO GET A WORKING A* ALGORITHM GOING
 * @author wrksttnpc
 */
public class PathFindingTestScreen extends Screen
{

    OrthogonalTiledMapRenderer tmr;
    TiledMap map;
    Tile[][] tiles;
    World world;
    
    PathFinding pathFinding;
    
    Array<Vector2> steps;
    
    int count = 0;
    
    public PathFindingTestScreen(GameEngine engine, SpriteBatch s, SpriteBatch hudBatch, ShapeRenderer sh)
    {
        super(engine, s, hudBatch, sh);
    }

    @Override
    public void load()
    {
        map = new TmxMapLoader().load("maps/test-pathfinding.tmx");
        tmr = new OrthogonalTiledMapRenderer(map);
        world = new World(map, tmr, engine, s, sh);
        tiles = engine.createTiles(map, world);
        
        pathFinding = new PathFinding(map);
   }

    @Override
    public void enter()
    {
    }

    @Override
    public void exit()
    {
    }

    @Override
    public void update(float deltaTime)
    {
        world.update(deltaTime);
        if(engine.isMouseButtonPressed(Buttons.LEFT))
        {
            pathFinding.update(engine);
            steps = pathFinding.calculate();
            count = 0;
        }
        if(steps != null)
        {
            if(count < steps.size)
            {
                count++;
                engine.sleep(3);
            }
        }
        
    }

    @Override
    public void draw()
    {
        engine.getCamera().zoom = 0.8f;
        engine.getCamera().position.x = world.getWidth() / 2;
        engine.getCamera().position.y = world.getHeight() / 2;
        tmr.setView(engine.getCamera());
        tmr.render();
        world.draw();
        
        sh.begin(ShapeRenderer.ShapeType.Line);
        sh.setColor(Color.GREEN);
        if(steps != null)
        {
            for(int i = 0; i < count - 1; i++)
            {
                sh.line(steps.get(i), steps.get(i + 1));
            }
        }
        sh.end();
    }

    @Override
    public void destroy()
    {
    }
    
}

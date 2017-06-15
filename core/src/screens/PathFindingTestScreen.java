/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package screens;

import Utils.PathFinding;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
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
import gameobjects.Entity;
import gold.daniel.main.GameEngine;
import gold.daniel.main.Screen;

/**
 * THIS IS TO GET A WORKING A* ALGORITHM GOING.
 * probably should NOT be in final game.
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
    
    TestObj mouse;
    
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
        mouse = new TestObj(null, null);
        
        TestObj temp = new TestObj(null, null);
        temp.setPosition(Tile.SIZE * 3, Tile.SIZE * 3);
        
        pathFinding = new PathFinding(map, temp, mouse);
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
        mouse.setPosition(engine.getMouse().x, engine.getMouse().y);
        if(engine.isKeyJustPressed(Keys.ESCAPE))
        {
            engine.switchScreen(PATHFINDING, MAIN_MENU);
        }
        world.update(deltaTime);
        if(engine.isMouseButtonPressed(Buttons.LEFT))
        {
            pathFinding.update(null, mouse);
            pathFinding.calculate();
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
        pathFinding.draw(sh);
        sh.end();
    }

    @Override
    public void destroy()
    {
    }
    
}
/**
 * used to test entity tracking.
 * SHOULD NOT BE USED IN FINAL GAME. only exists so we can instantiate an entity
 * as the class is abstract
 * @author wrksttnpc
 */
class TestObj extends Entity
{

    public TestObj(SpriteBatch s, ShapeRenderer sh)
    {
        super(s, sh);
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

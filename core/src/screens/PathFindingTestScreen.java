/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package screens;

import Utils.PathFinding;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
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
        pathFinding.calculate();
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
    }

    @Override
    public void destroy()
    {
    }
    
}

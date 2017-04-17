/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gold.daniel.main;

import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 *
 * @author wrksttnpc
 */
public class GameController
{
    GameEngine engine;
    
    float angle;
    
    public GameController(GameEngine engine)
    {
        this.engine = engine;
    }
    
    public Vector2 getMousePosition()
    {
        Vector3 values = new Vector3(engine.getMouseCoords(), 0);
        values = engine.viewport.unproject(values);
        return new Vector2(values.x, values.y);
    }
    
    public boolean isFireButtonPressed()
    {
        return engine.isMouseButtonPressed(Buttons.LEFT);
    }
    
    public Vector2 getMoveDirection()
    {
        Vector2 result = new Vector2();

        if(engine.isKeyPressed(Keys.A))
        {
            result.x -= 1;
        }
        if(engine.isKeyPressed(Keys.D))
        {
            result.x += 1;
        }
        if(engine.isKeyPressed(Keys.W))
        {
            result.y += 1;
        }
        if(engine.isKeyPressed(Keys.S))
        {
            result.y -= 1;
        }
        
        return result.nor();
    }
}

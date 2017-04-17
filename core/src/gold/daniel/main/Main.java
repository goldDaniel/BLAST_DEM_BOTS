package gold.daniel.main;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;


public class Main extends ApplicationAdapter
{
    public static final int WIDTH = 1024;
    public static final int HEIGHT  = 768;
    
    GameEngine engine;

    @Override
    public void create()
    {
       engine = new GameEngine();
       
    }

    @Override
    public void render()
    {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
    
        engine.updateEngine();
        engine.updateGame(Gdx.graphics.getDeltaTime());
        
        engine.draw();
    }

    @Override
    public void dispose()
    {

    }
    
    @Override
    public void resize(int width, int height)
    {
        engine.resize(width, height);
    }
}

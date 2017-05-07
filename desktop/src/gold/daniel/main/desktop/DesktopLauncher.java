package gold.daniel.main.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import gold.daniel.main.Main;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
                
                config.width = Main.WIDTH;
                config.height = Main.HEIGHT;
                
                config.resizable = true;
                
		new LwjglApplication(new Main(), config);
	}
}

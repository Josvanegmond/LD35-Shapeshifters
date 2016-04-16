package nl.joozey.shapeshifter.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import nl.joozey.shapeshifter.ShapeshifterMain;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 800;
		config.height = 600;
		config.title = "Shapeshifter";
		new LwjglApplication(new ShapeshifterMain(), config);
	}
}

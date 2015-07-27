package me.nathanp.beetlesredberry.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import me.nathanp.beetlesredberry.BeetlesRedBerry;

public class LevelEditor {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1024;
		config.height = 576;
		new LwjglApplication(new BeetlesRedBerry(true), config);
	}
}

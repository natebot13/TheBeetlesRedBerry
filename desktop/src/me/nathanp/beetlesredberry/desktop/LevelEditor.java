package me.nathanp.beetlesredberry.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import me.nathanp.beetlesredberry.BeetlesRedBerry;

public class LevelEditor {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new BeetlesRedBerry(), config);
	}
}
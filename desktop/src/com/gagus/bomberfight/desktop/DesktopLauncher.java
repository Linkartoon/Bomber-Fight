package com.gagus.bomberfight.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.gagus.bomberfight.BomberFight;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.height = 330;
		config.width = 330*16/9;
		new LwjglApplication(new BomberFight(), config);
	}
}

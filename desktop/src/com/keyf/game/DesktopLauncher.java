package com.keyf.game;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration cfg = new Lwjgl3ApplicationConfiguration();
		cfg.setTitle("MinecraftDrop");
		cfg.setWindowedMode(1920, 1080);
		new Lwjgl3Application(new MinecraftDrop(), cfg);

	}
}

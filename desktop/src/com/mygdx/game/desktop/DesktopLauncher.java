package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.mygdx.game.myGame;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;
import gameCode.Infastructure.World;


public class DesktopLauncher {


	private static final boolean rebuildAtlas = true;



	public static void main (String[] arg) {


		if(rebuildAtlas) {
			Settings settings = new Settings();
			settings.maxWidth = 2048;
			settings.maxHeight = 2048;
			settings.edgePadding = true;
			TexturePacker.process(settings, "/Users/me/Desktop/gdx3/core/assets", "/Users/me/Desktop/gdx3/core/assets", "atlas" );
		}

		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1000;
		config.height = 600;
		config.vSyncEnabled = false; // Setting to false disables vertical sync
		config.foregroundFPS = 0; // Setting to 0 disables foreground fps throttling
		config.backgroundFPS = 0; // Setting to 0 disables background fps throttling
		new LwjglApplication(new myGame(), config);
	}
}

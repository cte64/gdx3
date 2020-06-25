package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.mygdx.game.myGame;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;


public class DesktopLauncher {


	private static final boolean rebuildAtlas = true;



	public static void main (String[] arg) {


		if(rebuildAtlas) {
			Settings settings = new Settings();
			settings.maxWidth = 1024;
			settings.maxHeight = 1024;
			settings.edgePadding = true;
			TexturePacker.process(settings, "/Users/me/Desktop/gdx3/core/assets", "/Users/me/Desktop/gdx3/core/assets", "atlas" );
		}

		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 600;
		config.height = 600;
		new LwjglApplication(new myGame(), config);
	}
}

package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import java.util.HashMap;
import gameCode.Infastructure.World;


public class myGame extends ApplicationAdapter {

	SpriteBatch batch;
	private TextureAtlas atlas;
	private TextureAtlas tileAtlas;

	private static HashMap<String, Sprite> spriteMap;
	private static HashMap<String, Sprite> tileMap;


	private final int xTiles = 20;
	private final int yTiles = 20;
	private final int tileSize = 20;

	float timerCount = 0;
	int fpsCount = 0;

	private void fpsCounter(float time) {
		timerCount += time;
		fpsCount++;
		if(timerCount >= 1.0) {
			Gdx.graphics.setTitle("fps: " + fpsCount);
			timerCount = 0.0f;
			fpsCount = 0;
		}
	}

	private void addSprite(String filename) {
		TextureRegion region = atlas.findRegion(filename);
		Sprite sprite = new Sprite(region);
		Sprite put = spriteMap.put(filename, sprite);
	}

	public void updateTile(int index) {

	}

	@Override
	public void create () {

		//do the sprites ==========================================================================
		spriteMap = new HashMap<String, Sprite>();
		atlas = new TextureAtlas("/Users/me/Desktop/gdx3/core/assets/atlas.atlas");
		batch = new SpriteBatch();

		//do the tiles ============================================================================
		tileMap = new HashMap<String, Sprite>();

		tileAtlas = new TextureAtlas();
		Pixmap image = new Pixmap(20, 20, Pixmap.Format.RGB888);
		Texture texture = new Texture(image);
		TextureRegion region = new TextureRegion(texture);

		for(int y = 0; y < yTiles; y++) {
			for(int x = 0; x < xTiles; x++) {
				int index = (y * xTiles) + x;
				String key = Integer.toString(index);
				tileAtlas.addRegion(key, region);
				tileMap.put(key, new Sprite(region));
			}
		}

		//add the sprites ===============================
		addSprite("tile");
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();



		/*
		for(int y = 0; y < 50; y++) {
			for(int x = 0; x < 50; x++) {
				batch.draw(tileMap.get("1.1"), x*20, y*20);
			}
		}
		 */



		batch.end();

		fpsCounter(Gdx.graphics.getRawDeltaTime());
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		atlas.dispose();
	}
}

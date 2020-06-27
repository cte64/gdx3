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
import java.util.ListIterator;

import gameCode.Infastructure.Entity;
import gameCode.Infastructure.World;
import gameCode.Utilities.MathUtils;


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

		addSprite("tile");

		//DO THIS JUST FOR NOW =========================
		World.init();

		/*
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


		 */
	}

	@Override
	public void render () {


		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();





		//This is where everything happens! ================================================================

		for(Entity ent: World.entList) { ent.updateType("input"); }

		for(Entity ent: World.entList) { batch.draw(spriteMap.get(ent.spriteName), ent.x_pos, ent.y_pos); }
		//This is where everything happens! ================================================================









		batch.end();

		fpsCounter(Gdx.graphics.getRawDeltaTime());


	}
	
	@Override
	public void dispose () {
		//batch.dispose();
		//atlas.dispose();
	}
}

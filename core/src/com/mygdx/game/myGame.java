package com.mygdx.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import gameCode.Infastructure.*;
import gameCode.Infastructure.Graphics;
import gameCode.Utilities.Pixel;
import gameCode.Utilities.StringUtils;

public class myGame extends ApplicationAdapter {

	private float timerCount = 0;
	private int fpsCount = 0;

	private void fpsCounter(float time) {
		timerCount += time;
		fpsCount++;
		if(timerCount >= 1.0) {
			Gdx.graphics.setTitle("fps: " + fpsCount);
			timerCount = 0.0f;
			fpsCount = 0;
		}
	}

	@Override
	public void create () {

		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		//World.setCurrentState("play");

		//Initialize Game Objects
		Graphics.init();

		//Initialize the Input abstraction class =======
		InputAL.init();

		//Initialize the pixel class
		Pixel.init();

		//DO THIS JUST FOR NOW =========================
		World.init();
		World.createWorld(10);


		//set the input processor ======================
		InputProcessor ip = new InputProcessor() {
			@Override
			public boolean keyDown(int keycode) {
				return false;
			}

			@Override
			public boolean keyUp(int keycode) {
				return false;
			}

			@Override
			public boolean keyTyped(char character) {
				InputAL.charsQueue.add(character);
				return false;
			}

			@Override
			public boolean touchDown(int screenX, int screenY, int pointer, int button) {
				return false;
			}

			@Override
			public boolean touchUp(int screenX, int screenY, int pointer, int button) {
				return false;
			}

			@Override
			public boolean touchDragged(int screenX, int screenY, int pointer) {
				return false;
			}

			@Override
			public boolean mouseMoved(int screenX, int screenY) {
				return false;
			}

			@Override
			public boolean scrolled(int amount) {
				InputAL.scrollQueue.add(amount);
				return false;
			}
		};
		Gdx.input.setInputProcessor(ip);
	}

	@Override
	public void render () {



		for(Entity ent: World.getEntList()) { ent.updateType("input"); }
		//System.out.println("input");

		Collision.update();
		//System.out.println("collision");

		for(Entity ent: World.getEntList()) { ent.updateType("logic"); }
		for(Entity ent: World.getEntList()) { ent.updateType("text"); }

		Graphics.update(Gdx.graphics.getDeltaTime());
		//System.out.println("graphics");

		fpsCounter(Gdx.graphics.getRawDeltaTime());
		World.setDeltaTime(Gdx.graphics.getRawDeltaTime());
		InputAL.reset();

	}
	
	@Override
	public void dispose () {
	}
}

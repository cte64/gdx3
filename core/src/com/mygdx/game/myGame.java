package com.mygdx.game;

import box2dLight.RayHandler;
import com.badlogic.gdx.*;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import gameCode.Infrastructure.*;
import gameCode.Infrastructure.Graphics;
import gameCode.Utilities.Pixel;

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



		/*

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

		 */

		Graphics.update(Gdx.graphics.getDeltaTime());

	}
	
	@Override
	public void dispose () {


	}
}

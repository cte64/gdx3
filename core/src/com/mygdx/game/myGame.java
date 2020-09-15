package com.mygdx.game;

import com.badlogic.gdx.*;

import gameCode.Infrastructure.*;
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
		Engine.get();
		Pixel.init();
		State.mainMenu();
	}

	@Override
	public void render () {

		for(Entity ent: myWorld.get().getEntList()) { ent.updateType("input"); }
		Collision.update();

		for(Entity ent: myWorld.get().getEntList()) {
			if(ent.markBody) {
				if(ent.bodies.size() == 1) Engine.get().getPhysics().addGrid(ent);
				ent.markBody = false;
			}

			else if(ent.markBody1) {
				if(ent.bodies.size() != 1) Engine.get().getPhysics().subtractGrid(ent);
				ent.markBody1 = false;
			}
		}





		for(Entity ent: myWorld.get().getEntList()) { ent.updateType("logic"); }
		for(Entity ent: myWorld.get().getEntList()) { ent.updateType("text"); }
		for(Entity ent: myWorld.get().getEntList()) { ent.updateBody(); }


		Engine.get().getPhysics().update();
		Engine.get().getGraphics().update(Gdx.graphics.getDeltaTime());

		fpsCounter(Gdx.graphics.getRawDeltaTime());
		myWorld.get().setDeltaTime(Gdx.graphics.getRawDeltaTime());
		Engine.get().getInput().reset();

		Engine.get().getGraphics().update(Gdx.graphics.getDeltaTime());
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose () {


	}
}

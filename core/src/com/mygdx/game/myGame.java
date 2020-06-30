package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
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
import gameCode.Infastructure.Collision;
import gameCode.Infastructure.Entity;
import gameCode.Infastructure.FileSystem;
import gameCode.Infastructure.World;
import gameCode.Utilities.StringUtils;




public class myGame extends ApplicationAdapter {

	SpriteBatch batch;
	private TextureAtlas atlas;
	private TextureAtlas tileAtlas;

	private Viewport viewport;
	private Camera camera;

	private static HashMap<String, Sprite> spriteMap;
	private static HashMap<String, Sprite> tileMap;

	private ShapeRenderer shapeRenderer;


	private final int xTiles = 20;
	private final int yTiles = 20;
	private final int tileSize = 20;

	float timerCount = 0;
	int fpsCount = 0;

	private void drawOutlines() {


		shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

		//center ==============================================================================
		//int cLeft = FileSystem.centerLeft * World.tilesPerChunk * World.tileSize;
		//int cRight = FileSystem.centerRight * World.tilesPerChunk * World.tileSize;
		//int cTop = FileSystem.centerTop * World.tilesPerChunk * World.tileSize;
		//int cBottom = FileSystem.centerBottom * World.tilesPerChunk * World.tileSize;

		shapeRenderer.setColor(0, 0, 1, 1);
		shapeRenderer.line(10, 100, 10, 500);
		shapeRenderer.line(590, 100, 590, 500);

		/*
		sf::RectangleShape rectangle(sf::Vector2f(cRight - cLeft , 1));
		rectangle.setFillColor(sf::Color(0, 100, 250));
		rectangle.setPosition(cLeft, cTop);
		window->draw(rectangle);

		rectangle.setPosition(cLeft, cBottom);
		window->draw(rectangle);

		rectangle.setSize(sf::Vector2f(1, cBottom - cTop));
		rectangle.setPosition(cLeft, cTop);
		window->draw(rectangle);

		rectangle.setPosition(cRight, cTop);
		window->draw(rectangle);

		//middle =================================================================================
		int mLeft = world.fileThing.middleLeft * world.tilesPerChunk * world.tileSize;
		int mRight = world.fileThing.middleRight * world.tilesPerChunk * world.tileSize;
		int mTop = world.fileThing.middleTop * world.tilesPerChunk * world.tileSize;
		int mBottom = world.fileThing.middleBottom * world.tilesPerChunk * world.tileSize;

		sf::RectangleShape rectangle1(sf::Vector2f(mRight - mLeft , 2));
		rectangle1.setFillColor(sf::Color(255, 0, 0));
		rectangle1.setPosition(mLeft, mTop);
		window->draw(rectangle1);

		rectangle1.setPosition(mLeft, mBottom);
		window->draw(rectangle1);

		rectangle1.setSize(sf::Vector2f(2, mBottom - mTop));
		rectangle1.setPosition(mLeft, mTop);
		window->draw(rectangle1);

		rectangle1.setPosition(mRight, mTop);
		window->draw(rectangle1);

		//outer =================================================================================
		int oLeft = world.fileThing.outerLeft * world.tilesPerChunk * world.tileSize;
		int oRight = world.fileThing.outerRight * world.tilesPerChunk * world.tileSize;
		int oTop = world.fileThing.outerTop * world.tilesPerChunk * world.tileSize;
		int oBottom = world.fileThing.outerBottom * world.tilesPerChunk * world.tileSize;

		sf::RectangleShape rectangle2(sf::Vector2f(oRight - oLeft , 1));
		rectangle2.setFillColor(sf::Color(150, 50, 250));
		rectangle2.setPosition(oLeft, oTop);
		window->draw(rectangle2);

		rectangle2.setPosition(oLeft, oBottom);
		window->draw(rectangle2);

		rectangle2.setSize(sf::Vector2f(1, oBottom - oTop));
		rectangle2.setPosition(oLeft, oTop);
		window->draw(rectangle2);

		rectangle2.setPosition(oRight, oTop);
		window->draw(rectangle2);
		 */

		shapeRenderer.end();


	}

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

	private void positionCamera1() {

		Entity hero = World.getCamera();

		if(hero != null) {


			int x = -World.getxViewSize()/2 + (int)hero.x_pos + hero.getWidth()/2;
			int y = -World.getyViewSize()/2 + (int)hero.y_pos + hero.getHeight()/2;

			viewport.setScreenPosition(-x, -y);



				/*
			view.setCenter( center );

			viewPortLeft = view.getCenter().x - world.xViewSize/2;
			viewPortTop = view.getCenter().y - world.yViewSize/2;

			view.reset(sf::FloatRect(viewPortLeft, viewPortTop, world.xViewSize, world.yViewSize));

			view.rotate(-hero_ptr->angle);
			window->setView(view);
			*/
		}

	}

	public void resize(int width, int height) {
		viewport.update(width, height);
		//viewport.setScreenSize(600, 600);
	}

	@Override
	public void create () {
		camera = new OrthographicCamera();
		viewport = new FitViewport(600, 600, camera);
		shapeRenderer = new ShapeRenderer();


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

		World.createWorld(10);


		/*
		StringUtils str = new StringUtils("");
		FileSystem.setGameSubDirectory("three");

		FileSystem.getFile(new StringUtils("[type: metadata]"), str);

		System.out.println(str.data);

		 */


	}

	@Override
	public void render () {


		positionCamera1();
		viewport.apply();

		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();

		//This is where everything happens! ================================================================
		for(Entity ent: World.getEntList()) { ent.updateType("input"); }
		Collision.update();
		for(Entity ent: World.getEntList()) { batch.draw(spriteMap.get(ent.spriteName), ent.x_pos, ent.y_pos); }
		//This is where everything happens! ================================================================

		batch.end();

		drawOutlines();

		fpsCounter(Gdx.graphics.getRawDeltaTime());

	}
	
	@Override
	public void dispose () {
		//batch.dispose();
		//atlas.dispose();
	}
}

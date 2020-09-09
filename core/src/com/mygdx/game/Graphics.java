package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import gameCode.Infrastructure.*;
import gameCode.Menus.TextComponent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Graphics implements Disposable {

    private OrthographicCamera camera;
    public CameraHelper cameraHelper;
    private ShapeRenderer shapeRenderer;
    private SpriteBatch batch;
    private SpriteBatch hudBatch;

    //this is for sorting them by zPos
    public class sorter implements Comparator<Entity> {
        @Override
        public int compare(Entity o1, Entity o2) {
            if(o1.z_pos < o2.z_pos) return -1;
            if(o1.z_pos == o2.z_pos) return 0;
            return 1;
        }
    }

    public void init() {




        //Fonts ==================================================================================
        cameraHelper = new CameraHelper();
        shapeRenderer = new ShapeRenderer();


        //Set up the camera ======================================================================
        camera = new OrthographicCamera(World.get().getViewPortWidth(), World.get().getViewPortHeight());
        camera.position.set(0, 0, 0);
        camera.update();

        //Set up the sprites =====================================================================
        batch = new SpriteBatch();
        hudBatch = new SpriteBatch();






    }

    private void setCamera1() {
        Entity hero = World.get().getCamera();
        if(hero == null) return;
        float xPos = hero.x_pos;
        float yPos = hero.y_pos;
        cameraHelper.setPosition(xPos, yPos);
    }

    private void drawOutlines() {

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        //center ==============================================================================
        int cLeft = FileSystem.centerLeft * World.get().tilesPerChunk * World.get().tileSize;
        int cRight = FileSystem.centerRight * World.get().tilesPerChunk * World.get().tileSize;
        int cTop = FileSystem.centerTop * World.get().tilesPerChunk * World.get().tileSize;
        int cBottom = FileSystem.centerBottom * World.get().tilesPerChunk * World.get().tileSize;

        shapeRenderer.setColor(0, 0, 1, 1);
        shapeRenderer.line(cLeft, cTop, cLeft, cBottom);
        shapeRenderer.line(cRight, cTop, cRight, cBottom);
        shapeRenderer.line(cLeft, cTop, cRight, cTop);
        shapeRenderer.line(cLeft, cBottom, cRight, cBottom);

		//middle =================================================================================
		int mLeft = FileSystem.middleLeft * World.get().tilesPerChunk * World.get().tileSize;
		int mRight = FileSystem.middleRight * World.get().tilesPerChunk * World.get().tileSize;
		int mTop = FileSystem.middleTop * World.get().tilesPerChunk * World.get().tileSize;
		int mBottom = FileSystem.middleBottom * World.get().tilesPerChunk * World.get().tileSize;

        shapeRenderer.setColor(1, 0, 0.5f, 1);
        shapeRenderer.line(mLeft, mTop, mLeft, mBottom);
        shapeRenderer.line(mRight, mTop, mRight, mBottom);
        shapeRenderer.line(mLeft, mTop, mRight, mTop);
        shapeRenderer.line(mLeft, mBottom, mRight, mBottom);

        //outer =================================================================================
        int oLeft = FileSystem.outerLeft * World.get().tilesPerChunk * World.get().tileSize;
        int oRight = FileSystem.outerRight * World.get().tilesPerChunk * World.get().tileSize;
        int oTop = FileSystem.outerTop * World.get().tilesPerChunk * World.get().tileSize;
        int oBottom = FileSystem.outerBottom * World.get().tilesPerChunk * World.get().tileSize;

        shapeRenderer.setColor(1, 0, 0, 1);
        shapeRenderer.line(oLeft, oTop, oLeft, oBottom);
        shapeRenderer.line(oRight, oTop, oRight, oBottom);
        shapeRenderer.line(oLeft, oTop, oRight, oTop);
        shapeRenderer.line(oLeft, oBottom, oRight, oBottom);

        shapeRenderer.end();
    }

    public Graphics() {
        init();
    }

    public Vector3 getMouse() {
        return camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
    }

    public void update(float deltaTime) {

        setCamera1();
        Collections.sort(World.get().getEntByZIndex(), new sorter());
        cameraHelper.update(deltaTime);
        cameraHelper.applyTo(camera);
        batch.setProjectionMatrix(camera.combined);
        shapeRenderer.setProjectionMatrix(camera.combined);

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        hudBatch.begin();
            for(Entity ent: World.get().getEntByZIndex()) {

                if(ent.drawMode != "hud") continue;


                if(Engine.get().getAssets().spriteMap.containsKey(ent.spriteName)) {
                    hudBatch.draw(Engine.get().getAssets().spriteMap.get(ent.spriteName), ent.x_pos, ent.y_pos, ent.getWidth(), ent.getHeight());
                }

                ArrayList<Component> textComps = ent.getComponents("text");
                for(Component comp: textComps) {
                    TextComponent text = (TextComponent)comp;
                    if(text != null && text.show) {
                        int fontSize = text.getFontSize();
                        if(!Engine.get().getAssets().bmpFonts.containsKey(fontSize)) continue;
                        Engine.get().getAssets().bmpFonts.get(text.getFontSize()).setColor(text.getR(), text.getG(), text.getB(), text.getA());
                        Engine.get().getAssets().bmpFonts.get(text.getFontSize()).draw(hudBatch, text.getText(), text.getXPos(), text.getYPos());
                    }
                }
            }
        hudBatch.end();

        drawOutlines();
    }

    public void resize(int width, int height) {
        camera.viewportWidth = width;
        camera.viewportHeight = height;
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}

package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Disposable;
import gameCode.Infrastructure.*;
import gameCode.Menus.TextComponent;
import gameCode.Utilities.myMath;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Graphics implements Disposable {

    private OrthographicCamera camera;
    public CameraHelper cameraHelper;
    private ShapeRenderer shapeRenderer;
    private SpriteBatch batch;
    private SpriteBatch hudBatch;
    private Box2DDebugRenderer b2debug;
    private int viewPortWidth, viewPortHeight;

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
        viewPortWidth = 900;
        viewPortHeight = 600;
        cameraHelper = new CameraHelper();
        shapeRenderer = new ShapeRenderer();
        camera = new OrthographicCamera(viewPortWidth, viewPortHeight);
        camera.position.set(0, 0, 0);
        camera.update();
        batch = new SpriteBatch();
        hudBatch = new SpriteBatch();
        b2debug = new Box2DDebugRenderer();
    }

    public CameraHelper getCameraHelper() { return cameraHelper; }

    private void drawOutlines() {

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        //center ==============================================================================
        int cLeft = Engine.get().getFileSystem().centerLeft * myWorld.get().tilesPerChunk * myWorld.get().tileSize;
        int cRight = Engine.get().getFileSystem().centerRight * myWorld.get().tilesPerChunk * myWorld.get().tileSize;
        int cTop = Engine.get().getFileSystem().centerTop * myWorld.get().tilesPerChunk * myWorld.get().tileSize;
        int cBottom = Engine.get().getFileSystem().centerBottom * myWorld.get().tilesPerChunk * myWorld.get().tileSize;

        shapeRenderer.setColor(0, 0, 1, 1);
        shapeRenderer.line(cLeft, cTop, cLeft, cBottom);
        shapeRenderer.line(cRight, cTop, cRight, cBottom);
        shapeRenderer.line(cLeft, cTop, cRight, cTop);
        shapeRenderer.line(cLeft, cBottom, cRight, cBottom);

		//middle =================================================================================
		int mLeft = Engine.get().getFileSystem().middleLeft * myWorld.get().tilesPerChunk * myWorld.get().tileSize;
		int mRight = Engine.get().getFileSystem().middleRight * myWorld.get().tilesPerChunk * myWorld.get().tileSize;
		int mTop = Engine.get().getFileSystem().middleTop * myWorld.get().tilesPerChunk * myWorld.get().tileSize;
		int mBottom = Engine.get().getFileSystem().middleBottom * myWorld.get().tilesPerChunk * myWorld.get().tileSize;

        shapeRenderer.setColor(1, 0, 0.5f, 1);
        shapeRenderer.line(mLeft, mTop, mLeft, mBottom);
        shapeRenderer.line(mRight, mTop, mRight, mBottom);
        shapeRenderer.line(mLeft, mTop, mRight, mTop);
        shapeRenderer.line(mLeft, mBottom, mRight, mBottom);

        //outer =================================================================================
        int oLeft = Engine.get().getFileSystem().outerLeft * myWorld.get().tilesPerChunk * myWorld.get().tileSize;
        int oRight = Engine.get().getFileSystem().outerRight * myWorld.get().tilesPerChunk * myWorld.get().tileSize;
        int oTop = Engine.get().getFileSystem().outerTop * myWorld.get().tilesPerChunk * myWorld.get().tileSize;
        int oBottom = Engine.get().getFileSystem().outerBottom * myWorld.get().tilesPerChunk * myWorld.get().tileSize;

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

        //Clear the screen to a color =================================================
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Sort the Entities by z-Index ================================================
        Collections.sort(myWorld.get().getEntByZIndex(), new sorter());

        //Camera Stuff ================================================================
        cameraHelper.update();
        cameraHelper.applyTo(camera);
        batch.setProjectionMatrix(camera.combined);

        //Batch Rendering ============================================================
        batch.begin();
            for(Entity ent: myWorld.get().getEntByZIndex()) {
                if(ent.drawMode == "hud") continue;

                if(Engine.get().getAssets().spriteMap.containsKey(ent.spriteName)) {

                    TextureRegion reg = Engine.get().getAssets().spriteMap.get(ent.spriteName);
                    reg.setRegionX(reg.getRegionX() + ent.spriteOffsetX);
                    reg.setRegionY(reg.getRegionY() + ent.spriteOffsetY);
                    reg.setRegionWidth((int)ent.width);
                    reg.setRegionHeight((int)ent.height);

                    batch.draw( reg,
                                ent.x_pos, ent.y_pos,
                            ent.getWidth()/2, ent.getHeight()/2,
                                ent.width, ent.height,
                                1.0f, 1.0f,
                                myMath.toDeg( ent.angle));
                }

                /*
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

                 */
            }
        batch.end();

        hudBatch.begin();
            for(Entity ent: myWorld.get().getEntByZIndex()) {

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

        //Draw the outlines of the Box2d collision box and the boundaries of the chunks =============
        drawOutlines();
        b2debug.render(Engine.get().getPhysics().getb2World(), camera.combined);
    }

    public void resize(int width, int height) {
        camera.viewportWidth = width;
        camera.viewportHeight = height;
    }

    public Entity getCamera() {
        return cameraHelper.getTarget();
    }

    public int getVPWidth() {
        return viewPortWidth;
    }

    public int getVPHeight() { return viewPortHeight; }

    public void setVPWidth(int w) {
        viewPortWidth = w;
    }

    public void setVPHeight(int h) {
        viewPortHeight = h;
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}

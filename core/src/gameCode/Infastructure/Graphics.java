package gameCode.Infastructure;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Disposable;
import gameCode.Infastructure.World;
import gameCode.Utilities.StringUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Graphics implements Disposable {

    private static TextureAtlas spriteAtlas;
    private static TextureAtlas tileAtlas;
    private static ArrayList<String> tileIDs;
    private static SpriteBatch batch;
    private static OrthographicCamera camera;
    public static CameraHelper cameraHelper;
    private static ShapeRenderer shapeRenderer;
    private static HashMap<String, Sprite> spriteMap;

    public static void returnCoord(String coord) {
        tileIDs.add(coord);
    }

    public static String getCoord() {
        if(tileIDs.size() == 0) return "";
        String retVal = tileIDs.get(0);
        tileIDs.remove(0);
        return retVal;
    }

    private static void setCamera1() {
        Entity hero = World.getCamera();
        if(hero == null) return;
        float xPos = hero.x_pos;
        float yPos = hero.y_pos;
        cameraHelper.setPosition(xPos, yPos);
    }

    private static void drawOutlines() {

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        //center ==============================================================================
        int cLeft = FileSystem.centerLeft * World.tilesPerChunk * World.tileSize;
        int cRight = FileSystem.centerRight * World.tilesPerChunk * World.tileSize;
        int cTop = FileSystem.centerTop * World.tilesPerChunk * World.tileSize;
        int cBottom = FileSystem.centerBottom * World.tilesPerChunk * World.tileSize;

        shapeRenderer.setColor(0, 0, 1, 1);
        shapeRenderer.line(cLeft, cTop, cLeft, cBottom);
        shapeRenderer.line(cRight, cTop, cRight, cBottom);
        shapeRenderer.line(cLeft, cTop, cRight, cTop);
        shapeRenderer.line(cLeft, cBottom, cRight, cBottom);

		//middle =================================================================================
		int mLeft = FileSystem.middleLeft * World.tilesPerChunk * World.tileSize;
		int mRight = FileSystem.middleRight * World.tilesPerChunk * World.tileSize;
		int mTop = FileSystem.middleTop * World.tilesPerChunk * World.tileSize;
		int mBottom = FileSystem.middleBottom * World.tilesPerChunk * World.tileSize;

        shapeRenderer.setColor(1, 0, 0.5f, 1);
        shapeRenderer.line(mLeft, mTop, mLeft, mBottom);
        shapeRenderer.line(mRight, mTop, mRight, mBottom);
        shapeRenderer.line(mLeft, mTop, mRight, mTop);
        shapeRenderer.line(mLeft, mBottom, mRight, mBottom);

        //outer =================================================================================
        int oLeft = FileSystem.outerLeft * World.tilesPerChunk * World.tileSize;
        int oRight = FileSystem.outerRight * World.tilesPerChunk * World.tileSize;
        int oTop = FileSystem.outerTop * World.tilesPerChunk * World.tileSize;
        int oBottom = FileSystem.outerBottom * World.tilesPerChunk * World.tileSize;

        shapeRenderer.setColor(1, 0, 0, 1);
        shapeRenderer.line(oLeft, oTop, oLeft, oBottom);
        shapeRenderer.line(oRight, oTop, oRight, oBottom);
        shapeRenderer.line(oLeft, oTop, oRight, oTop);
        shapeRenderer.line(oLeft, oBottom, oRight, oBottom);

        shapeRenderer.end();
    }

    public static void init() {

        cameraHelper = new CameraHelper();
        shapeRenderer = new ShapeRenderer();
        tileIDs = new ArrayList<String>();

        //Set up the camera ======================================================================
        camera = new OrthographicCamera(World.getViewPortWidth(), World.getViewPortHeight());
        camera.position.set(0, 0, 0);
        camera.update();

        //Set up the sprites =====================================================================
        batch = new SpriteBatch();
        spriteMap = new HashMap<String, Sprite>();
        spriteAtlas = new TextureAtlas("/Users/me/Desktop/gdx3/core/assets/atlas.atlas");
        tileAtlas = new TextureAtlas();
        addSprite("tile");
        addSprite("thing");

        //set up the tile atlas ==================================================================
        int numTiles = 50;
        int padding = 1;
        for(int y = 0; y < numTiles; y++) {
        for(int x = 0; x < numTiles; x++) {
            int xPos = padding + x*(padding + World.tileSize);
            int yPos = padding + y*(padding + World.tileSize);
            String name = "tileId: " + StringUtils.toString(x) + "." + StringUtils.toString(y);
            Texture newTexture = new Texture(World.tileSize, World.tileSize, Pixmap.Format.RGB888);
            tileAtlas.addRegion(name, newTexture, xPos, yPos, World.tileSize, World.tileSize);
            TextureRegion region = tileAtlas.findRegion(name);
            Sprite sprite = new Sprite(region);
            spriteMap.put(name, sprite);
            tileIDs.add(name);
        }}
    }

    public static void updateSprite(String name, Pixmap image) {
        TextureRegion region = tileAtlas.findRegion(name);
        Texture newTexture = new Texture(image);
        region.setTexture(newTexture);
        Sprite newSprite = new Sprite( tileAtlas.findRegion(name));
        spriteMap.get(name).set(newSprite);
    }

    public Graphics()  {
    }

    private static void addSprite(String filename) {
        TextureRegion region = spriteAtlas.findRegion(filename);
        Sprite sprite = new Sprite(region);
        spriteMap.put(filename, sprite);
    }

    public static void update(float deltaTime) {

        setCamera1();

        cameraHelper.update(deltaTime);
        cameraHelper.applyTo(camera);
        batch.setProjectionMatrix(camera.combined);
        shapeRenderer.setProjectionMatrix(camera.combined);

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        for(Entity ent: World.getEntList()) { batch.draw(spriteMap.get(ent.spriteName), ent.x_pos, ent.y_pos); }
        batch.end();

        drawOutlines();
    }

    public static void resize(int width, int height) {
        camera.viewportWidth = width;
        camera.viewportHeight = height;
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}

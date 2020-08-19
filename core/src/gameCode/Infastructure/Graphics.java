package gameCode.Infastructure;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
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
    private static SpriteBatch hudBatch;
    private static OrthographicCamera camera;
    public static CameraHelper cameraHelper;
    private static ShapeRenderer shapeRenderer;
    private static HashMap<String, Sprite> spriteMap;
    private static HashMap<String, String> seen = new HashMap<String, String>();
    private static BitmapFont font;
    private static GlyphLayout layout;

    public static void init() {

        //Fonts ==================================================================================
        font = new BitmapFont();
        layout = new GlyphLayout();

        cameraHelper = new CameraHelper();
        shapeRenderer = new ShapeRenderer();
        tileIDs = new ArrayList<String>();

        //Set up the camera ======================================================================
        camera = new OrthographicCamera(World.getViewPortWidth(), World.getViewPortHeight());
        camera.position.set(0, 0, 0);
        camera.update();

        //Set up the sprites =====================================================================
        batch = new SpriteBatch();
        hudBatch = new SpriteBatch();
        spriteMap = new HashMap<String, Sprite>();
        spriteAtlas = new TextureAtlas("/Users/me/Desktop/gdx3/core/assets/atlas.atlas");
        tileAtlas = new TextureAtlas();

        //Menus stuff
        addSprite("tile");
        addSprite("mainMenuBack");
        addSprite("createGameBack");
        addSprite("loadGameBack");
        addSprite("loadGameFront");
        addSprite("minus");
        addSprite("plus");
        addSprite("play");
        addSprite("playHover");
        addSprite("loadGameDeleteOpen");
        addSprite("loadGameDeleteClosed");
        addSprite("loadGameDeleteClosed1");
        addSprite("halfMenuItem");
        addSprite("menuItem");
        addSprite("listItem");
        addSprite("scrollBar");
        addSprite("inventoryTray");

        /*
        addSprite("tile");


         */


        //set up the tile atlas ==================================================================
        int numTiles = 40;
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

    public static void returnCoord(String coord) { tileIDs.add(coord);  }

    public static String getCoord() {
        if(tileIDs.size() == 0) return "";
        String retVal = new String ( tileIDs.get(0)) ;
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


    public static void updateSprite(String name, Pixmap image) {
        TextureRegion region = tileAtlas.findRegion(name);
        Texture newTexture = new Texture(image);
        region.setTexture(newTexture);
        spriteMap.put(name, new Sprite(newTexture));
    }

    public Graphics() {
    }

    public static Vector3 getMouse() {
        return camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
    }

    private static void addSprite(String filename) {
        TextureRegion region = spriteAtlas.findRegion(filename);
        Sprite sprite = new Sprite(region);
        spriteMap.put(filename, sprite);
    }

    private static void printColors(String name) {

        Texture texture = spriteMap.get(name).getTexture();
        TextureData td = texture.getTextureData();
        Pixmap img = td.consumePixmap();

        System.out.print(name + " : ");

        HashMap<Integer, Integer> stuff = new HashMap<Integer, Integer>();
        for (int j = 0; j < 60; j++) {
            for (int i = 0; i < 60; i++) {
                int pix = img.getPixel(i, j);
                if (!stuff.containsKey(pix)) stuff.put(pix, 1);
                else stuff.put(pix, stuff.get(pix) + 1);
            }
        }
        for (int i : stuff.keySet()) {
            System.out.print(i + "." + stuff.get(i) + "  ");
        }
        System.out.println("");

    }

    public static Vector2 getTextBounds(String text, int fontSize) {
        layout.setText(font, text);
        float width = layout.width;
        float height = layout.height;
        return new Vector2(width, height);
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
            for(Entity ent: World.getEntByZIndex()) {
                if(ent.drawMode == "normal" && spriteMap.containsKey(ent.spriteName)) {
                    batch.draw(spriteMap.get(ent.spriteName), ent.x_pos, ent.y_pos);
                }
            }
        batch.end();


        hudBatch.begin();
            for(Entity ent: World.getEntByZIndex()) {

                if(ent.drawMode != "hud") continue;
                if(spriteMap.containsKey(ent.spriteName)) hudBatch.draw(spriteMap.get(ent.spriteName), ent.x_pos, ent.y_pos);

                ArrayList<Component> textComps = ent.getComponents("text");
                for(Component comp: textComps) {
                    TextComponent text = (TextComponent)comp;
                    if(text != null && text.show) {
                        font.setColor(1.0f, 1.0f, 0.0f, 1.0f);
                        font.draw(hudBatch, text.getText(), text.getXPos(), text.getYPos());
                    }
                }
            }
        hudBatch.end();




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

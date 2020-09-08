package com.mygdx.game;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.Disposable;
import gameCode.Infrastructure.*;
import gameCode.Menus.TextComponent;
import gameCode.Utilities.StringUtils;
import gameCode.Utilities.myPair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;



import com.badlogic.gdx.graphics.Pixmap.Format;

import static com.badlogic.gdx.graphics.Pixmap.Format.RGB888;

public class Graphics implements Disposable {

    private TextureAtlas spriteAtlas;
    private TextureAtlas tileAtlas;
    private ArrayList<String> tileIDs;
    private SpriteBatch batch;
    private SpriteBatch hudBatch;
    private OrthographicCamera camera;
    public CameraHelper cameraHelper;
    private ShapeRenderer shapeRenderer;
    private HashMap<String, Sprite> spriteMap;
    private HashMap<String, String> seen = new HashMap<String, String>();
    private BitmapFont font;
    private GlyphLayout layout;
    private Body player;


    //Font stuff here ======================================
    private FreeTypeFontGenerator generator;
    private FreeTypeFontGenerator.FreeTypeFontParameter parameter;
    private HashMap<Integer, BitmapFont> bmpFonts;



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


        //Font stuff =====================================================================================
        generator = new FreeTypeFontGenerator(Gdx.files.internal("core/fonts/timesNewRoman.ttf"));
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        bmpFonts = new HashMap<Integer, BitmapFont>();
        for(int x = 10; x < 40; x++) {
            parameter.size = x;
            BitmapFont newFont = generator.generateFont(parameter);
            bmpFonts.put(x, newFont);
        }

        generator.dispose(); // don't forget to dispose to avoid memory leaks!

        //Fonts ==================================================================================
        font = new BitmapFont();
        layout = new GlyphLayout();
        cameraHelper = new CameraHelper();
        shapeRenderer = new ShapeRenderer();
        tileIDs = new ArrayList<String>();


        //Set up the camera ======================================================================
        camera = new OrthographicCamera(World.get().getViewPortWidth(), World.get().getViewPortHeight());
        camera.position.set(0, 0, 0);
        camera.update();

        //Set up the sprites =====================================================================
        batch = new SpriteBatch();
        hudBatch = new SpriteBatch();
        spriteMap = new HashMap<String, Sprite>();
        spriteAtlas = new TextureAtlas("/Users/me/Desktop/gdx3/core/assets/atlas.atlas");
        tileAtlas = new TextureAtlas();

        //Menus stuff
        for(TextureAtlas.AtlasRegion name: spriteAtlas.getRegions()) {

            String[] strs = name.name.split("/");
            String newName = strs[strs.length - 1];

            TextureRegion region = spriteAtlas.findRegion(name.name);
            Sprite sprite = new Sprite(region);
            spriteMap.put(newName, sprite);
        }


        //set up the tile atlas ==================================================================
        int numTiles = 40;
        int padding = 1;
        for(int y = 0; y < numTiles; y++) {
        for(int x = 0; x < numTiles; x++) {
            int xPos = padding + x*(padding + World.get().tileSize);
            int yPos = padding + y*(padding + World.get().tileSize);
            String name = "tileId: " + StringUtils.toString(x) + "." + StringUtils.toString(y);
            Texture newTexture = new Texture(World.get().tileSize, World.get().tileSize, RGB888);
            tileAtlas.addRegion(name, newTexture, xPos, yPos, World.get().tileSize, World.get().tileSize);
            TextureRegion region = tileAtlas.findRegion(name);
            Sprite sprite = new Sprite(region);
            spriteMap.put(name, sprite);
            tileIDs.add(name);
        }}
    }

    public void returnCoord(String coord) { tileIDs.add(coord);  }

    public myPair<Integer, Integer> getSpriteDimensions(String name) {

        myPair<Integer, Integer> retVal = new myPair(0, 0);

        if(spriteMap.containsKey(name)) {
            retVal.first = (int)spriteMap.get(name).getWidth();
            retVal.second = (int)spriteMap.get(name).getHeight();
        }

        return retVal;
    }

    public String getCoord() {
        if(tileIDs.size() == 0) return "";
        String retVal = new String ( tileIDs.get(0)) ;
        tileIDs.remove(0);
        return retVal;
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

    public void updateSprite(String name, Pixmap image) {


        Texture dt = new Texture(60, 60, Pixmap.Format.RGBA8888);
        dt.draw(image, 0, 0);


        TextureRegion region = tileAtlas.findRegion(name);

        region.setTexture(dt);
        spriteMap.put(name, new Sprite(dt));

    }

    public Graphics() {
        init();
    }

    public Vector3 getMouse() {
        return camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
    }

    private void addSprite(String filename) {
        TextureRegion region = spriteAtlas.findRegion(filename);
        Sprite sprite = new Sprite(region);
        spriteMap.put(filename, sprite);
    }

    private void printColors(String name) {

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

    public Vector2 getTextBounds(String text, int fontSize) {
        layout.setText(bmpFonts.get(fontSize), text);
        float width = layout.width;
        float height = layout.height;
        return new Vector2(width, height);
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


                if(spriteMap.containsKey(ent.spriteName)) {
                    hudBatch.draw(spriteMap.get(ent.spriteName), ent.x_pos, ent.y_pos, ent.getWidth(), ent.getHeight());
                }

                ArrayList<Component> textComps = ent.getComponents("text");
                for(Component comp: textComps) {
                    TextComponent text = (TextComponent)comp;
                    if(text != null && text.show) {
                        int fontSize = text.getFontSize();
                        if(!bmpFonts.containsKey(fontSize)) continue;
                        bmpFonts.get(text.getFontSize()).setColor(text.getR(), text.getG(), text.getB(), text.getA());
                        bmpFonts.get(text.getFontSize()).draw(hudBatch, text.getText(), text.getXPos(), text.getYPos());
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

package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import gameCode.Infrastructure.myWorld;
import gameCode.Utilities.Pixel;
import gameCode.Utilities.myString;
import gameCode.Utilities.myPair;

import java.util.ArrayList;
import java.util.HashMap;

import static com.badlogic.gdx.graphics.Pixmap.Format.RGB888;
import static com.badlogic.gdx.graphics.Pixmap.Format.fromGdx2DPixmapFormat;

public class Assets {

    private class AssetNode {
        public Sprite sprite;
        public Pixmap pixmap;
    }

    public TextureAtlas spriteAtlas;
    public TextureAtlas tileAtlas;
    public ArrayList<String> tileIDs;
    public HashMap<String, AssetNode> spriteMap;
    public GlyphLayout layout;


    //Font stuff here ======================================
    public FreeTypeFontGenerator generator;
    public FreeTypeFontGenerator.FreeTypeFontParameter parameter;
    public HashMap<Integer, BitmapFont> bmpFonts;

    public Assets() {

        spriteAtlas = new TextureAtlas("/Users/me/Desktop/gdx3/core/assets/atlases/atlas.atlas");

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

        layout = new GlyphLayout();
        tileIDs = new ArrayList<String>();


        spriteMap = new HashMap<String, AssetNode>();
        tileAtlas = new TextureAtlas();

        //Menus stuff
        for(TextureAtlas.AtlasRegion name: spriteAtlas.getRegions()) {

            String[] strs = name.name.split("/");
            String newName = strs[strs.length - 1];

            TextureRegion region = spriteAtlas.findRegion(name.name);
            Sprite sprite = new Sprite(region);

            AssetNode node = new AssetNode();
            node.sprite = sprite;

            sprite.getTexture().getTextureData().prepare();


            Pixmap pixmap = new Pixmap(sprite.getRegionWidth(), sprite.getRegionHeight(), Pixmap.Format.RGBA8888);
            pixmap = sprite.getTexture().getTextureData().consumePixmap();
            node.pixmap = pixmap;

            spriteMap.put(newName, node);
        }


        //set up the tile atlas ==================================================================
        int numTiles = 40;
        int padding = 1;
        for(int y = 0; y < numTiles; y++) {
        for(int x = 0; x < numTiles; x++) {
            int xPos = padding + x*(padding + myWorld.get().tileSize);
            int yPos = padding + y*(padding + myWorld.get().tileSize);
            String name = "tileId: " + myString.toString(x) + "." + myString.toString(y);
            Texture newTexture = new Texture(myWorld.get().tileSize, myWorld.get().tileSize, RGB888);
            tileAtlas.addRegion(name, newTexture, xPos, yPos, myWorld.get().tileSize, myWorld.get().tileSize);
            TextureRegion region = tileAtlas.findRegion(name);
            Sprite sprite = new Sprite(region);

            AssetNode node = new AssetNode();
            node.sprite = sprite;

            spriteMap.put(name, node);
            tileIDs.add(name);
        }}

    }

    public void updateSprite(String name, Pixmap image) {

        Texture dt = new Texture(60, 60, Pixmap.Format.RGBA8888);
        dt.draw(image, 0, 0);

        TextureRegion region = tileAtlas.findRegion(name);

        region.setTexture(dt);

        AssetNode node = new AssetNode();
        node.sprite = new Sprite(dt);
        node.pixmap = image;

        spriteMap.put(name, node);
    }

    public String getCoord() {
        if(tileIDs.size() == 0) return "";
        String retVal = new String ( tileIDs.get(0)) ;
        tileIDs.remove(0);
        return retVal;
    }

    public void returnCoord(String coord) { tileIDs.add(coord);  }

    public myPair<Integer, Integer> getSpriteDimensions(String name) {

        myPair<Integer, Integer> retVal = new myPair(0, 0);

        if(spriteMap.containsKey(name)) {
            retVal.first = (int)spriteMap.get(name).sprite.getWidth();
            retVal.second = (int)spriteMap.get(name).sprite.getHeight();
        }

        return retVal;
    }

    public Vector2 getTextBounds(String text, int fontSize) {
        layout.setText(bmpFonts.get(fontSize), text);
        float width = layout.width;
        float height = layout.height;
        return new Vector2(width, height);
    }

    public TextureRegion getRegion(String id) {
        if(!spriteMap.containsKey(id)) return null;
        else return spriteMap.get(id).sprite;
    }

    public int getPixel(String id, int x, int y) {
        if(!spriteMap.containsKey(id)) return 0;
        if(spriteMap.get(id).pixmap == null) return 0;
        return spriteMap.get(id).pixmap.getPixel(x, y);
    }
}

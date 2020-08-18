package gameCode.Infastructure;

import com.badlogic.gdx.graphics.Pixmap;
import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import gameCode.Utilities.MathUtils;
import gameCode.Utilities.Pixel;
import gameCode.Utilities.StringUtils;
import gameCode.Utilities.myPair;

public class Chunk {


    private String chunkName;


    private class Tile {
        public Pixmap image;
        public StringUtils terrainData;
        public boolean active;
        public String tileName;
        public Tile(int index) {
            image = new Pixmap(World.tileSize, World.tileSize, Pixmap.Format.RGB888);
            terrainData = new StringUtils("");
            active = false;

            StringUtils newName = new StringUtils("[type: tile][chunkX: ][chunkY: ][tileX: ][tileY: ]");
            StringUtils.setField(newName, "chunkX", StringUtils.getField(chunkName, "chunkX"));
            StringUtils.setField(newName, "chunkY", StringUtils.getField(chunkName, "chunkY"));
            StringUtils.setField(newName, "tileX", StringUtils.toString(index % World.tilesPerChunk));
            StringUtils.setField(newName, "tileY", StringUtils.toString(index / World.tilesPerChunk));
            tileName = newName.data;
        }
    }

    private ArrayList<Tile> tiles;

    public Chunk(myPair<Integer, Integer> key) {
        chunkName = "[type: chunk][chunkX: " + StringUtils.toString(key.first) + "][chunkY: " + StringUtils.toString(key.second) + "]";
        tiles = new ArrayList<Tile>();
    }

    //Setters =============================================================================================
    public void setTerrain(StringUtils terrainData) {
        ArrayList<StringUtils> tileStrArr = StringUtils.getBeforeChar(terrainData.data, '\n');
        for(int index = 0; index < tileStrArr.size(); index++) {
            Tile newTile = new Tile(index);
            newTile.terrainData = tileStrArr.get(index);
            newTile.image = Pixel.stringToImage(tileStrArr.get(index));
            tiles.add(newTile);
        }
    }
    public void setActive(int x, int y, boolean newActive) {
        int index = (World.tilesPerChunk * y) + x;
        index = MathUtils.clamp(index, 0, tiles.size() - 1);
        tiles.get(index).active = newActive;
    }

    //Getters =============================================================================================
    public String getChunkName() { return chunkName; }
    public Pixmap getImage(int x, int y) {
        int index = (World.tilesPerChunk * y) + x;
        index = MathUtils.clamp(index, 0, tiles.size() - 1);
        return tiles.get(index).image;
    }
    public boolean getActive(int x, int y) {
        int index = (World.tilesPerChunk * y) + x;
        index = MathUtils.clamp(index, 0, tiles.size() - 1);
        return tiles.get(index).active;
    }
    public boolean isImageBlank(int xIndex, int yIndex) {
        int index = (World.tilesPerChunk * yIndex) + xIndex;
        index = MathUtils.clamp(index, 0, tiles.size() - 1);
        if(tiles.get(index).terrainData.data.length() == 0) return true;
        else return false;
    }
    public String getTileName(int x, int y) {
        int index = (World.tilesPerChunk * y) + x;
        index = MathUtils.clamp(index, 0, tiles.size() - 1);
        return tiles.get(index).tileName;
    }
    public ArrayList<StringUtils> getSerializedTerrain() {
        ArrayList<StringUtils> retVal = new ArrayList<StringUtils>();
        for(Tile tile: tiles) {
            retVal.add(tile.terrainData);
        }
        return retVal;
    }

    //These are utility functions for mapping coordinates =================================================
    public static myPair<Integer, Integer> makeKeyFromPixel(int x, int y) {
        int chunkX = x / (World.tileSize * World.tilesPerChunk);
        int chunkY = y / (World.tileSize * World.tilesPerChunk);
        return new myPair(chunkX, chunkY);
    }


    public void setPixel(int x, int y, String color) {

        int tileX = (x / World.tileSize) % World.tilesPerChunk;
        int tileY = (y / World.tileSize) % World.tilesPerChunk;
        int pixelX = x % World.tileSize;
        int pixelY = World.tileSize - 1 - y % World.tileSize;

        int index = (tileY * World.tilesPerChunk) + tileX;
        if(index < 0 || index > tiles.size() - 1) return;

        char colorC = Pixel.getCharFromType(color);
        int colorI = Pixel.charToColor(colorC, pixelX, pixelY);

        Pixel.insertPixel(tiles.get(index).terrainData, pixelX, pixelY, colorC);
        tiles.get(index).image.drawPixel(pixelX, pixelY, colorI);
    }


    //this is only sfasnfsdfasdfsadf

    /*

    private String name;
    private boolean active;
    private Pixmap image;
    private ArrayList<String> serializedObjects;


    public Chunk() {
        name = "";
        active = false;
        image = new Pixmap(0, 0, Pixmap.Format.RGB888);
        serializedObjects = new ArrayList<String>();
    }
    public boolean isImageEmpty() {
        if(image.getWidth() == 0 && image.getHeight() == 0) return true;
        else return false;
    }
    public boolean isImageBlank() {

        for(int y = 0; y < image.getHeight(); y++) {
            for(int x = 0; x < image.getWidth(); x++) {
                if(image.getPixel(x, y) != 0)
                    return false;
            }
        }

        return true;
    }
    public void createImage(int width, int height) {
        width = MathUtils.clamp(width, 0, 100);
        height = MathUtils.clamp(height, 0, 100);
        deleteImage();
        image = new Pixmap(width, height, Pixmap.Format.RGB888);
    }

    //getters
    public Pixmap getImage() { return image; }
    public String getName() { return name; }
    public boolean getActive() { return active; }
    public ArrayList<String> getObjects() { return serializedObjects; }

    //setters
    public void setName(String newName) { name = newName; }
    public void setActive(boolean newActive) { active = newActive; }
    public void setImage(Pixmap newImage) { image = newImage; }
    public void deleteImage() { image.dispose(); }
    public void addObject(String newObj) { serializedObjects.add(newObj); }

    //ADD THESE LATER ========================================
    public void setPixel() {}
    public int getPixel() {return 0;}

     */
}

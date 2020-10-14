package gameCode.Infrastructure;

import com.badlogic.gdx.graphics.Pixmap;
import java.util.ArrayList;

import gameCode.Utilities.myMath;
import gameCode.Utilities.Pixel;
import gameCode.Utilities.myString;
import gameCode.Utilities.myPair;

public class Chunk {


    private String chunkName;
    private class Tile {
        public Pixmap image;
        public myString terrainData;
        public boolean active;
        public String tileName;
        public Tile(int index) {
            image = new Pixmap(myWorld.get().tileSize, myWorld.get().tileSize, Pixmap.Format.RGB888);
            terrainData = new myString("");
            active = false;

            myString newName = new myString("[type: tile][chunkX: ][chunkY: ][tileX: ][tileY: ]");
            newName.setField("chunkX", myString.getField(chunkName, "chunkX"));
            newName.setField("chunkY", myString.getField(chunkName, "chunkY"));
            newName.setField( "tileX", myString.toString(index % myWorld.get().tilesPerChunk));
            newName.setField("tileY", myString.toString(index / myWorld.get().tilesPerChunk));
            tileName = newName.data;
        }
    }

    private ArrayList<Tile> tiles;

    public Chunk(myPair<Integer, Integer> key) {
        chunkName = "[type: chunk][chunkX: " + myString.toString(key.first) + "][chunkY: " + myString.toString(key.second) + "]";
        tiles = new ArrayList<Tile>();
    }

    //Setters =============================================================================================
    public void setTerrain(myString terrainData) {
        ArrayList<myString> tileStrArr = myString.getBeforeChar(terrainData.data, '\n');
        for(int index = 0; index < tileStrArr.size(); index++) {
            Tile newTile = new Tile(index);
            newTile.terrainData = tileStrArr.get(index);
            newTile.image = Pixel.stringToImage(tileStrArr.get(index));
            tiles.add(newTile);
        }
    }
    public void setActive(int x, int y, boolean newActive) {
        int index = (myWorld.get().tilesPerChunk * y) + x;
        index = myMath.clamp(index, 0, tiles.size() - 1);
        tiles.get(index).active = newActive;
    }

    //Getters =============================================================================================
    public String getChunkName() { return chunkName; }
    public Pixmap getImage(int x, int y) {
        int index = (myWorld.get().tilesPerChunk * y) + x;
        index = myMath.clamp(index, 0, tiles.size() - 1);
        return tiles.get(index).image;
    }
    public boolean getActive(int x, int y) {
        int index = (myWorld.get().tilesPerChunk * y) + x;
        index = myMath.clamp(index, 0, tiles.size() - 1);
        return tiles.get(index).active;
    }
    public boolean isImageBlank(int xIndex, int yIndex) {
        int index = (myWorld.get().tilesPerChunk * yIndex) + xIndex;
        index = myMath.clamp(index, 0, tiles.size() - 1);


        if(tiles.get(index).terrainData.data.length() == 0) return true;


        //this shouldn't need to be here
        char emptyChar = Pixel.getCharFromType("empty");
        for(int x = 0; x < tiles.get(index).terrainData.data.length(); x++) {
            char c = tiles.get(index).terrainData.data.charAt(x);
            if(c != emptyChar) return false;

        }


        return true;
    }
    public String getTileName(int x, int y) {
        int index = (myWorld.get().tilesPerChunk * y) + x;
        index = myMath.clamp(index, 0, tiles.size() - 1);
        return tiles.get(index).tileName;
    }
    public myString getSerializedTerrain() {
        myString retVal = new myString("");
        for(Tile tile: tiles) {
            retVal.data += tile.terrainData.data + "\n";
        }
        return retVal;
    }

    public boolean isRegionEmpty(int xTile, int yTile, int xStart, int yStart, int w, int h) {

        if(isImageBlank(xTile, yTile)) return true;

        int index = (yTile * myWorld.get().tilesPerChunk) + xTile;
        if(index < 0 || index > tiles.size() - 1) return true;

        char emptyChar = Pixel.getCharFromType("empty");
        if(tiles.get(index).terrainData.data.length() == 1
           && tiles.get(index).terrainData.data.charAt(0) == emptyChar) return true;

        for(int y = yStart; y < yStart + h; y++) {
        for(int x = xStart; x < xStart + w; x++) {
            int pixIndex = ((myWorld.get().tileSize - 1 - y) * myWorld.get().tileSize) + x;
            if(tiles.get(index).terrainData.data.charAt(pixIndex) != emptyChar)
                return false;
        }}

        return true;
    }

    //These are utility functions for mapping coordinates =================================================
    public static myPair<Integer, Integer> makeKeyFromPixel(int x, int y) {
        int chunkX = x / (myWorld.get().tileSize * myWorld.get().tilesPerChunk);
        int chunkY = y / (myWorld.get().tileSize * myWorld.get().tilesPerChunk);
        return new myPair(chunkX, chunkY);
    }

    public void setPixel(int x, int y, String color) {

        int tileX = (x / myWorld.get().tileSize) % myWorld.get().tilesPerChunk;
        int tileY = (y / myWorld.get().tileSize) % myWorld.get().tilesPerChunk;
        int pixelX = x % myWorld.get().tileSize;
        int pixelY = y % myWorld.get().tileSize;

        int index = (tileY * myWorld.get().tilesPerChunk) + tileX;
        if(index < 0 || index > tiles.size() - 1) return;

        char colorC = Pixel.getCharFromType(color);
        int colorI = Pixel.charToColor(colorC, pixelX, pixelY);

        Pixel.insertPixel(tiles.get(index).terrainData, pixelX, pixelY, colorC);
        tiles.get(index).image.drawPixel(pixelX, 59 - pixelY, colorI);
    }

    public String getPixel(int x, int y) {
        int tileX = (x / myWorld.get().tileSize) % myWorld.get().tilesPerChunk;
        int tileY = (y / myWorld.get().tileSize) % myWorld.get().tilesPerChunk;
        int pixelX = x % myWorld.get().tileSize;
        int pixelY = 59 - (y % myWorld.get().tileSize);

        int tileIndex = (tileY * myWorld.get().tilesPerChunk) + tileX;
        if(tileIndex < 0 || tileIndex > tiles.size() - 1) return "empty";


        char colorC;
        int pixelIndex = (pixelY * myWorld.get().tileSize) + pixelX;
        pixelIndex = myMath.clamp(pixelIndex, 0, tiles.get(tileIndex).terrainData.data.length() - 1);
        colorC = tiles.get(tileIndex).terrainData.data.charAt(pixelIndex);


        String type = Pixel.getTypeFromChar(colorC);
        return type;
    }

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

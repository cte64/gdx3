package gameCode.Terrain;

import com.badlogic.gdx.math.Vector2;
import gameCode.Infastructure.FileSystem;
import gameCode.Infastructure.InputAL;
import gameCode.Infastructure.World;
import gameCode.Utilities.*;
import gameCode.Terrain.ScatterTerrain;
import gameCode.Terrain.Perlin;

import java.security.KeyStore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MakeWorld {

    private int lowestPoint;
    private int layerC;
    private int stretch;
    private int scale;
    private boolean fillIt;
    private char terrainType;
    private int minDepth = 1000;
    private int worldRadius;
    private Map< String, ArrayList<Vector2> > rims;

    String directory;
    int numChunks;
    ArrayList<StringUtils> messages;


    private void cleanUpRims() {

        char emptyChar = Pixel.getCharFromType("empty");

        //ASSIGN EACH COORDINATE TO ITS APPROPRIATE CHUNKGRID INDEX ============
        for (Map.Entry<String, ArrayList<Vector2>> rim: rims.entrySet()) {

            ArrayList< ArrayList<Vector2> > chunkGrid = new ArrayList< ArrayList<Vector2> >();

            for (int x = 0; x < World.getNumChunks() * World.getNumChunks(); x++)
                chunkGrid.add(   new ArrayList<Vector2>()   );

            for (Vector2 coord : rim.getValue()) {

                int xChunk = (int)((coord.x * World.getNumChunks()) / World.getNumPixels());
                int yChunk = (int)((coord.y * World.getNumChunks()) / World.getNumPixels());

                int index = (yChunk * World.getNumChunks()) + xChunk;
                index = MathUtils.clamp(index, 0, chunkGrid.size() - 1);
                chunkGrid.get(index).add(coord);
            }

            //GET RID OF RIM COORDINATES THAT REPRESENT EMPTY SPACE ========================================
            for (int yChunk = 0; yChunk < World.getNumChunks(); yChunk++) {
            for (int xChunk = 0; xChunk < World.getNumChunks(); xChunk++) {

                StringUtils data = new StringUtils("");
                String fileName = "[type: terrain][xChunk: ][yChunk: ]";
                StringUtils.setField(fileName, "xChunk", StringUtils.toString(xChunk));
                StringUtils.setField(fileName, "yChunk", StringUtils.toString(yChunk));
                FileSystem.getFile(new StringUtils(fileName), data);
                String[] tiles = data.data.split("\n");

                int index = (yChunk * World.getNumChunks()) + xChunk;
                index = MathUtils.clamp(index, 0, chunkGrid.size() - 1);

                for(Vector2 i: chunkGrid.get(index)) {

                    int xTile = (int)((i.x / World.tileSize) % World.tilesPerChunk);
                    int yTile = (int)((i.y / World.tileSize) % World.tilesPerChunk);

                    int tileIndex = (yTile * World.tilesPerChunk) + xTile;
                    tileIndex = MathUtils.clamp(tileIndex, 0, tiles.length - 1);

                    if (tiles[tileIndex].length() == 1 && tiles[tileIndex].charAt(0) != emptyChar) continue;

                    int pixelX = (int)(i.x % World.tileSize);
                    int pixelY = (int)(i.y % World.tileSize);

                    int pixIndex = (pixelY * World.tileSize) + pixelX;
                    pixIndex = MathUtils.clamp(pixIndex, 0, tiles[tileIndex].length() - 1);

                    if (rim.getValue().size() == 0) break;
                    if (tiles[tileIndex].charAt(pixIndex) == emptyChar) {
                        rim.getValue().remove(i);
                    }
                }
            }}
        }
    }

    /*
    private void addPixelsToGame(ArrayList<Vector2> coords, String type) {

        char terrainChar = Pixel.getCharFromType(type);
        char emptyChar = Pixel.getCharFromType("empty");

        //CREATE A CHUNKGRID ===================================================
        ArrayList< ArrayList<Vector2> > chunkGrid = new ArrayList< ArrayList<Vector2> >();
        for (int x = 0; x < World.getNumChunks() * World.getNumChunks(); x++)
            chunkGrid.add( new ArrayList<Vector2>() );

        //ASSIGN EACH COORDINATE TO ITS APPROPRIATE CHUNKGRID INDEX ============
        for(Vector2 coord: coords) {

            int xPos = (int)coord.x;
            int yPos = (int)coord.y;

            int xChunk = (xPos * World.getNumChunks()) / World.getNumPixels();
            int yChunk = (yPos * World.getNumChunks()) / World.getNumPixels();

            int index = yChunk * World.getNumChunks() + xChunk;
            index = MathUtils.clamp(index, 0, chunkGrid.size() - 1);

            chunkGrid.get(index).add(coord);
        }

        //PLACE TERRAIN INTO GAMECHUNKS ========================================
        for (int yChunk = 0; yChunk < World.getNumChunks(); yChunk++) {
        for (int xChunk = 0; xChunk < World.getNumChunks(); xChunk++) {

            StringUtils data = new StringUtils("");
            StringUtils fileName = new StringUtils("[type: terrain][xChunk: ][yChunk: ]");
            StringUtils.setField(fileName, "xChunk", StringUtils.toString(xChunk));
            StringUtils.setField(fileName, "yChunk", StringUtils.toString(yChunk));

            FileSystem.getFile(fileName, data);
            ArrayList<StringUtils> tiles = StringUtils.splitToArr(data.data, "\n");

            int index = yChunk * World.getNumChunks() + xChunk;
            index = MathUtils.clamp(index, 0, chunkGrid.size() - 1);

            for (int z = 0; z < chunkGrid.get(index).size(); z++) {

                int xPosPixel = (int)chunkGrid.get(index).get(z).x;
                int yPosPixel = (int)chunkGrid.get(index).get(z).y;

                int xTile = (xPosPixel / World.tileSize) % World.tilesPerChunk;
                int yTile = (yPosPixel / World.tileSize) % World.tilesPerChunk;

                int xPixel = xPosPixel % World.tileSize;
                int yPixel = yPosPixel % World.tileSize;

                int tileIndex = (yTile * World.tilesPerChunk) + xTile;
                tileIndex = MathUtils.clamp(tileIndex, 0, tiles.size() - 1);

                if (tiles.get(tileIndex).data.length() > 0) {

                    int pixIndex = yPixel * World.tileSize + xPixel;

                    if ((tiles.get(tileIndex).data.length() == 1 && tiles.get(tileIndex).data.charAt(0) != emptyChar))
                        Pixel.insertPixel(tiles.get(tileIndex), xPixel, yPixel, terrainChar);

                    else if (pixIndex < tiles.get(tileIndex).data.length())
                        Pixel.insertPixel(tiles.get(tileIndex), xPixel, yPixel, terrainChar);
                }
            }

            StringUtils updatedChunk = new StringUtils("");

            for (int x = 0; x < tiles.size(); x++) updatedChunk.data += tiles.get(x).data + "\n";
            StringUtils name = new StringUtils( "[type: chunk][xChunk: " + StringUtils.toString(xChunk) + "][yChunk: " + StringUtils.toString(yChunk) + "]");
            FileSystem.setFile(name, updatedChunk);
        }}

    }

     */

    private void fillTerrain(ArrayList<Float> newTotal, String rimName) {

        if(rimName != "") rims.put(rimName, new ArrayList<Vector2>());

        int centerX = World.getNumPixels()/2;
        int centerY = World.getNumPixels()/2;

        for(int yChunk = 0; yChunk < World.getNumChunks(); yChunk++) {
        for(int xChunk = 0; xChunk < World.getNumChunks(); xChunk++) {

            StringUtils data = new StringUtils("");
            StringUtils fileName = new StringUtils("[type: chunk][xChunk: ][yChunk: ]");
            StringUtils.setField(fileName, "xChunk", StringUtils.toString(xChunk));
            StringUtils.setField(fileName, "yChunk", StringUtils.toString(yChunk));
            FileSystem.getFile(fileName, data);

            ArrayList<StringUtils> tiles = StringUtils.getBeforeChar(data.data, '\n');

            for(int yTile = 0; yTile < World.tilesPerChunk; yTile++) {
            for(int xTile = 0; xTile < World.tilesPerChunk; xTile++) {

                int yPos = (yChunk * World.tilesPerChunk * World.tileSize) + (yTile * World.tileSize);
                int xPos = (xChunk * World.tilesPerChunk * World.tileSize) + (xTile * World.tileSize);

                int mTL = (int)MathUtils.mag(centerX, centerY, xPos, yPos);
                int mTR = (int)MathUtils.mag(centerX, centerY, xPos + World.tileSize, yPos);
                int mBL = (int)MathUtils.mag(centerX, centerY, xPos, yPos + World.tileSize);
                int mBR = (int)MathUtils.mag(centerX, centerY, xPos + World.tileSize, yPos + World.tileSize);

                int tileIndex = (yTile * World.tilesPerChunk) + xTile;
                tileIndex = MathUtils.clamp(tileIndex, 0, tiles.size() - 1);

                int highestPoint = lowestPoint + stretch;
                int chunkPoint = lowestPoint - 60;

                if( mTL < chunkPoint && mTR < chunkPoint && mBL < chunkPoint && mBR < chunkPoint ) { tiles.set(tileIndex, new StringUtils("" + terrainType)); }

                if( (mTL <= highestPoint && mTL >= chunkPoint) || (mTR <= highestPoint && mTR >= chunkPoint) ||
                         (mBL <= highestPoint && mBL >= chunkPoint) || (mBR <= highestPoint && mBR >= chunkPoint)) {

                    for(int yPixel = 0; yPixel < World.tileSize; yPixel++) {
                    for(int xPixel = 0; xPixel < World.tileSize; xPixel++) {


                        int yPix = (yChunk * World.tilesPerChunk * World.tileSize) + (yTile * World.tileSize) + yPixel;
                        int xPix = (xChunk * World.tilesPerChunk * World.tileSize) + (xTile * World.tileSize) + xPixel;
                        int pixelIndex = ((59 - yPixel ) * World.tileSize) + xPixel;

                        int pixMag = (int)MathUtils.mag(centerX, centerY, xPix, yPix);
                        float angle = MathUtils.angleBetweenCells(centerX, centerY, xPix, yPix);
                        int adjX = (int)((angle / 360.0)*layerC);

                        //capture the outermost pixel =======================================================================
                        if (rims.containsKey(rimName) && pixMag == newTotal.get(adjX).intValue())
                            rims.get(rimName).add(new Vector2(xPix, yPix));

                        char pix = Pixel.getCharFromType("empty");
                        if(pixMag < lowestPoint) pix = terrainType;
                        else if( pixMag < newTotal.get(adjX)) pix = terrainType;
                        else {
                            if(tiles.get(tileIndex).data.length() == 1) pix = tiles.get(tileIndex).data.charAt(0);
                            if(tiles.get(tileIndex).data.length() == World.tileSize * World.tileSize) pix = tiles.get(tileIndex).data.charAt(pixelIndex);
                        }

                        Pixel.insertPixel(tiles.get(tileIndex), xPixel, yPixel, pix);
                    }}

                }

                tiles.get(tileIndex).data += "\n";
            }}

            StringUtils updatedChunk = new StringUtils("");
            for(int x = 0; x < tiles.size(); x++) { updatedChunk.data += tiles.get(x).data; }
            StringUtils name = new StringUtils("[type: chunk][xChunk: " + StringUtils.toString(xChunk) + "][yChunk: " + StringUtils.toString(yChunk) + "]");
            FileSystem.setFile(name, updatedChunk);
        }}
    }

    public MakeWorld(String newDirectory, int numChunks, int tRadius, ArrayList<StringUtils> loadingMessages) {
        directory = newDirectory;
        this.numChunks = numChunks;
        worldRadius = tRadius;
        this.messages = loadingMessages;
    }

    public void start() {

        //INITIALIZE WORLD ==========================================
        World.createWorld(numChunks);
        FileSystem.createGameDirectory(directory);
        messages.add( new StringUtils("World Initialized"));

        //INITIALIZE RIMS ============================================
        rims = new HashMap< String, ArrayList<Vector2> >();
        messages.add( new StringUtils("Rims Initialized"));

        //FILL METADATA FILE ========================================
        StringUtils data = new StringUtils("[worldName: ][numChunks: ][dateCreated: ]");
        data.setField(data, "numChunks", StringUtils.toString(numChunks));
        data.setField(data, "worldName", directory);
        data.setField(data, "dateCreated", Misc.getDate());
        FileSystem.setFile(new StringUtils("[type: metadata]"), data);
        messages.add( new StringUtils("MetaData File Created"));

        //CREATE THE MAIN CHARACTER =================================
        StringUtils heroData = new StringUtils("[type: hero][subType: testHero][details: ][xPos: 2400][yPos: 2400][inven0.0: woodenAxe.1][inven0.1: wooden Pickaxe.1]");
        FileSystem.setFile(new StringUtils("[type: hero]"), heroData);
        messages.add( new StringUtils("Created Hero"));

        //MAKE THE SOLID LAYERS =====================================
        //makeLayer((int)(worldRadius * 1.00), 400, 10, "dirt", true, "[name: outer]");
        //messages.add( new StringUtils("Dirt Created"));


        /*

        makeLayer((int)(worldRadius * 0.85), 200, 10, "clay", true, "");
        messages.add( new StringUtils("Clay Created"));

        makeLayer((int)(worldRadius * 0.82), 150, 10, "sand", true, "");
        messages.add( new StringUtils("Sand Created"));

        makeLayer((int)(worldRadius * 0.78), 400, 10, "stone", true, "");
        messages.add( new StringUtils("Stone Created"));

        makeLayer((int)(worldRadius * 0.30), 200, 10, "coal", true, "");
        messages.add( new StringUtils("Coal Created"));

         */
    }

    void makeLayer(int newLowest, int newStretch, int newOctaves, String newType, boolean newFillIt, String rimName){

        lowestPoint = newLowest;
        stretch = newStretch;
        fillIt = newFillIt;
        layerC = (int)((lowestPoint + stretch) * 2.0f * MathUtils.PI);
        terrainType = Pixel.getCharFromType(newType);

        ArrayList<Float> newTotal = new ArrayList<Float>();
        Perlin perlin = new Perlin(newLowest, layerC, newOctaves, newStretch, newTotal);

        fillTerrain(newTotal, rimName);
    }

    void makeMap(String directory, int numChunks, int tRadius, StringUtils message, StringUtils loadingBar) {}

}

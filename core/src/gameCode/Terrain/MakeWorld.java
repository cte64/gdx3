package gameCode.Terrain;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Engine;
import gameCode.Infrastructure.myWorld;
import gameCode.Utilities.*;

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
    ArrayList<myString> messages;


    private void cleanUpRims() {

        char emptyChar = Pixel.getCharFromType("empty");

        //ASSIGN EACH COORDINATE TO ITS APPROPRIATE CHUNKGRID INDEX ============
        for (Map.Entry<String, ArrayList<Vector2>> rim: rims.entrySet()) {

            ArrayList< ArrayList<Vector2> > chunkGrid = new ArrayList< ArrayList<Vector2> >();

            for (int x = 0; x < myWorld.get().getNumChunks() * myWorld.get().getNumChunks(); x++)
                chunkGrid.add(   new ArrayList<Vector2>()   );

            for (Vector2 coord : rim.getValue()) {

                int xChunk = (int)((coord.x * myWorld.get().getNumChunks()) / myWorld.get().getNumPixels());
                int yChunk = (int)((coord.y * myWorld.get().getNumChunks()) / myWorld.get().getNumPixels());

                int index = (yChunk * myWorld.get().getNumChunks()) + xChunk;
                index = myMath.clamp(index, 0, chunkGrid.size() - 1);
                chunkGrid.get(index).add(coord);
            }

            //GET RID OF RIM COORDINATES THAT REPRESENT EMPTY SPACE ========================================
            for (int yChunk = 0; yChunk < myWorld.get().getNumChunks(); yChunk++) {
            for (int xChunk = 0; xChunk < myWorld.get().getNumChunks(); xChunk++) {

                myString data = new myString("");
                myString fileName = new myString("[type: terrain][xChunk: ][yChunk: ]");
                fileName.setField("xChunk", myString.toString(xChunk));
                fileName.setField( "yChunk", myString.toString(yChunk));
                Engine.get().getFileSystem().getFile(new myString(fileName.data), data);
                String[] tiles = data.data.split("\n");

                int index = (yChunk * myWorld.get().getNumChunks()) + xChunk;
                index = myMath.clamp(index, 0, chunkGrid.size() - 1);

                for(Vector2 i: chunkGrid.get(index)) {

                    int xTile = (int)((i.x / myWorld.get().tileSize) % myWorld.get().tilesPerChunk);
                    int yTile = (int)((i.y / myWorld.get().tileSize) % myWorld.get().tilesPerChunk);

                    int tileIndex = (yTile * myWorld.get().tilesPerChunk) + xTile;
                    tileIndex = myMath.clamp(tileIndex, 0, tiles.length - 1);

                    if (tiles[tileIndex].length() == 1 && tiles[tileIndex].charAt(0) != emptyChar) continue;

                    int pixelX = (int)(i.x % myWorld.get().tileSize);
                    int pixelY = (int)(i.y % myWorld.get().tileSize);

                    int pixIndex = (pixelY * myWorld.get().tileSize) + pixelX;
                    pixIndex = myMath.clamp(pixIndex, 0, tiles[tileIndex].length() - 1);

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
        for (int x = 0; x < World.get().getNumChunks() * World.get().getNumChunks(); x++)
            chunkGrid.add( new ArrayList<Vector2>() );

        //ASSIGN EACH COORDINATE TO ITS APPROPRIATE CHUNKGRID INDEX ============
        for(Vector2 coord: coords) {

            int xPos = (int)coord.x;
            int yPos = (int)coord.y;

            int xChunk = (xPos * World.get().getNumChunks()) / World.get().getNumPixels();
            int yChunk = (yPos * World.get().getNumChunks()) / World.get().getNumPixels();

            int index = yChunk * World.get().getNumChunks() + xChunk;
            index = MathUtils.clamp(index, 0, chunkGrid.size() - 1);

            chunkGrid.get(index).add(coord);
        }

        //PLACE TERRAIN INTO GAMECHUNKS ========================================
        for (int yChunk = 0; yChunk < World.get().getNumChunks(); yChunk++) {
        for (int xChunk = 0; xChunk < World.get().getNumChunks(); xChunk++) {

            StringUtils data = new StringUtils("");
            StringUtils fileName = new StringUtils("[type: terrain][xChunk: ][yChunk: ]");
            StringUtils.setField(fileName, "xChunk", StringUtils.toString(xChunk));
            StringUtils.setField(fileName, "yChunk", StringUtils.toString(yChunk));

            FileSystem.getFile(fileName, data);
            ArrayList<StringUtils> tiles = StringUtils.splitToArr(data.data, "\n");

            int index = yChunk * World.get().getNumChunks() + xChunk;
            index = MathUtils.clamp(index, 0, chunkGrid.size() - 1);

            for (int z = 0; z < chunkGrid.get(index).size(); z++) {

                int xPosPixel = (int)chunkGrid.get(index).get(z).x;
                int yPosPixel = (int)chunkGrid.get(index).get(z).y;

                int xTile = (xPosPixel / World.get().tileSize) % World.get().tilesPerChunk;
                int yTile = (yPosPixel / World.get().tileSize) % World.get().tilesPerChunk;

                int xPixel = xPosPixel % World.get().tileSize;
                int yPixel = yPosPixel % World.get().tileSize;

                int tileIndex = (yTile * World.get().tilesPerChunk) + xTile;
                tileIndex = MathUtils.clamp(tileIndex, 0, tiles.size() - 1);

                if (tiles.get(tileIndex).data.length() > 0) {

                    int pixIndex = yPixel * World.get().tileSize + xPixel;

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

        int centerX = myWorld.get().getNumPixels()/2;
        int centerY = myWorld.get().getNumPixels()/2;

        for(int yChunk = 0; yChunk < myWorld.get().getNumChunks(); yChunk++) {
        for(int xChunk = 0; xChunk < myWorld.get().getNumChunks(); xChunk++) {

            myString data = new myString("");
            myString fileName = new myString("[type: chunk][xChunk: ][yChunk: ]");
            fileName.setField( "xChunk", myString.toString(xChunk));
            fileName.setField( "yChunk", myString.toString(yChunk));
            Engine.get().getFileSystem().getFile(fileName, data);

            ArrayList<myString> tiles = myString.getBeforeChar(data.data, '\n');

            for(int yTile = 0; yTile < myWorld.get().tilesPerChunk; yTile++) {
            for(int xTile = 0; xTile < myWorld.get().tilesPerChunk; xTile++) {

                int yPos = (yChunk * myWorld.get().tilesPerChunk * myWorld.get().tileSize) + (yTile * myWorld.get().tileSize);
                int xPos = (xChunk * myWorld.get().tilesPerChunk * myWorld.get().tileSize) + (xTile * myWorld.get().tileSize);

                int mTL = (int) myMath.mag(centerX, centerY, xPos, yPos);
                int mTR = (int) myMath.mag(centerX, centerY, xPos + myWorld.get().tileSize, yPos);
                int mBL = (int) myMath.mag(centerX, centerY, xPos, yPos + myWorld.get().tileSize);
                int mBR = (int) myMath.mag(centerX, centerY, xPos + myWorld.get().tileSize, yPos + myWorld.get().tileSize);

                int tileIndex = (yTile * myWorld.get().tilesPerChunk) + xTile;
                tileIndex = myMath.clamp(tileIndex, 0, tiles.size() - 1);

                int highestPoint = lowestPoint + stretch;
                int chunkPoint = lowestPoint - 60;

                if( mTL < chunkPoint && mTR < chunkPoint && mBL < chunkPoint && mBR < chunkPoint ) { tiles.set(tileIndex, new myString("" + terrainType)); }

                if( (mTL <= highestPoint && mTL >= chunkPoint) || (mTR <= highestPoint && mTR >= chunkPoint) ||
                         (mBL <= highestPoint && mBL >= chunkPoint) || (mBR <= highestPoint && mBR >= chunkPoint)) {

                    for(int yPixel = 0; yPixel < myWorld.get().tileSize; yPixel++) {
                    for(int xPixel = 0; xPixel < myWorld.get().tileSize; xPixel++) {


                        int yPix = (yChunk * myWorld.get().tilesPerChunk * myWorld.get().tileSize) + (yTile * myWorld.get().tileSize) + yPixel;
                        int xPix = (xChunk * myWorld.get().tilesPerChunk * myWorld.get().tileSize) + (xTile * myWorld.get().tileSize) + xPixel;
                        int pixelIndex = ((59 - yPixel ) * myWorld.get().tileSize) + xPixel;

                        int pixMag = (int) myMath.mag(centerX, centerY, xPix, yPix);
                        float angle = myMath.angleBetweenCells(centerX, centerY, xPix, yPix);
                        int adjX = (int)((angle / 360.0)*layerC);

                        //capture the outermost pixel =======================================================================
                        if (rims.containsKey(rimName) && pixMag == newTotal.get(adjX).intValue())
                            rims.get(rimName).add(new Vector2(xPix, yPix));

                        char pix = Pixel.getCharFromType("empty");
                        if(pixMag < lowestPoint) pix = terrainType;
                        else if( pixMag < newTotal.get(adjX)) pix = terrainType;
                        else {
                            if(tiles.get(tileIndex).data.length() == 1) pix = tiles.get(tileIndex).data.charAt(0);
                            if(tiles.get(tileIndex).data.length() == myWorld.get().tileSize * myWorld.get().tileSize) pix = tiles.get(tileIndex).data.charAt(pixelIndex);
                        }

                        Pixel.insertPixel(tiles.get(tileIndex), xPixel, yPixel, pix);
                    }}

                }

                tiles.get(tileIndex).data += "\n";
            }}

            myString updatedChunk = new myString("");
            for(int x = 0; x < tiles.size(); x++) { updatedChunk.data += tiles.get(x).data; }
            myString name = new myString("[type: chunk][xChunk: " + myString.toString(xChunk) + "][yChunk: " + myString.toString(yChunk) + "]");
            Engine.get().getFileSystem().setFile(name, updatedChunk);
        }}
    }

    public MakeWorld(String newDirectory, int numChunks, int tRadius, ArrayList<myString> loadingMessages) {
        directory = newDirectory;
        this.numChunks = numChunks;
        worldRadius = tRadius;
        this.messages = loadingMessages;
    }

    public void start() {

        //INITIALIZE WORLD ==========================================
        myWorld.get().createWorld(numChunks);
        Engine.get().getFileSystem().createGameDirectory(directory);
        messages.add( new myString("World Initialized"));

        //INITIALIZE RIMS ============================================
        rims = new HashMap< String, ArrayList<Vector2> >();
        messages.add( new myString("Rims Initialized"));

        //FILL METADATA FILE ========================================
        myString data = new myString("[worldName: ][numChunks: ][dateCreated: ]");
        data.setField("numChunks", myString.toString(numChunks));
        data.setField("worldName", directory);
        data.setField("dateCreated", Misc.getDate());
        Engine.get().getFileSystem().setFile(new myString("[type: metadata]"), data);
        messages.add( new myString("MetaData File Created"));

        //CREATE THE MAIN CHARACTER =================================
        myString heroData = new myString("[type: actor][subType: hero][details: ][xPos: 2400][yPos: 2400][inven0.0: woodenAxe.1][inven0.1: wooden Pickaxe.1]");
        Engine.get().getFileSystem().setFile(new myString("[type: hero]"), heroData);
        messages.add( new myString("Created Hero"));

        /*
        //MAKE THE SOLID LAYERS =====================================
        makeLayer((int)(worldRadius * 1.00), 400, 10, "dirt", true, "[name: outer]");
        messages.add( new myString("Dirt Created"));



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
        layerC = (int)((lowestPoint + stretch) * 2.0f * myMath.PI);
        terrainType = Pixel.getCharFromType(newType);

        ArrayList<Float> newTotal = new ArrayList<Float>();
        Perlin perlin = new Perlin(newLowest, layerC, newOctaves, newStretch, newTotal);

        fillTerrain(newTotal, rimName);
    }

    void makeMap(String directory, int numChunks, int tRadius, myString message, myString loadingBar) {}

}

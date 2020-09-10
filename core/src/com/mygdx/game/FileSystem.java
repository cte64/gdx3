package com.mygdx.game;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.Gdx;
import gameCode.Infrastructure.Chunk;
import gameCode.Infrastructure.Entity;
import gameCode.Infrastructure.World;
import gameCode.Utilities.myString;

import gameCode.Utilities.MathUtils;
import gameCode.Utilities.myPair;

import java.util.ArrayList;

public class FileSystem {

    private final String gameSaveDirectory = "core/saves/";
    private String gameSubDirectory;
    private ArrayList<Boolean> chunkUpdate1 = new ArrayList<Boolean>();
    private ArrayList<Boolean> chunkUpdate2 = new ArrayList<Boolean>();

    //these are the boundaries for the active chunk regions ==========================================================
    public int outerLeft, outerRight, outerTop, outerBottom;      //outer ring
    public int middleLeft, middleRight, middleTop, middleBottom;  //middle ring
    public int centerLeft, centerRight, centerTop, centerBottom;  //center ring

    //these functions are for changing the chunk regions =============================================================
    private void center(int xIndex, int yIndex) {

        //center ======================================================================
        centerLeft = xIndex;
        centerRight = centerLeft + 1;
        centerTop = yIndex;
        centerBottom = centerTop + 1;

        centerLeft = MathUtils.clamp(centerLeft, 0, World.get().getNumCells() - 1);
        centerRight = MathUtils.clamp(centerRight, 0, World.get().getNumCells());
        centerTop = MathUtils.clamp(centerTop, 0, World.get().getNumCells() - 1);
        centerBottom = MathUtils.clamp(centerBottom, 0, World.get().getNumCells());

        //middle ======================================================================
        middleLeft = centerLeft - 1;
        middleRight = centerRight + 1;
        middleTop = centerTop - 1;
        middleBottom = centerBottom + 1;

        middleLeft = MathUtils.clamp(middleLeft, 0, World.get().getNumCells() - 3);
        middleRight = MathUtils.clamp(middleRight, 3, World.get().getNumCells());
        middleTop = MathUtils.clamp(middleTop, 0, World.get().getNumCells() - 3);
        middleBottom = MathUtils.clamp(middleBottom, 3, World.get().getNumCells());

        //outer =======================================================================
        outerLeft = centerLeft - 2;
        outerRight = centerRight + 2;
        outerTop = centerTop - 2;
        outerBottom = centerBottom + 2;

        outerLeft = MathUtils.clamp(outerLeft, 0, World.get().getNumCells() - 5);
        outerRight = MathUtils.clamp(outerRight, 5, World.get().getNumCells());
        outerTop = MathUtils.clamp(outerTop, 0, World.get().getNumCells() - 5);
        outerBottom = MathUtils.clamp(outerBottom, 5, World.get().getNumCells());
    }
    private void setUpdate(int x, int y, int select, boolean newVal) {
        if (select == 1) {
            if (chunkUpdate1.size() == 0) return;
            int index = (y * World.get().getNumChunks()) + x;
            index = MathUtils.clamp(index, 0, chunkUpdate1.size() - 1);
            chunkUpdate1.set(index, newVal);
        }
        if (select == 2) {
            if (chunkUpdate2.size() == 0) return;
            int index = (y * World.get().getNumChunks()) + x;
            index = MathUtils.clamp(index, 0, chunkUpdate2.size() - 1);
            chunkUpdate2.set(index, newVal);
        }
    }
    private boolean getUpdate(int x, int y, int select) {

        if (select == 1) {
            if (chunkUpdate1.size() == 0) return false;
            int index = (y * World.get().getNumChunks()) + x;
            index = MathUtils.clamp(index, 0, chunkUpdate1.size() - 1);
            return chunkUpdate1.get(index);
        }

        if (select == 2) {
            if (chunkUpdate2.size() == 0) return false;
            int index = (y * World.get().getNumChunks()) + x;
            index = MathUtils.clamp(index, 0, chunkUpdate2.size() - 1);
            return chunkUpdate2.get(index);
        }

        return false;
    }
    private void updateChunks() {

        //first set all the first one to false
        for(int y = 0; y < World.get().getNumChunks(); y++) {
            for(int x = 0; x < World.get().getNumChunks(); x++)
                setUpdate(x, y, 1, false);
        }

        for(int y = outerTop; y < outerBottom; y++) {
            for(int x = outerLeft; x < outerRight; x++) {
                setUpdate(x, y, 1, true);
            }
        }

        for(int y = 0; y < World.get().getNumChunks(); y++) {
            for(int x = 0; x < World.get().getNumChunks(); x++) {

                if(getUpdate(x, y, 1) && !getUpdate(x, y, 2)) {
                    setUpdate(x, y, 2, true);
                    readChunk(x, y);
                }

                else if(!getUpdate(x, y, 1) && getUpdate(x, y, 2)) {
                    setUpdate(x, y, 2, false);
                    writeChunk(x, y, true);
                }
            }}
    }
    private void writeChunk(int xIndex, int yIndex, boolean deleteCurrent) {

        //create a new key from the xIndex and yIndex
        myPair<Integer, Integer> key = new myPair(xIndex, yIndex);
        Chunk chunkPtr = World.get().getChunk(key);
        if(chunkPtr == null) {
            System.out.println("Write chunk failed because chunk " + xIndex + "." + yIndex + " does not exist");
            return;
        }



        myString chunkFileName = new myString("[type: chunk][xChunk: ][yChunk: ]");

        chunkFileName.setField("xChunk", myString.toString(xIndex));
        chunkFileName.setField("yChunk", myString.toString(yIndex));

        myString terrainData = chunkPtr.getSerializedTerrain();
        setFile(chunkFileName, terrainData);

        /*


        String chunkFileName = gameSaveDirectory + gameSubDirectory + "chunks/" + "chunk-" + StringUtils.toString(xIndex) + "." + StringUtils.toString(yIndex) + ".txt";


        FileHandle chunkFile = Gdx.files.local(chunkFileName);


        if(!chunkFile.exists()) { System.out.println("File: " + chunkFileName + " does not exist!"); return; }

         */




        World.get().deleteChunk(key);
    }
    private void readChunk(int xIndex, int yIndex) {

        //create a new key from the xIndex and yIndex
        myPair<Integer, Integer> key = new myPair(xIndex, yIndex);

        Chunk chunkPtr = World.get().getChunk(key);
        if(chunkPtr == null) {
            Chunk newChunk = new Chunk(key);
            chunkPtr = newChunk;
            World.get().insertChunk(key, newChunk);
        }



        /*
        //String entFileName = gameSaveDirectory + gameSubDirectory + "entities/" + "chunk-" + StringUtils.toString(xIndex) + "." + StringUtils.toString(yIndex) + ".txt";
        //FileHandle entFile = Gdx.files.local(entFileName);
        //if(!entFile.exists()) { System.out.println("File: " + entFileName + " does not exist!"); return; }
        //StringUtils objectStr = new StringUtils(entFile.readString());
         */

        myString chunkFileName = new myString("[type: chunk][xChunk: ][yChunk: ]");
        chunkFileName.setField("xChunk", myString.toString(xIndex));
        chunkFileName.setField("yChunk", myString.toString(yIndex));

        myString terrainData = new myString("");
        getFile(chunkFileName, terrainData);

        chunkPtr.setTerrain(terrainData);




        //chunkPtr.setObjects(objectStr);

        /*
        ArrayList<StringUtils> tileStrArr = StringUtils.getBeforeChar(tileStr, '\n');
        ArrayList<StringUtils> entStrArr =  StringUtils.getBeforeChar(entStr, '\n');

        int leftEdge = xIndex * World.get().tilesPerChunk;
        int rightEdge = leftEdge + World.get().tilesPerChunk;
        int topEdge = yIndex * World.get().tilesPerChunk;
        int bottomEdge = topEdge + World.get().tilesPerChunk;

        for(int y = topEdge; y < bottomEdge; y++) {
        for(int x = leftEdge; x < rightEdge; x++) {

            Chunk chunkPtr = World.get().getChunk(x, y);
            if(chunkPtr == null) continue;

            StringUtils newName = new StringUtils("[type: terrain][subType: terrainBlock][xPos: ][yPos: ]");
            StringUtils.setField(newName, "xPos", StringUtils.toString(x));
            StringUtils.setField(newName, "yPos", StringUtils.toString(y));
            chunkPtr.setName(newName.data);

            int index = (y - topEdge) * World.get().tilesPerChunk + (x - leftEdge);
            if(index < tileStrArr.size() && tileStrArr.get(index).data.length() > 0) {
                Pixmap image = Pixel.stringToImage( tileStrArr.get(index) );
                chunkPtr.setImage( image );
                chunkPtr.setActive(false);
            }
        }}

        for(StringUtils strUtil: entStrArr) {

            if(strUtil.data.length() < 5) continue;

            String xStr = StringUtils.getField(strUtil, "xPos");
            String yStr = StringUtils.getField(strUtil, "yPos");

            int xPos = StringUtils.stringToInt(xStr) / World.get().tileSize;
            int yPos = StringUtils.stringToInt(yStr) / World.get().tileSize;

            xPos = MathUtils.clamp(xPos, 0, World.get().getNumBlocks());
            yPos = MathUtils.clamp(yPos, 0, World.get().getNumBlocks());

            Chunk chunkPtr = World.get().getChunk(xPos, yPos);
            if(chunkPtr != null) chunkPtr.addObject(strUtil.data);
        }

         */
    }

    //public stuff ===================================================================================================
    public FileSystem() {}

    public String getGameSaveDirectory() { return gameSaveDirectory; }
    public String getGameSubDirectory() { return gameSubDirectory;  }
    public void setGameSubDirectory(String newDir) { gameSubDirectory = newDir + "/"; }
    public ArrayList<String> getSaveNames() {

        ArrayList<String> retVal = new ArrayList<String>();
        FileHandle dir = Gdx.files.internal("core/saves/");
        for(FileHandle file: dir.list()) {
            String fileName = file.toString() + "/metadata.txt";

            FileHandle gameFile = Gdx.files.local(fileName);
            if(!gameFile.exists()) {
                System.out.println("File: " + fileName + " does not exist!");
                return retVal;
            }
            String metaData = gameFile.readString();
            retVal.add(metaData);
        }

        return retVal;
    }

    public void deleteDirectory(String directory) {
        FileHandle file = Gdx.files.local(gameSaveDirectory + directory);
        if(file.exists() && file.isDirectory()) file.deleteDirectory();
    }
    public void createGameDirectory(String newDir) {

        gameSubDirectory = newDir + "/";

        //create metadata file
        myString metaName = new myString("[type: metadata]");
        setFile(metaName, new myString("finally got it working"));

        //create the main character and put in a separate file
        myString heroName = new myString("[type: hero]");
        setFile(heroName, new myString("this is hero"));

        //populate the chunks and entities folder
        for (int yChunk = 0; yChunk < World.get().getNumChunks(); yChunk++) {
            for (int xChunk = 0; xChunk < World.get().getNumChunks(); xChunk++) {

                myString chunkStr = new myString("");
                for (int yTile = 0; yTile < World.get().tilesPerChunk; yTile++) {
                    for (int xTile = 0; xTile < World.get().tilesPerChunk; xTile++) {
                        chunkStr.data += "\n";
                    }}

                myString entStr = new myString("");

                myString chunkName = new myString("[type: chunk][xChunk: " + myString.toString(xChunk) + "][yChunk: " + myString.toString(yChunk) + "]");
                setFile(chunkName, chunkStr);

                myString entName = new myString("[type: entity][xChunk: " + myString.toString(xChunk) + "][yChunk: " + myString.toString(yChunk) + "]");
                setFile(entName, entStr);
            }}
    }
    public void saveCurrentChunks() {
        for(int yIndex = outerTop; yIndex < outerBottom; yIndex++) {
        for(int xIndex = outerLeft; xIndex < outerRight; xIndex++) {
            writeChunk(xIndex, yIndex, false);
        }}
    }

    public void setFile(myString filename, myString data) {

        String type = filename.getField("type");
        String name = "";

        if (type.equals("hero")) name = gameSaveDirectory + gameSubDirectory + "hero.txt";
        if (type.equals("metadata")) name = gameSaveDirectory + gameSubDirectory + "metadata.txt";

        if (type.equals("chunk"))  {
            int xIndex = myString.stringToInt(filename.getField("xChunk"));
            int yIndex = myString.stringToInt(filename.getField("yChunk"));
            name = gameSaveDirectory + gameSubDirectory + "chunks/" + "chunk-" + myString.toString(xIndex) + "." + myString.toString(yIndex) + ".txt";
        }
        if (type.equals("entity")) {
            int xIndex = myString.stringToInt(filename.getField("xChunk"));
            int yIndex = myString.stringToInt(filename.getField("yChunk"));
            name = gameSaveDirectory + gameSubDirectory + "entities/" + "chunk-" + myString.toString(xIndex) + "." + myString.toString(yIndex) + ".txt";
        }

        FileHandle file = Gdx.files.local(name);
        file.writeString(data.data, false);
    }
    public void getFile(myString filename, myString data) {

        String type = filename.getField("type");
        String name = "";

        if (type.equals("inventory")) name = "core/gameFiles/InventoryRecipes.txt";
        if (type.equals("hero")) name = gameSaveDirectory + gameSubDirectory + "hero.txt";
        if (type.equals("metadata")) name = gameSaveDirectory + gameSubDirectory + "metadata.txt";
        if (type.equals("chunk")) {
            int xIndex = myString.stringToInt(filename.getField("xChunk"));
            int yIndex = myString.stringToInt(filename.getField("yChunk"));
            name = gameSaveDirectory + gameSubDirectory + "chunks/" + "chunk-" + myString.toString(xIndex) + "." + myString.toString(yIndex) + ".txt";
        }
        if (type.equals("entity")) {
            int xIndex = myString.stringToInt(filename.getField("xChunk"));
            int yIndex = myString.stringToInt(filename.getField("yChunk"));
            name = gameSaveDirectory + gameSubDirectory + "entities/" + "chunk-" + myString.toString(xIndex) + "." + myString.toString(yIndex) + ".txt";
        }

        FileHandle file = Gdx.files.local(name);
        if(!file.exists()) {
            System.out.println("File: " + name + " does not exist!");
            return;
        }

        data.data = file.readString();
    }

    public void init() {

        //outer ring
        outerLeft = 0;
        outerRight = 0;
        outerTop = 0;
        outerBottom = 0;

        //middle ring
        middleLeft = 0;
        middleRight = 0;
        middleTop = 0;
        middleBottom = 0;

        //center ring
        centerLeft = 0;
        centerRight = 0;
        centerTop = 0;
        centerBottom = 0;

        //reinitialize the chunks
        chunkUpdate1.clear();
        chunkUpdate2.clear();

        for (int y = 0; y < World.get().getNumChunks(); y++) {
            for (int x = 0; x < World.get().getNumChunks(); x++) {
                chunkUpdate1.add(false);
                chunkUpdate2.add(false);
            }}
    }
    public void update() {

        Entity hero = World.get().getCamera();
        if(hero == null) return;

        int xIndex = (int)(hero.x_pos * World.get().getNumChunks()) / World.get().getNumPixels();
        int yIndex = (int)(hero.y_pos * World.get().getNumChunks()) / World.get().getNumPixels();

        xIndex = MathUtils.clamp(xIndex, 0, World.get().getNumChunks() - 1);
        yIndex = MathUtils.clamp(yIndex, 0, World.get().getNumChunks() - 1);

        if(xIndex < middleLeft || xIndex >= middleRight || yIndex < middleTop || yIndex >= middleBottom) {
            center(xIndex, yIndex);
            updateChunks();
        }
    }
}

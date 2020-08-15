package gameCode.Infastructure;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import gameCode.Utilities.Pixel;
import gameCode.Utilities.StringUtils;

import gameCode.Utilities.MathUtils;

import java.util.ArrayList;

public class FileSystem {

    private final static String gameSaveDirectory = "core/saves/";
    private static String gameSubDirectory;
    private static ArrayList<Boolean> chunkUpdate1 = new ArrayList<Boolean>();
    private static ArrayList<Boolean> chunkUpdate2 = new ArrayList<Boolean>();

    public static int outerLeft, outerRight, outerTop, outerBottom;      //outer ring
    public static int middleLeft, middleRight, middleTop, middleBottom;  //middle ring
    public static int centerLeft, centerRight, centerTop, centerBottom;  //center ring

    private FileSystem() {}
    public static void init() {

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

        for (int y = 0; y < World.getNumChunks(); y++) {
        for (int x = 0; x < World.getNumChunks(); x++) {
            chunkUpdate1.add(false);
            chunkUpdate2.add(false);
        }}
    }
    public static void center(int xIndex, int yIndex) {

        //center ======================================================================
        centerLeft = xIndex;
        centerRight = centerLeft + 1;
        centerTop = yIndex;
        centerBottom = centerTop + 1;

        centerLeft = MathUtils.clamp(centerLeft, 0, World.getNumCells() - 1);
        centerRight = MathUtils.clamp(centerRight, 0, World.getNumCells());
        centerTop = MathUtils.clamp(centerTop, 0, World.getNumCells() - 1);
        centerBottom = MathUtils.clamp(centerBottom, 0, World.getNumCells());

        //middle ======================================================================
        middleLeft = centerLeft - 1;
        middleRight = centerRight + 1;
        middleTop = centerTop - 1;
        middleBottom = centerBottom + 1;

        middleLeft = MathUtils.clamp(middleLeft, 0, World.getNumCells() - 3);
        middleRight = MathUtils.clamp(middleRight, 3, World.getNumCells());
        middleTop = MathUtils.clamp(middleTop, 0, World.getNumCells() - 3);
        middleBottom = MathUtils.clamp(middleBottom, 3, World.getNumCells());

        //outer =======================================================================
        outerLeft = centerLeft - 2;
        outerRight = centerRight + 2;
        outerTop = centerTop - 2;
        outerBottom = centerBottom + 2;

        outerLeft = MathUtils.clamp(outerLeft, 0, World.getNumCells() - 5);
        outerRight = MathUtils.clamp(outerRight, 5, World.getNumCells());
        outerTop = MathUtils.clamp(outerTop, 0, World.getNumCells() - 5);
        outerBottom = MathUtils.clamp(outerBottom, 5, World.getNumCells());
    }
    public static void setUpdate(int x, int y, int select, boolean newVal) {
        if (select == 1) {
            if (chunkUpdate1.size() == 0) return;
            int index = (y * World.getNumChunks()) + x;
            index = MathUtils.clamp(index, 0, chunkUpdate1.size() - 1);
            chunkUpdate1.set(index, newVal);
        }
        if (select == 2) {
            if (chunkUpdate2.size() == 0) return;
            int index = (y * World.getNumChunks()) + x;
            index = MathUtils.clamp(index, 0, chunkUpdate2.size() - 1);
            chunkUpdate2.set(index, newVal);
        }
    }
    public static boolean getUpdate(int x, int y, int select) {

        if (select == 1) {
            if (chunkUpdate1.size() == 0) return false;
            int index = (y * World.getNumChunks()) + x;
            index = MathUtils.clamp(index, 0, chunkUpdate1.size() - 1);
            return chunkUpdate1.get(index);
        }

        if (select == 2) {
            if (chunkUpdate2.size() == 0) return false;
            int index = (y * World.getNumChunks()) + x;
            index = MathUtils.clamp(index, 0, chunkUpdate2.size() - 1);
            return chunkUpdate2.get(index);
        }

        return false;
    }
    public static void writeChunk(int xIndex, int yIndex, boolean deleteCurrent) {



        /*
        StringOps st;
        int leftEdge = xIndex * world.tilesPerChunk;
        int rightEdge = leftEdge + world.tilesPerChunk;
        int topEdge = yIndex * world.tilesPerChunk;
        int bottomEdge = topEdge + world.tilesPerChunk;

        std::string chunkData = "";
        std::string entData = "";

        for(int y = topEdge; y < bottomEdge; y++) {
            for(int x = leftEdge; x < rightEdge; x++) {

                eryThang* thangPtr = world.getThang(x, y);
                if (thangPtr == nullptr) continue;

                //CHUNKS ============================================================
                std::string chunkString = "";
                if(thangPtr->isImageEmpty()) chunkString = "\n";
                else {
                    imageToString(thangPtr->getImage(), chunkString);
                    st.compressString(chunkString);
                    chunkString += "\n";
                }

                chunkData += chunkString;
                if (deleteCurrent) thangPtr->deleteImage();

                //SERIALIZED ENTITIES ==============================================
                for(auto currentObj: *thangPtr->getObjectVec()) {
                    entData += currentObj + "\n";
                }
                if (deleteCurrent) thangPtr->getObjectVec()->clear();

                //ACTIVE ENTITIES (I don't really care that this is inefficient) ==================================================
                for (auto iter = world.getEntList().begin(); iter != world.getEntList().end(); iter++) {

                    int xPos = (iter->x_pos) / world.tileSize;
                    int yPos = (iter->y_pos) / world.tileSize;

                    if (st.getField(iter->entityName, "type") == "item" && xPos == x && yPos == y) {
                        entData += iter->getSerializedEntity() + "\n";
                    }
                }
            }}

        std::string chunkName = "[type: chunk][xChunk: " + st.toString(xIndex) + "][yChunk: " + st.toString(yIndex) + "]";
        world.fileThing.setFile(chunkName, chunkData);

        std::string entName = "[type: entity][xChunk: " + st.toString(xIndex) + "][yChunk: " + st.toString(yIndex) + "]";
        world.fileThing.setFile(entName, entData);

         */
    }
    public static void readChunk(int xIndex, int yIndex) {

        String chunkFileName = gameSaveDirectory + gameSubDirectory + "chunks/" + "chunk-" + StringUtils.toString(xIndex) + "." + StringUtils.toString(yIndex) + ".txt";
        String entFileName = gameSaveDirectory + gameSubDirectory + "entities/" + "chunk-" + StringUtils.toString(xIndex) + "." + StringUtils.toString(yIndex) + ".txt";
        
        FileHandle chunkFile = Gdx.files.local(chunkFileName);
        FileHandle entFile = Gdx.files.local(entFileName);

        if(!chunkFile.exists()) { System.out.println("File: " + chunkFileName + " does not exist!"); return; }
        if(!entFile.exists()) { System.out.println("File: " + entFileName + " does not exist!"); return; }

        String tileStr = chunkFile.readString();
        String entStr = entFile.readString();

        ArrayList<StringUtils> tileStrArr = StringUtils.getBeforeChar(tileStr, '\n');
        ArrayList<StringUtils> entStrArr =  StringUtils.getBeforeChar(entStr, '\n');

        int leftEdge = xIndex * World.tilesPerChunk;
        int rightEdge = leftEdge + World.tilesPerChunk;
        int topEdge = yIndex * World.tilesPerChunk;
        int bottomEdge = topEdge + World.tilesPerChunk;

        for(int y = topEdge; y < bottomEdge; y++) {
        for(int x = leftEdge; x < rightEdge; x++) {

            Chunk chunkPtr = World.getChunk(x, y);
            if(chunkPtr == null) continue;

            StringUtils newName = new StringUtils("[type: terrain][subType: terrainBlock][xPos: ][yPos: ]");
            StringUtils.setField(newName, "xPos", StringUtils.toString(x));
            StringUtils.setField(newName, "yPos", StringUtils.toString(y));
            chunkPtr.setName(newName.data);

            int index = (y - topEdge) * World.tilesPerChunk + (x - leftEdge);
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

            int xPos = StringUtils.stringToInt(xStr) / World.tileSize;
            int yPos = StringUtils.stringToInt(yStr) / World.tileSize;

            xPos = MathUtils.clamp(xPos, 0, World.getNumBlocks());
            yPos = MathUtils.clamp(yPos, 0, World.getNumBlocks());

            Chunk chunkPtr = World.getChunk(xPos, yPos);
            if(chunkPtr != null) chunkPtr.addObject(strUtil.data);
        }
    }

    public static void updateChunks() {

        //first set all the first one to false
        for(int y = 0; y < World.getNumChunks(); y++) {
            for(int x = 0; x < World.getNumChunks(); x++)
                setUpdate(x, y, 1, false);
        }

        for(int y = outerTop; y < outerBottom; y++) {
            for(int x = outerLeft; x < outerRight; x++) {
                setUpdate(x, y, 1, true);
            }
        }

        for(int y = 0; y < World.getNumChunks(); y++) {
        for(int x = 0; x < World.getNumChunks(); x++) {

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
    public static void update() {

        Entity hero = World.getCamera();
        if(hero == null) return;

        int xIndex = (int)(hero.x_pos * World.getNumChunks()) / World.getNumPixels();
        int yIndex = (int)(hero.y_pos * World.getNumChunks()) / World.getNumPixels();

        xIndex = MathUtils.clamp(xIndex, 0, World.getNumChunks() - 1);
        yIndex = MathUtils.clamp(yIndex, 0, World.getNumChunks() - 1);

        if(xIndex < middleLeft || xIndex >= middleRight || yIndex < middleTop || yIndex >= middleBottom) {
            center(xIndex, yIndex);
            updateChunks();
        }
    }
    public static String getGameSaveDirectory() { return gameSaveDirectory; }
    public static String getGameSubDirectory() { return gameSubDirectory;  }
    public static void setGameSubDirectory(String newDir) { gameSubDirectory = newDir + "/"; }
    public static void deleteDirectory(String directory) {
        FileHandle file = Gdx.files.local(gameSaveDirectory + directory);
        if(file.exists() && file.isDirectory()) file.deleteDirectory();
    }
    public static void setFile(StringUtils filename, StringUtils data) {

        String type = StringUtils.getField(filename, "type");
        String name = "";

        if (type.equals("hero")) name = gameSaveDirectory + gameSubDirectory + "hero.txt";
        if (type.equals("metadata")) name = gameSaveDirectory + gameSubDirectory + "metadata.txt";

        if (type.equals("chunk"))  {
            int xIndex = StringUtils.stringToInt(StringUtils.getField(filename, "xChunk"));
            int yIndex = StringUtils.stringToInt(StringUtils.getField(filename, "yChunk"));
            name = gameSaveDirectory + gameSubDirectory + "chunks/" + "chunk-" + StringUtils.toString(xIndex) + "." + StringUtils.toString(yIndex) + ".txt";
        }
        if (type.equals("entity")) {
            int xIndex = StringUtils.stringToInt(StringUtils.getField(filename, "xChunk"));
            int yIndex = StringUtils.stringToInt(StringUtils.getField(filename, "yChunk"));
            name = gameSaveDirectory + gameSubDirectory + "entities/" + "chunk-" + StringUtils.toString(xIndex) + "." + StringUtils.toString(yIndex) + ".txt";
        }

        FileHandle file = Gdx.files.local(name);
        file.writeString(data.data, false);
    }
    public static void getFile(StringUtils filename, StringUtils data) {

        String type = StringUtils.getField(filename, "type");
        String name = "";

        if (type.equals("hero")) name = gameSaveDirectory + gameSubDirectory + "hero.txt";
        if (type.equals("metadata")) name = gameSaveDirectory + gameSubDirectory + "metadata.txt";
        if (type.equals("chunk")) {
            int xIndex = StringUtils.stringToInt(StringUtils.getField(filename, "xChunk"));
            int yIndex = StringUtils.stringToInt(StringUtils.getField(filename, "yChunk"));
            name = gameSaveDirectory + gameSubDirectory + "chunks/" + "chunk-" + StringUtils.toString(xIndex) + "." + StringUtils.toString(yIndex) + ".txt";
        }
        if (type.equals("entity")) {
            int xIndex = StringUtils.stringToInt(StringUtils.getField(filename, "xChunk"));
            int yIndex = StringUtils.stringToInt(StringUtils.getField(filename, "yChunk"));
            name = gameSaveDirectory + gameSubDirectory + "entities/" + "chunk-" + StringUtils.toString(xIndex) + "." + StringUtils.toString(yIndex) + ".txt";
        }

        FileHandle file = Gdx.files.local(name);
        if(!file.exists()) {
            System.out.println("File: " + name + " does not exist!");
            return;
        }

        data.data = file.readString();
    }
    public static void createGameDirectory(String newDir) {

        gameSubDirectory = newDir + "/";

        //create metadata file
        StringUtils metaName = new StringUtils("[type: metadata]");
        setFile(metaName, new StringUtils("finally got it working"));

        //create the main character and put in a separate file
        StringUtils heroName = new StringUtils("[type: hero]");
        setFile(heroName, new StringUtils("this is hero"));

        //populate the chunks and entities folder
        for (int yChunk = 0; yChunk < World.getNumChunks(); yChunk++) {
        for (int xChunk = 0; xChunk < World.getNumChunks(); xChunk++) {

            StringUtils chunkStr = new StringUtils("");
            for (int yTile = 0; yTile < World.tilesPerChunk; yTile++) {
            for (int xTile = 0; xTile < World.tilesPerChunk; xTile++) {
                chunkStr.data += "\n";
            }}

            StringUtils entStr = new StringUtils("");

            StringUtils chunkName = new StringUtils("[type: chunk][xChunk: " + StringUtils.toString(xChunk) + "][yChunk: " + StringUtils.toString(yChunk) + "]");
            setFile(chunkName, chunkStr);

            StringUtils entName = new StringUtils("[type: entity][xChunk: " + StringUtils.toString(xChunk) + "][yChunk: " + StringUtils.toString(yChunk) + "]");
            setFile(entName, entStr);
        }}
    }
    public static ArrayList<String> getSaveNames() {

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

}

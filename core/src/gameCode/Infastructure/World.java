package gameCode.Infastructure;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.mygdx.game.myGame;
import gameCode.Living.HeroInput;
import gameCode.Utilities.StringUtils;
import gameCode.Utilities.MathUtils;

public class World {

    //Make Sure that this cant be instantiated
    private World() {}

    //Stores the gameState ===============================
    private static String currentState;

    //Declare constants ==================================
    public static final int xCell = 10;
    public static final int tileSize = 60;
    public static final int tilesPerChunk = 20;

    //These values change based on the size of the game ==
    private static int numChunks;
    private static int numBlocks;
    private static int numPixels;
    private static int numCells;

    //ViewPort parameters ================================
    public static int viewPortWidth = 600;
    public static int viewPortHeight = 600;

    //Add and delete entities =====================================================================
    public static ArrayList<Entity> entitiesToBeAdded = new ArrayList<Entity>();
    public static ArrayList<Entity> entitiesToBeDeleted = new ArrayList<Entity>();

    //These are the different data structures that reference the game entities
    private static LinkedList<Entity> entList = new LinkedList<Entity>();
    private static HashMap<String, Entity> entByName = new HashMap<String, Entity>();
    private static ArrayList<Chunk> chunks = new ArrayList<Chunk>();
    private static ArrayList<Boolean> containsMoveables = new ArrayList<Boolean>();
    private static ArrayList< ArrayList<Entity> > locatorCells = new ArrayList<  ArrayList<Entity> >();
    private static ArrayList<Entity> entitiesOrderedByLayer = new ArrayList<Entity>();
    private static class FrameStruct { int width, height, left, right, top, bottom; Entity ent; };
    private static ArrayList<FrameStruct> frames = new ArrayList<FrameStruct>();

    //Entity that controls the viewport ========================================================================
    private static Entity camera = null;

    //Getters  =============================================================================================
    public static int getNumChunks() { return numChunks; }
    public static int getNumPixels() { return numPixels; }
    public static int getNumBlocks() { return numBlocks; }
    public static int getNumCells() { return numCells; }
    public static int getViewPortWidth() { return viewPortWidth; }
    public static int getViewPortHeight() { return viewPortHeight; }
    public static LinkedList<Entity> getEntList() { return entList; }
    public static Entity getCamera() { return camera; }
    public static ArrayList<Entity> getLocatorCell(int x, int y) {
        if(locatorCells.size() == 0) return null;
        int index = (y * numCells) + x;
        index = MathUtils.clamp(index, 0, locatorCells.size() - 1);
        return locatorCells.get(index);
    }
    public static Boolean getMoveable(int y, int x) {
        if(containsMoveables.size() == 0) return false;
        int index = (y * numCells) + x;
        index = MathUtils.clamp(index, 0, containsMoveables.size() - 1);
        return containsMoveables.get(index);
    }
    public static Chunk getChunk(int x, int y) {
        int index = (numBlocks * y) + x;
        if(chunks.size() == 0 || index < 0 || index > chunks.size() - 1) return null;
        else return chunks.get(index);
    }
    public static String getCurrentState() { return currentState; }

    //Setters ==============================================================================================
    public static void setMoveable(int y, int x, boolean newBool) {
        if (containsMoveables.size() == 0) return;
        int index = (y * numCells) + x;
        index = MathUtils.clamp(index, 0, containsMoveables.size() - 1);
        containsMoveables.set(index, newBool);
    }
    public static void setCurrentState(String newState) { currentState = newState; }

    //Modify World State ===========================================================================================
    public static void init() {

        currentState = "init";

        //Set the viewport size and update the screen ===================
        Entity ent = new Entity();
        ent.x_pos = 100;
        ent.y_pos = 100;
        ent.spriteName = "tile";
        ent.addComponent(new HeroInput());
        entList.add(ent);
        camera = ent;

        Entity ent1 = new Entity();
        ent1.x_pos = 200;
        ent1.y_pos = 100;
        ent1.spriteName = "tile";
        entList.add(ent1);




    }
    public static void createWorld(int newChunks) {

        numChunks = newChunks;
        numBlocks = numChunks * tilesPerChunk;
        numPixels = numBlocks * tileSize;
        numCells = numBlocks / xCell;

        chunks.clear();
        containsMoveables.clear();
        locatorCells.clear();
        entitiesOrderedByLayer.clear();


        for (int y = 0; y < numCells; y++) {
        for (int x = 0; x < numCells; x++) {
            locatorCells.add(new ArrayList<Entity>());
            containsMoveables.add(false);
        }}

        for (int y = 0; y < numBlocks; y++) {
        for (int x = 0; x < numBlocks; x++) {

            StringUtils name = new StringUtils("[type: terrain][subType: terrainBlock][xPos: ][yPos: ]");
            StringUtils.setField(name, "xPos", StringUtils.toString(x));
            StringUtils.setField(name, "yPos", StringUtils.toString(y));

            Chunk t = new Chunk();
            t.setName(name.data);
            t.setActive(false);
            chunks.add(t);
        }}

        FileSystem.init();
    }
    public static void deleteWorld() {

        for(Entity ent: entList) { entitiesToBeDeleted.add(ent); }
        chunks.clear();
        containsMoveables.clear();
        entByName.clear();
        locatorCells.clear();
        frames.clear();

        numChunks = 0;
        numBlocks = 0;
        numPixels = 0;
        numCells =  0;
        camera = null;

        FileSystem.init();
    }

    //Loading and Unloading ========================================================================
    public static void setEdge() {

        for(FrameStruct frame: frames) {
            if(frame.ent != null) {
                int width, height;
                if(frame.width == 0 && frame.height == 0) {
                    width = (int)Math.ceil((viewPortWidth/tileSize)/2.0);
                    height = (int)Math.ceil((viewPortHeight / tileSize)/2.0);
                }
                else {
                    width = frame.width;
                    height = frame.height;
                }

                int left = ((int)frame.ent.x_pos / tileSize) - width - 1;
                if(left < 0) left = 0;
                int right = left + 2 * width + 4;
                int top = ((int)frame.ent.y_pos) / tileSize - height - 1;
                if(top < 0) top = 0;
                int bottom = top + 2 * height + 4;

                frame.left = left;
                frame.right = right;
                frame.bottom = bottom;
                frame.top = top;
            }
        }

    }
    public static void cleanUp() {}
    public static void loadEntities() {}
    public static void update() {}

    //Sifting Frame ===============================================================
    public static void addSiftingFrame(Entity ent, int newWidth, int newHeight) {
        FrameStruct newFrame = new FrameStruct();
        newFrame.width = newWidth;
        newFrame.height = newHeight;
        newFrame.ent = ent;
        frames.add(newFrame);
    }
}

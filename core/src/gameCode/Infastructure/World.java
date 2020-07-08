package gameCode.Infastructure;

import java.awt.*;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import com.badlogic.gdx.math.Vector2;
import jdk.internal.net.http.common.Pair;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.mygdx.game.myGame;
import gameCode.Living.HeroInput;
import gameCode.Utilities.Coordinates;
import gameCode.Utilities.StringUtils;
import gameCode.Utilities.MathUtils;
import jdk.internal.net.http.common.Pair;
import gameCode.Infastructure.MakeEntity;

public class World {

    //Make Sure that this cant be instantiated
    private World() {}

    //Stores the gameState ==========================================================================
    private static String currentState = "init";

    //Declare constants =============================================================================
    public static final int xCell = 10;
    public static final int tileSize = 60;
    public static final int tilesPerChunk = 20;

    //These values change based on the size of the game ============================================
    private static int numChunks;
    private static int numBlocks;
    private static int numPixels;
    private static int numCells;

    //ViewPort parameters =========================================================================
    private static int viewPortWidth = 600;
    private static int viewPortHeight = 600;

    //Time between frame ==========================================================================
    private static float deltaTime = 0.0f;

    //Add and delete entities =====================================================================
    public static ArrayList<Entity> entitiesToBeAdded = new ArrayList<Entity>();
    public static ArrayList<Entity> entitiesToBeDeleted = new ArrayList<Entity>();

    //These are the different data structures that reference the game entities =====================
    private static LinkedList<Entity> entList = new LinkedList<Entity>();
    private static HashMap<String, Entity> entByName = new HashMap<String, Entity>();
    private static ArrayList< ArrayList<Entity> > locatorCells = new ArrayList<  ArrayList<Entity> >();
    private static ArrayList<Chunk> chunks = new ArrayList<Chunk>();
    private static ArrayList<Boolean> containsMoveables = new ArrayList<Boolean>();
    private static class FrameStruct { int width, height, left, right, top, bottom; Entity ent; };
    private static ArrayList<FrameStruct> frames = new ArrayList<FrameStruct>();

    //Entity that controls the viewport ============================================================
    private static Entity camera = null;

    //GETTERS =======================================================================================
    public static int getNumChunks() { return numChunks; }
    public static int getNumPixels() { return numPixels; }
    public static int getNumBlocks() { return numBlocks; }
    public static int getNumCells() { return numCells; }
    public static int getViewPortWidth() { return viewPortWidth; }
    public static int getViewPortHeight() { return viewPortHeight; }
    public static float getDeltaTime() { return deltaTime; }
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
    public static Entity getEntByName(String name) {
        if(entByName.containsKey(name)) return entByName.get(name);
        else return null;
    }
    public static Entity getEntByLocation(int x, int y) {

        int newX = ((x * numCells) / numPixels);
        int newY = ((y * numCells) / numPixels);

        ArrayList<Entity> cellPtr = getLocatorCell(newX, newY);
        if(cellPtr == null) return null;

        for(Entity ent: cellPtr) {
            Vector2 c = Coordinates.getPoint2(x, y, ent);
            if (c.x >= ent.x_pos && c.x < ent.x_pos + ent.getWidth() && c.y >= ent.y_pos && c.y < ent.y_pos + ent.getHeight()) {
                return ent;
            }
        }
        return null;
    }

    //SETTERS ======================================================================================
    public static void setMoveable(int x, int y, boolean newBool) {
        if (containsMoveables.size() == 0) return;
        int index = (y * numCells) + x;
        index = MathUtils.clamp(index, 0, containsMoveables.size() - 1);
        containsMoveables.set(index, newBool);
    }
    public static void setCurrentState(String newState) { currentState = newState; }
    public static void setDeltaTime(float newDelta) { deltaTime = newDelta; }
    public static void setViewPortWidth(int newWidth) { viewPortWidth = newWidth; }
    public static void setViewPortHeight(int newHeight) { viewPortHeight = newHeight; }
    public static void addSiftingFrame(Entity ent, int newWidth, int newHeight) {
        FrameStruct newFrame = new FrameStruct();
        newFrame.width = newWidth;
        newFrame.height = newHeight;
        newFrame.ent = ent;
        frames.add(newFrame);
    }
    public static void setCamera(Entity newCamera) { camera = newCamera; }
    public static void positionEntity(Entity ent) {
        ArrayList<Vector2> corner_coords = Coordinates.getLocatorCellCoord(ent);
        for(Vector2 coord: corner_coords) {
            int x = (int)coord.x;
            int y = (int)coord.y;
            ArrayList<Entity> cellPtr = getLocatorCell(x, y);
            if(cellPtr != null) {
                cellPtr.add(ent);
                if(ent.moveable) setMoveable(x, y, true);
            }
        }
    }

    //Modify World State ===========================================================================
    public static void init() {

        currentState = "init";
        createWorld(10);

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

        Entity test = new Entity();
        test.height = 40;
        test.width = 20;
        test.x_pos = 100;
        test.y_pos = 100;
        test.angle = 0;

        for(int x = 0; x < 100; x++) {
            test.x_pos += 12;
            ArrayList<Vector2> coords = Coordinates.getLocatorCellCoord(test);
            for(Vector2 a: coords) {
                System.out.println(x + " )_) " + a.x + " : " + a.y);
            }
            System.out.println();
        }
    }
    public static void createWorld(int newChunks) {

        numChunks = newChunks;
        numBlocks = numChunks * tilesPerChunk;
        numPixels = numBlocks * tileSize;
        numCells = numBlocks / xCell;

        chunks.clear();
        containsMoveables.clear();
        locatorCells.clear();


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
    public static void update() {

        //update the state ========================================================
        if(currentState != State.getState()) {
            State.loadState();
            State.setState( currentState);
        }

        //delete stuff ============================================================
        for(Entity ent: entitiesToBeDeleted) {
            if(ent == null) continue;

            if( StringUtils.getField(ent.entityName, "type") == "terrain") Graphics.availCoords();
            ent.markForDeletion = true;

            entByName.remove(ent);
            ArrayList<Vector2> corner_coords = Coordinates.getLocatorCellCoord(ent);
            for(Vector2 coord: corner_coords) {
                ArrayList<Entity> cellPtr = getLocatorCell((int)coord.x, (int)coord.y);
                if(cellPtr != null) cellPtr.remove(ent);
            }
            entList.remove(ent);
        }
        entitiesToBeDeleted.clear();

        //add stuff =================================================================
        for(Entity ent: entitiesToBeAdded) {
            entList.add(ent);
            entByName.put(ent.entityName, ent);
            positionEntity(ent);
        }
        entitiesToBeAdded.clear();
    }
    private static void cleanUp() {

        if (camera == null) return;
        for (FrameStruct frame : frames) {

            int a = 3;
            int left_edge = frame.left - a;
            int right_edge = frame.right + a;
            int bottom_edge = frame.bottom + a;
            int top_edge = frame.top - a;

            left_edge = MathUtils.clamp(left_edge, 0, numBlocks - 1);
            right_edge = MathUtils.clamp(right_edge, 0, numBlocks - 1);
            top_edge = MathUtils.clamp(top_edge, 0, numBlocks - 1);
            bottom_edge = MathUtils.clamp(bottom_edge, 0, numBlocks - 1);

            for (Entity ent : entList) {
                Vector2 aXY = Coordinates.getPoint2(ent.x_pos + ent.getWidth() / 2, ent.y_pos + ent.getHeight() / 2, frame.ent);
                int aX = (int) aXY.x;
                int aY = (int) aXY.y;
                boolean mark = false;

                if (ent.deleteRange == -2) continue;
                else if (ent.deleteRange == -1) {
                    if (aX < left_edge * tileSize || aX > right_edge * tileSize || aY < top_edge * tileSize || aY > bottom_edge * tileSize)
                        mark = true;
                } else if (ent.deleteRange >= 0) {

                    int dist = (int) MathUtils.mag(ent.x_pos + ent.getWidth() / 2,
                            ent.y_pos + ent.getHeight() / 2,
                            camera.x_pos + camera.getWidth() / 2,
                            camera.y_pos + camera.getHeight() / 2);

                    if (dist > ent.deleteRange) {
                        mark = true;
                    }
                }

                Chunk chunkPtr = getChunk(ent.bitMapX, ent.bitMapY);
                if (chunkPtr == null) continue;
                if (!mark && StringUtils.getField(ent.entityName, "type") == "terrain" && chunkPtr.isImageEmpty())
                    mark = true;

                if (mark) {
                    Chunk chunkPtr2 = getChunk((int) (ent.x_pos / tileSize), (int) (ent.y_pos / tileSize));
                    if (chunkPtr2 != null) chunkPtr2.setActive(false);
                    entitiesToBeDeleted.add(ent);
                    if (StringUtils.getField(ent.entityName, "type") != "terrain") chunkPtr2.addObject(ent.entityName);
                }
            }
        }
    }

    public static void loadEntities() {



        for(FrameStruct frame: frames) {
            if(frame.ent == null) continue;


            for(int y = frame.top; y < frame.bottom; y++) {
            for (int x = frame.left; x < frame.right; x++) {


                //ArrayList<Vector2> tPoint = Coordinates.getPoint2(x*tileSize, y*tileSize, frame.ent);

            }}
        }


        /*
        Coordinates c;
        std::pair<float, float> tPoint = c.getPoint2(x*tileSize, y*tileSize, thisEnt);
        int tX = tPoint.first/tileSize;
        int tY = tPoint.second/tileSize;

        clamp(tX, 0, numBlocks - 1);
        clamp(tY, 0, numBlocks - 1);

        eryThang* thangPtr = getThang(tX, tY);
        if (thangPtr == nullptr) continue;

        if(!thangPtr->getActive()) {

            if (!thangPtr->isImageEmpty() || thangPtr->getObjectVec()->size() > 0) {
                std::string ent_name = thangPtr->getName();

                //do the terrain thing ===============================================
                if(getEntByName(ent_name) == nullptr) {
                    Entity ent = makeIt.getEntity(ent_name, *thangPtr->getImage());
                    ent.x_pos = tX*tileSize;
                    ent.y_pos = tY*tileSize;
                    ent.bitMapX = tX;
                    ent.bitMapY = tY;
                    entitiesToBeAdded.push_back(ent);
                }

                //do the entities ====================================================
                while (thangPtr->getObjectVec()->size() > 0) {

                    std::string entName = thangPtr->getObjectVec()->back();

                    std::string xStr = st.getField(entName, "xPos");
                    std::string yStr = st.getField(entName, "yPos");

                    int xPos = st.stringToInt(xStr);
                    int yPos = st.stringToInt(yStr);

                    clamp(xPos, 0, numPixels - 1);
                    clamp(yPos, 0, numPixels - 1);

                    Entity ent = makeIt.getEntity(entName, sf::Image());
                    ent.x_pos = xPos;
                    ent.y_pos = yPos;
                    entitiesToBeAdded.push_back(ent);

                    thangPtr->getObjectVec()->pop_back();
                }

                thangPtr->setActive(true);
            }
        }

         */
    }

    /*
    void loadEntities();
     */
}
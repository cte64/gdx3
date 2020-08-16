package gameCode.Infastructure;

import java.awt.*;
import java.nio.ByteBuffer;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.Vector2;
import gameCode.Terrain.MakeWorld;
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
    private static int viewPortWidth = 1000;
    private static int viewPortHeight = 700;

    //Time between frame ==========================================================================
    private static float deltaTime = 0.0f;

    //Add and delete entities =====================================================================
    public static ArrayList<Entity> entitiesToBeAdded = new ArrayList<Entity>();
    public static ArrayList<Entity> entitiesToBeDeleted = new ArrayList<Entity>();

    //These are the different data structures that reference the game entities =====================
    private static LinkedList<Entity> entList = new LinkedList<Entity>();
    private static HashMap<String, Entity> entByName = new HashMap<String, Entity>();
    private static ArrayList<Entity> entByZIndex = new ArrayList<Entity>();
    private static HashMap<Vector2, Chunk> chunks = new HashMap<Vector2, Chunk>();
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
    public static Chunk getChunk(Vector2 key) {
        if(chunks.containsKey(key)) return chunks.get(key);
        else return null;
    }
    public static String getCurrentState() { return currentState; }
    public static Entity getEntByName(String name) {
        if(entByName.containsKey(name)) return entByName.get(name);
        else return null;
    }

    public static ArrayList<Entity> getEntByZIndex() { return entByZIndex; }

    public static HashMap<Vector2, Chunk> getChunkMap() { return chunks; }

    //SETTERS ======================================================================================
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
    public static void insertChunk(Vector2 key, Chunk newChunk) { chunks.put(key, newChunk); }
    public static void deleteChunk(Vector2 key) { chunks.remove(key); }

    private static void positionByZIndex(Entity ent) {

        if(entByZIndex.size() == 0) {
            entByZIndex.add(ent);
            return;
        }

        int index = 0;
        for(int x = 0; x <entByZIndex.size(); x++) {
            if(ent.z_pos < entByZIndex.get(x).z_pos) break;
            index = x + 1;
        }

        entByZIndex.add(index, ent);
    }

    //Modify World State ===========================================================================
    public static void init() {
        currentState = "mainMenu";
    }
    public static void createWorld(int newChunks) {

        numChunks = newChunks;
        numBlocks = numChunks * tilesPerChunk;
        numPixels = numBlocks * tileSize;
        numCells = numBlocks / xCell;

        chunks.clear();
        FileSystem.init();
    }
    public static void deleteWorld() {

        for(Entity ent: entList) { entitiesToBeDeleted.add(ent); }
        chunks.clear();
        entByName.clear();
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

        //print stuff =============================================================
        //for(Entity ent: entitiesToBeAdded) { System.out.println("Added: " + ent.entityName); }
        //for(Entity ent: entitiesToBeDeleted) { System.out.println("Deleted: " + ent.entityName); }

        //delete stuff ============================================================
        for(Entity ent: entitiesToBeDeleted) {
            if(ent == null) continue;
            if( StringUtils.getField(ent.entityName, "type").equals( "terrain") ) { Graphics.returnCoord(ent.spriteName); }
            ent.markForDeletion = true;

            entByName.remove(ent.entityName);
            entByZIndex.remove(ent);
            entList.remove(ent);
        }
        entitiesToBeDeleted.clear();

        //add stuff =================================================================
        for(Entity ent: entitiesToBeAdded) {
            entList.add(ent);
            entByName.put(ent.entityName, ent);
            positionByZIndex(ent);
        }
        entitiesToBeAdded.clear();
    }
    public static void cleanUp() {

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
                int left = left_edge * tileSize;
                int right = right_edge * tileSize;
                int top = top_edge * tileSize;
                int bottom = bottom_edge * tileSize;
                boolean mark = false;

                if (ent.deleteRange == -2) continue;
                if (ent.deleteRange == -1 && (aX < left || aX > right || aY < top || aY > bottom) )  {
                    mark = true;
                    //System.out.println("we got here and a 134");

                }


                if (ent.deleteRange >= 0) {
                    int dist = (int) MathUtils.mag(ent.x_pos + ent.getWidth() / 2,
                            ent.y_pos + ent.getHeight() / 2,
                            camera.x_pos + camera.getWidth() / 2,
                            camera.y_pos + camera.getHeight() / 2);
                    if (dist > ent.deleteRange) mark = true;
                }


                Vector2 key = new Vector2(ent.bitMapX, ent.bitMapY);
                Chunk chunkPtr = getChunk(key);
                if (chunkPtr == null) continue;
                /*

                 */







                //if it is a terrain item and it is empty delete it
                //if (!mark && StringUtils.getField(ent.entityName, "type") == "chunk" && chunkPtr.isImageBlank(ent.bitMapX, ent.bitMapY)) mark = true;

                /*

                 */
                if (mark) {





                    Vector2 key2 = new Vector2( (int)(ent.x_pos / tileSize), (int)(ent.y_pos / tileSize));
                    Chunk chunkPtr2 = getChunk(key2);



                    System.out.println("FERRY CORSTEN 222 ");
                    /*



                    if(chunkPtr2 == null) continue;
                    chunkPtr2.setActive((int)key.x, (int)key.y, false);
                    entitiesToBeDeleted.add(ent);

                     */

                }

            }
        }


    }
    public static void loadEntities() {


              /*
                        //do the entities =======================================================
                        while (chunkPtr.getObjects().size() > 0) {

                            String entName = chunkPtr.getObjects().get( chunkPtr.getObjects().size() - 1 );

                            String xStr = StringUtils.getField(entName, "xPos");
                            String yStr = StringUtils.getField(entName, "yPos");

                            int xPos = StringUtils.stringToInt(xStr);
                            int yPos = StringUtils.stringToInt(yStr);

                            xPos = MathUtils.clamp(xPos, 0, numPixels - 1);
                            yPos = MathUtils.clamp(yPos, 0, numPixels - 1);

                            Entity ent = MakeEntity.getEntity(entName);
                            ent.x_pos = xPos;
                            ent.y_pos = yPos;
                            entitiesToBeAdded.add(ent);

                            chunkPtr.getObjects().remove( chunkPtr.getObjects().size() - 1 );
                        }
                         */

        for(FrameStruct frame: frames) {

            if(frame.ent == null) continue;

            for (int y = frame.top; y < frame.bottom; y++) {
            for (int x = frame.left; x < frame.right; x++) {

                Vector2 tPoint = Coordinates.getPoint2(x*tileSize, y*tileSize, frame.ent);

                int tileXAbs = (int)(tPoint.x/tileSize);
                int tileYAbs = (int)(tPoint.y/tileSize);

                int tileXRel = tileXAbs % tilesPerChunk;
                int tileYRel = tileYAbs % tilesPerChunk;

                int chunkX = (int)(tPoint.x / (tileSize * tilesPerChunk));
                int chunkY = (int)(tPoint.y / (tileSize * tilesPerChunk));

                Vector2 key = new Vector2(chunkX, chunkY);
                Chunk chunkPtr = getChunk(key);
                if(chunkPtr == null) continue;


                if(!chunkPtr.getActive(tileXRel, tileYRel) && !chunkPtr.isImageBlank(tileXRel, tileYRel)) {

                    String ent_name = chunkPtr.getTileName(tileXRel, tileYRel);

                    //do the terrain thing ==================================================
                    if(getEntByName(ent_name) == null) {
                        Entity ent = MakeEntity.getEntity(ent_name, chunkPtr.getImage(tileXRel, tileYRel));
                        ent.entityName = ent_name;
                        ent.x_pos = tileXAbs*tileSize;
                        ent.y_pos = tileYAbs*tileSize;
                        ent.bitMapX = tileXAbs;
                        ent.bitMapY = tileYAbs;
                        ent.deleteRange = -1;
                        entitiesToBeAdded.add(ent);
                    }

                    chunkPtr.setActive(tileXRel, tileYRel, true);
                }
            }}
        }
    }
}
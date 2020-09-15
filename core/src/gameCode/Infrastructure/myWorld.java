package gameCode.Infrastructure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Engine;
import gameCode.Utilities.myPair;

import gameCode.Utilities.Coordinates;
import gameCode.Utilities.myString;
import gameCode.Utilities.myMath;

public class myWorld {

    //Single instance ===============================================================================
    private static myWorld worldInstance = null;
    private myWorld() {
    }
    public static myWorld get() {
        if(worldInstance == null)
            worldInstance = new myWorld();
        return worldInstance;
    }


    //Declare constants =============================================================================
    public final int xCell = 10;
    public final int tileSize = 60;
    public final int tilesPerChunk = 20;

    //These values change based on the size of the game ============================================
    private int numChunks;
    private int numBlocks;
    private int numPixels;
    private int numCells;

    //ViewPort parameters =========================================================================
    private int viewPortWidth = 800;
    private int viewPortHeight = 600;

    //Time between frame ==========================================================================
    private float deltaTime = 0.0f;

    //Add and delete entities =====================================================================
    public ArrayList<Entity> entitiesToBeAdded = new ArrayList<Entity>();
    public ArrayList<Entity> entitiesToBeDeleted = new ArrayList<Entity>();

    //These are the different data structures that reference the game entities =====================
    private LinkedList<Entity> entList = new LinkedList<Entity>();
    private HashMap<String, Entity> entByName = new HashMap<String, Entity>();
    private ArrayList<Entity> entByZIndex = new ArrayList<Entity>();
    private HashMap<myPair<Integer, Integer>, Chunk> chunks = new HashMap<myPair<Integer, Integer>, Chunk>();
    private class FrameStruct { int width, height, left, right, top, bottom; Entity ent; };
    private ArrayList<FrameStruct> frames = new ArrayList<FrameStruct>();

    //Entity that controls the viewport ============================================================
    private Entity camera = null;

    //GETTERS =======================================================================================
    public int getNumChunks() { return numChunks; }
    public int getNumPixels() { return numPixels; }
    public int getNumBlocks() { return numBlocks; }
    public int getNumCells() { return numCells; }
    public int getViewPortWidth() { return viewPortWidth; }
    public int getViewPortHeight() { return viewPortHeight; }
    public float getDeltaTime() { return deltaTime; }
    public LinkedList<Entity> getEntList() { return entList; }
    public Entity getCamera() { return camera; }
    public Chunk getChunk(myPair<Integer, Integer> key) {
        if(chunks.containsKey(key)) return chunks.get(key);
        else return null;
    }
    public Entity getEntByName(String name) {
        if(entByName.containsKey(name)) return entByName.get(name);
        else return null;
    }
    public ArrayList<Entity> getEntByZIndex() { return entByZIndex; }
    public HashMap<myPair<Integer, Integer>, Chunk> getChunkMap() { return chunks; }
    public ArrayList<Entity> getEntByLocation(int x, int y) {

        //will rewrite this later
        ArrayList<Entity> ents = new ArrayList<Entity>();

        for(Entity ent: entList) {
            if(x > ent.x_pos && x < ent.x_pos + ent.getWidth() &&
               y > ent.y_pos && y < ent.y_pos + ent.getHeight())
                ents.add(ent);
        }

        return ents;
    }

    //SETTERS ======================================================================================
    public void setDeltaTime(float newDelta) { deltaTime = newDelta; }
    public void setViewPortWidth(int newWidth) { viewPortWidth = newWidth; }
    public void setViewPortHeight(int newHeight) { viewPortHeight = newHeight; }
    public void addSiftingFrame(Entity ent, int newWidth, int newHeight) {
        FrameStruct newFrame = new FrameStruct();
        newFrame.width = newWidth;
        newFrame.height = newHeight;
        newFrame.ent = ent;
        frames.add(newFrame);
    }
    public void setCamera(Entity newCamera) { camera = newCamera; }
    public void insertChunk(myPair<Integer, Integer> key, Chunk newChunk) {
        chunks.put(key, newChunk);
    }
    public void deleteChunk(myPair<Integer, Integer> key) { chunks.remove(key); }
    private void positionByZIndex(Entity ent) {

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
    public void init() {
        //State.mainMenu();
    }
    public void createWorld(int newChunks) {

        numChunks = newChunks;
        numBlocks = numChunks * tilesPerChunk;
        numPixels = numBlocks * tileSize;
        numCells = numBlocks / xCell;

        chunks.clear();
        Engine.get().getFileSystem().init();
    }
    public void deleteWorld() {

        for(Entity ent: entList) { entitiesToBeDeleted.add(ent); }
        chunks.clear();
        entByName.clear();
        frames.clear();

        numChunks = 0;
        numBlocks = 0;
        numPixels = 0;
        numCells =  0;
        camera = null;

        Engine.get().getFileSystem().init();
    }

    //Loading and Unloading ========================================================================
    public void setEdge() {

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
    public void update() {


        //print stuff =============================================================
        //for(Entity ent: entitiesToBeAdded) { System.out.println("Added: " + ent.entityName); }
        //for(Entity ent: entitiesToBeDeleted) { System.out.println("Deleted: " + ent.entityName); }

        //delete stuff ============================================================
        for(Entity ent: entitiesToBeDeleted) {
            if(ent == null) continue;
            if( myString.getField(ent.entityName, "type").equals( "tile") ) { Engine.get().getAssets().returnCoord(ent.spriteName); }
            ent.markForDeletion = true;


           // Engine.get().getPhysics().subtractBody(ent);




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
    public void cleanUp() {

        if (camera == null) return;
        for (FrameStruct frame : frames) {

            int a = 3;
            int left_edge = frame.left - a;
            int right_edge = frame.right + a;
            int bottom_edge = frame.bottom + a;
            int top_edge = frame.top - a;

            left_edge = myMath.clamp(left_edge, 0, numBlocks - 1);
            right_edge = myMath.clamp(right_edge, 0, numBlocks - 1);
            top_edge = myMath.clamp(top_edge, 0, numBlocks - 1);
            bottom_edge = myMath.clamp(bottom_edge, 0, numBlocks - 1);

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
                if (ent.deleteRange == -1 && (aX < left || aX > right || aY < top || aY > bottom)) mark = true;
                if (ent.deleteRange >= 0) {
                    int dist = (int) myMath.mag(ent.x_pos + ent.getWidth() / 2,
                            ent.y_pos + ent.getHeight() / 2,
                            camera.x_pos + camera.getWidth() / 2,
                            camera.y_pos + camera.getHeight() / 2);
                    if (dist > ent.deleteRange) mark = true;
                }

                int chunkX = (int) (ent.x_pos / (tileSize * tilesPerChunk));
                int chunkY = (int) (ent.y_pos / (tileSize * tilesPerChunk));
                int tileX = (int) (ent.x_pos / tileSize) % tilesPerChunk;
                int tileY = (int) (ent.y_pos / tileSize) % tilesPerChunk;

                myPair<Integer, Integer> key = new myPair(chunkX, chunkY);
                Chunk chunkPtr = getChunk(key);

                if(chunkPtr != null) {
                    if(myString.getField(ent.entityName, "type") == "tile" && chunkPtr.isImageBlank(tileX, tileY)) mark = true;
                    chunkPtr.setActive(tileX, tileY, false);
                }

                if(mark) entitiesToBeDeleted.add(ent);
            }
        }
    }
    public void loadEntities() {


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

                myPair<Integer, Integer> key = new myPair(chunkX, chunkY);
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
                        ent.width = tileSize;
                        ent.height = tileSize;
                        ent.bitMapX = tileXAbs;
                        ent.bitMapY = tileYAbs;
                        ent.deleteRange = -1;
                       // Engine.get().getPhysics().addBody(ent, 0, 0, tileSize, tileSize, "static", false);
                        entitiesToBeAdded.add(ent);
                    }

                    chunkPtr.setActive(tileXRel, tileYRel, true);
                }
            }}
        }
    }
}
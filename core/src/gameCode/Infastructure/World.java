package gameCode.Infastructure;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import gameCode.Living.HeroInput;
import gameCode.Utilities.StringUtils;

public class World {

    //Make Sure that this cant be instantiated
    private World() {}

    //Declare constants ==================================
    public static final int xCell = 10;
    public static final int tileSize = 60;
    public static final int tilesPerChunk = 20;

    //These values change based on the size of the game ==
    private static int numChunks;
    private static int numBlocks;
    private static int numPixels;
    private static int numCells;

    //viewPort parameters ================================
    public static int xViewSize;
    public static int yViewSize;

    //These modify the game state ========================
    public static ArrayList<Entity> entitiesToBeAdded = new ArrayList<Entity>();
    public static ArrayList<Entity> entitiesToBeDeleted = new ArrayList<Entity>();



    //These are the different data structures that reference the game entities
    private static LinkedList<Entity> entList = new LinkedList<Entity>();
    private static ArrayList<Chunk> chunks = new ArrayList<Chunk>();
    private static ArrayList<Boolean> containsMoveables = new ArrayList<Boolean>();
    private static ArrayList< ArrayList<Entity> > locatorCells = new ArrayList<  ArrayList<Entity> >();
    private static ArrayList<Entity> entitiesOrderedByLayer = new ArrayList<Entity>();

    //Entity that controls the viewport ========================================================================
    private static Entity camera = null;


    //Basic getters and setters ================================================================================
    public static int getNumChunks() { return numChunks; }
    public static int getNumPixels() { return numPixels; }
    public static int getNumBlocks() { return numBlocks; }
    public static int getNumCells() { return numCells; }

    public static LinkedList<Entity> getEntList() { return entList; }
    public static Entity getCamera() { return camera; }

    public static void init() {





        /*

        entList =
        Entity ent = new Entity();
        ent.x_pos = 100;
        ent.y_pos = 100;
        ent.spriteName = "tile";
        ent.addComponent(new HeroInput());
        entList.add(ent);

        String abso = "/Users/me/Desktop/gdx3/core/saves/";
        FileHandle[] files = Gdx.files.local("core/saves/").list();


        for(FileHandle file: files) {
            // do something interesting here
            String s = file.name();
            System.out.println("directory: " + s);
        }

         */

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

}

package gameCode.Infastructure;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.LinkedList;
import gameCode.Living.HeroInput;

public class World {

    //Make Sure that this cant be instantiated
    private World() {}

    //Declare constants ==================================
    static public final int xCell = 10;
    static public final int tileSize = 60;
    static public final int tilesPerChunk = 20;

    //These values change based on the size of the game ==

    //These modify the game state ========================
    ArrayList<Entity> entitiesToBeAdded;

    //These are the different data structures that reference the game entities
    static public LinkedList<Entity> entList;


    public LinkedList<Entity> getEntList() { return entList; }


    public static void init() {


        entList = new LinkedList<Entity>();

        Entity ent = new Entity();
        ent.x_pos = 100;
        ent.y_pos = 100;
        ent.spriteName = "tile";
        ent.addComponent(new HeroInput());

        entList.add(ent);

    }



}

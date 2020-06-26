package gameCode.Infastructure;

import java.security.PublicKey;
import java.util.LinkedList;

public class World {

    //Make Sure that this cant be instantiated
    private World() {}


    //Declare constants ==================================
    static public final int xCell = 10;
    static public final int tileSize = 60;
    static public final int tilesPerChunk = 20;

    //These values change based on the size of the game ==


    //These are the different data structures that reference the game entities
    LinkedList<Entity> entList;


    public LinkedList<Entity> getEntList() { return entList; }

    public static void init() {

    }



}

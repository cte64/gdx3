package gameCode.Infastructure;

import gameCode.Living.HeroInput;
import gameCode.Terrain.MakeWorld;

public class State {

    private static void roundEarth() {}
    private static void loadGame() {}
    private static void paused() {}
    private static void play() {}
    private static void mainScreen() {}
    private static void newGame() {}
    private static void loading() {}


    private static void testGame() {



        //MakeWorld.testCube();


        //Set the viewport size and update the screen ===================
        Entity ent = new Entity();
        ent.x_pos = 100;
        ent.y_pos = 100;
        ent.spriteName = "tile";
        ent.entityName = "hero";
        ent.addComponent(new HeroInput());
        World.entitiesToBeAdded.add(ent);
        World.setCamera(ent);
        World.addSiftingFrame(ent, 0, 0);


    }

    private State() {}
    public void deleteMenuItems() {}
    private static String state;
    public static String getState() { return state; }
    public static void setState(String newState) { state = newState; }

    public static void loadState() {
        if(World.getCurrentState() == "testGame") testGame();
    }
}

package gameCode.Infastructure;

import com.badlogic.gdx.graphics.Pixmap;
import gameCode.Living.HeroInput;
import gameCode.Terrain.MakeWorld;
import gameCode.Utilities.StringUtils;

public class State {

    private static void roundEarth() {}
    private static void loadGame() {}
    private static void paused() {}
    private static void play() {}
    private static void mainScreen() {}
    private static void newGame() {}
    private static void loading() {}

    private static void testGame() {

        //MakeWorld makeWorld = new MakeWorld("newOne", 10, 700);

        //Set the viewport size and update the screen ===================
        Entity ent = new Entity();
        ent.x_pos = 0;//World.getNumPixels() / 2;
        ent.y_pos = World.getNumPixels() / 2;
        ent.spriteName = "tile";
        ent.entityName = "hero";
        ent.addComponent(new HeroInput());
        World.entitiesToBeAdded.add(ent);
        World.setCamera(ent);
        World.addSiftingFrame(ent, 0, 0);

        Entity hud = new Entity();
        hud.x_pos = 100;
        hud.y_pos = 100;
        hud.spriteName = "menuBack";
        hud.entityName = "hud";
        hud.drawMode = "hud";
        hud.text = "Clarence";
        World.entitiesToBeAdded.add(hud);
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

package gameCode.Infastructure;

import com.badlogic.gdx.graphics.Pixmap;
import gameCode.Living.HeroInput;
import gameCode.Menus.LoadGame;
import gameCode.Menus.MainMenu;
import gameCode.Menus.NewGame;
import gameCode.Terrain.MakeWorld;
import gameCode.Utilities.StringUtils;

public class State {




    private static void roundEarth() {}
    private static void loadGame() {
        deleteMenuItems();
        Entity hud = new Entity();
        hud.entityName = "[type: menu][name: loadGame]";
        hud.drawMode = "hud";
        hud.addComponent(new LoadGame());
        World.entitiesToBeAdded.add(hud);
    }
    private static void paused() {}
    private static void play() {}
    private static void mainMenu() {
        deleteMenuItems();
        Entity hud = new Entity();
        hud.entityName = "[type: menu][name: mainMenu]";
        hud.drawMode = "hud";
        hud.addComponent(new MainMenu());
        World.entitiesToBeAdded.add(hud);
    }
    private static void newGame() {
        deleteMenuItems();
        Entity hud = new Entity();
        hud.entityName = "[type: menu][name: newGame]";
        hud.drawMode = "hud";
        hud.addComponent(new NewGame());
        World.entitiesToBeAdded.add(hud);
    }
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
    }
    private State() {}
    public static void deleteMenuItems() {
        for(Entity ent: World.getEntList()) {
            String type = StringUtils.getField(ent.entityName, "type");
            if(type.equals("menu")) World.entitiesToBeDeleted.add(ent);
        }
    }
    private static String state;
    public static String getState() { return state; }
    public static void setState(String newState) { state = newState; }

    public static void loadState() {
        if(World.getCurrentState() == "testGame") testGame();
        if(World.getCurrentState() == "mainMenu") mainMenu();
        if(World.getCurrentState() == "newGame") newGame();
        if(World.getCurrentState() == "loadGame") loadGame();
    }
}

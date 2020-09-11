package gameCode.Infrastructure;

import com.mygdx.game.Engine;
import gameCode.Menus.MenuScreens.LoadGame;
import gameCode.Utilities.myString;

public class State {

    public static void loadGame() {
        deleteType("type", "menu");
        Entity hud = new Entity();
        hud.entityName = "[type: menu][name: loadGame]";
        hud.drawMode = "hud";
        hud.addComponent(new LoadGame());
        myWorld.get().entitiesToBeAdded.add(hud);
        state = "loadGame";
    }

    public static void play(String newState) {

        //set the state =====================================================================
        String directory = myString.getField(newState, "directory");
        state = "[action: play]";

        //delete menu stuff =================================================================
        deleteType("type", "menu");

        //set Directory =====================================================================
        Engine.get().getFileSystem().setGameSubDirectory(directory);

        //MetaData and Create World =========================================================
        myString metaData = new myString("");
        Engine.get().getFileSystem().getFile(new myString("[type: metadata]"), metaData);
        String numChunksStr = metaData.getField("numChunks");
        int numChunks = myString.stringToInt(numChunksStr);
        myWorld.get().createWorld(numChunks);

        //Hero ==============================================================================
        myString heroData = new myString("");
        Engine.get().getFileSystem().getFile(new myString("[type: hero]"), heroData);
        Entity hero = MakeEntity.getEntity(heroData.data);
        myWorld.get().entitiesToBeAdded.add(hero);

        //Add the pause item ================================================================
        Entity pause = MakeEntity.getEntity("[type: menu][subType: pause]");
        myWorld.get().entitiesToBeAdded.add(pause);
    }

    public static void mainMenu() {
        myWorld.get().deleteWorld();
        Entity ent = MakeEntity.getEntity("[type: menu][subType: mainMenu]");
        myWorld.get().entitiesToBeAdded.add(ent);
        state = "mainMenu";
    }

    public static void newMenuTest() {
        myWorld.get().deleteWorld();
        Entity ent = MakeEntity.getEntity("[type: menu][subType: newMenuTest]");
        myWorld.get().entitiesToBeAdded.add(ent);
        state = "newMenuTest";
    }

    public static void newGame() {
        deleteType("type", "menu");
        Entity ent = MakeEntity.getEntity("[type: menu][subType: newGame]");
        myWorld.get().entitiesToBeAdded.add(ent);
        state = "newGame";
    }

    public static void creatingGame(String newData) {
        state = newData;
        deleteType("type", "menu");
        Entity ent = MakeEntity.getEntity("[type: menu][subType: createGameLoadingScreen]");
        myWorld.get().entitiesToBeAdded.add(ent);
    }

    //State stuff ==========================================================
    private static String state;
    public static String getState() { return state; }
    public static void setState(String newState) { state = newState; }

    //Modifiers ==================================================================
    private State() {}

    public static void deleteType(String field, String type) {
        for(Entity ent: myWorld.get().getEntList()) {
            String thisType = myString.getField(ent.entityName, field);
            if(thisType.equals(type)) myWorld.get().entitiesToBeDeleted.add(ent);
        }
    }
}

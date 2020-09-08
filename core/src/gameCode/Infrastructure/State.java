package gameCode.Infrastructure;

import gameCode.Menus.MenuScreens.LoadGame;
import gameCode.Utilities.StringUtils;

public class State {

    public static void loadGame() {
        deleteType("type", "menu");
        Entity hud = new Entity();
        hud.entityName = "[type: menu][name: loadGame]";
        hud.drawMode = "hud";
        hud.addComponent(new LoadGame());
        World.get().entitiesToBeAdded.add(hud);
        state = "loadGame";
    }

    public static void play(String newState) {

        //set the state =====================================================================
        String directory = StringUtils.getField(newState, "directory");
        state = "[action: play]";

        //delete menu stuff =================================================================
        deleteType("type", "menu");

        //set Directory =====================================================================
        FileSystem.setGameSubDirectory(directory);

        //MetaData and Create World =========================================================
        StringUtils metaData = new StringUtils("");
        FileSystem.getFile(new StringUtils("[type: metadata]"), metaData);
        String numChunksStr = StringUtils.getField(metaData, "numChunks");
        int numChunks = StringUtils.stringToInt(numChunksStr);
        World.get().createWorld(numChunks);

        //Hero ==============================================================================
        StringUtils heroData = new StringUtils("");
        FileSystem.getFile(new StringUtils("[type: hero]"), heroData);
        Entity hero = MakeEntity.getEntity(heroData.data);
        World.get().entitiesToBeAdded.add(hero);

        //Add the pause item ================================================================
        Entity pause = MakeEntity.getEntity("[type: menu][subType: pause]");
        World.get().entitiesToBeAdded.add(pause);
    }

    public static void mainMenu() {
        World.get().deleteWorld();
        Entity ent = MakeEntity.getEntity("[type: menu][subType: mainMenu]");
        World.get().entitiesToBeAdded.add(ent);
        state = "mainMenu";
    }

    public static void newMenuTest() {
        World.get().deleteWorld();
        Entity ent = MakeEntity.getEntity("[type: menu][subType: newMenuTest]");
        World.get().entitiesToBeAdded.add(ent);
        state = "newMenuTest";
    }

    public static void newGame() {
        deleteType("type", "menu");
        Entity ent = MakeEntity.getEntity("[type: menu][subType: newGame]");
        World.get().entitiesToBeAdded.add(ent);
        state = "newGame";
    }

    public static void creatingGame(String newData) {
        state = newData;
        deleteType("type", "menu");
        Entity ent = MakeEntity.getEntity("[type: menu][subType: createGameLoadingScreen]");
        World.get().entitiesToBeAdded.add(ent);
    }

    //State stuff ==========================================================
    private static String state;
    public static String getState() { return state; }
    public static void setState(String newState) { state = newState; }

    //Modifiers ==================================================================
    private State() {}

    public static void deleteType(String field, String type) {
        for(Entity ent: World.get().getEntList()) {
            String thisType = StringUtils.getField(ent.entityName, field);
            if(thisType.equals(type)) World.get().entitiesToBeDeleted.add(ent);
        }
    }
}

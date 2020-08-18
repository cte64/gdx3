package gameCode.Infastructure;

import com.badlogic.gdx.graphics.Pixmap;
import gameCode.Living.HeroInput;
import gameCode.Menus.*;
import gameCode.Terrain.MakeWorld;
import gameCode.Utilities.StringUtils;

import java.io.File;

public class State {

    private static void loadGame() {
        deleteType("type", "menu");
        Entity hud = new Entity();
        hud.entityName = "[type: menu][name: loadGame]";
        hud.drawMode = "hud";
        hud.addComponent(new LoadGame());
        World.entitiesToBeAdded.add(hud);
    }

    private static void play() {

        //delete menu stuff =================================================================
        deleteType("type", "menu");

        //set Directory =====================================================================
        String directory = StringUtils.getField(World.getCurrentState(), "directory");
        FileSystem.setGameSubDirectory(directory);

        //MetaData and Create World =========================================================
        StringUtils metaData = new StringUtils("");
        FileSystem.getFile(new StringUtils("[type: metadata]"), metaData);
        String numChunksStr = StringUtils.getField(metaData, "numChunks");
        int numChunks = StringUtils.stringToInt(numChunksStr);
        World.createWorld(numChunks);

        //Hero ==============================================================================
        StringUtils heroData = new StringUtils("");
        FileSystem.getFile(new StringUtils("[type: hero]"), heroData);
        Entity hero = MakeEntity.getEntity(heroData.data);
        World.entitiesToBeAdded.add(hero);

        //Add the pause item ================================================================
        Entity pause = MakeEntity.getEntity("[type: menu][subType: pause]");
        World.entitiesToBeAdded.add(pause);
    }

    private static void mainMenu() {
        deleteType("type", "menu");
        Entity ent = MakeEntity.getEntity("[type: menu][subType: mainMenu]");
        World.entitiesToBeAdded.add(ent);
    }

    private static void newGame() {
        deleteType("type", "menu");
        Entity ent = MakeEntity.getEntity("[type: menu][subType: newGame]");
        World.entitiesToBeAdded.add(ent);
    }

    private static void creatingGame() {
        deleteType("type", "menu");
        Entity ent = MakeEntity.getEntity("[type: menu][subType: createGameLoadingScreen]");
        World.entitiesToBeAdded.add(ent);
    }

    //State stuff ==========================================================
    private static String state;

    public static String getState() { return state; }

    public static void setState(String newState) { state = newState; }

    //Modifiers ==================================================================
    private State() {}

    public static void loadState() {

        String action = StringUtils.getField(World.getCurrentState(), "action");

        if(action.equals("newGame")) newGame();
        if(action.equals("loadGame")) loadGame();
        if(action.equals("mainMenu")) mainMenu();
        if(action.equals("play")) play();
        if(action.equals("createNewWorld")) creatingGame();
    }

    public static void deleteType(String field, String type) {
        for(Entity ent: World.getEntList()) {
            String thisType = StringUtils.getField(ent.entityName, field);
            if(thisType.equals(type)) World.entitiesToBeDeleted.add(ent);
        }
    }
}

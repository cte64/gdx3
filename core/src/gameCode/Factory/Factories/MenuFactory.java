package gameCode.Factory.Factories;

import gameCode.Factory.AbstractFactory;
import gameCode.Infrastructure.Entity;
import gameCode.Menus.MenuScreens.*;
import gameCode.Utilities.myString;

public class MenuFactory extends AbstractFactory {

    public Entity makeEntity(String id) {

        Entity ent = new Entity();
        String subType = myString.getField(id, "subType");

        if(subType.equals("mainMenu")) {
            ent.drawMode = "hud";
            ent.deleteRange = -2;
            ent.addComponent(new MainMenu());
        }

        if(subType.equals("newGame")) {
            ent.drawMode = "hud";
            ent.deleteRange = -2;
            ent.addComponent(new NewGame());
        }

        if(subType.equals("createGameLoadingScreen")) {
            ent.drawMode = "hud";
            ent.deleteRange = -2;
            ent.addComponent(new CreateGameLoadingScreen());
        }

        if(subType.equals("pause")) {
            ent.drawMode = "hud";
            ent.deleteRange = -2;
            ent.addComponent(new PauseGame());
        }

        if(subType.equals("loadGame")) {
            ent.drawMode = "hud";
            ent.deleteRange = -2;
            ent.addComponent(new LoadGame());
        }


        return ent;
    }
}

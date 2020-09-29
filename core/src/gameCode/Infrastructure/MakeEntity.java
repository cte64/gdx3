package gameCode.Infrastructure;

import com.badlogic.gdx.graphics.Pixmap;
import com.mygdx.game.Engine;
import gameCode.Living.HeroInput;
import gameCode.Living.PlaceTerrain;
import gameCode.Menus.Inventory.InventoryManager;
import gameCode.Menus.MenuScreens.CreateGameLoadingScreen;
import gameCode.Menus.MenuScreens.MainMenu;
import gameCode.Menus.MenuScreens.NewGame;
import gameCode.Menus.MenuScreens.PauseGame;
import gameCode.Utilities.myString;

public class MakeEntity {

    public static Entity getEntity(String name, Pixmap image) {
        Entity ent = getEntity(name);
        ent.deleteRange = -1;
        ent.spriteName = Engine.get().getAssets().getCoord();
        Engine.get().getAssets().updateSprite(ent.spriteName, image);
        return ent;
    }

    public static Entity getEntity(String name) {


        String type = myString.getField(name, "type");
        String subType = myString.getField(name, "subType");

        Entity ent = new Entity();
        ent.entityName = name;



        if(subType.equals("silverPickaxe")) {
            System.out.println("painav");
        }

        if(type.equals("hero")) {
            String x_posStr = myString.getField(name, "xPos");
            String y_posStr = myString.getField(name, "yPos");
            ent.x_pos = myWorld.get().getNumPixels()/2 - 400;//StringUtils.stringToInt(x_posStr);
            ent.y_pos = ent.x_pos + 4200;//StringUtils.stringToInt(y_posStr);
            ent.spriteName = "miner";
            ent.entityName = "hero";
            ent.z_pos = 20;
            ent.drawMode = "normal";
            ent.addComponent(new HeroInput());
            ent.width = 38;
            ent.height = 54;


            ent.addComponent(new PlaceTerrain());
            ent.addComponent(new InventoryManager());
            Engine.get().getGraphics().getCameraHelper().setTarget(ent);
            Engine.get().getPhysics().addBody2(ent, 0, 0, ent.width, ent.height, "dynamic", true, 1);
            myWorld.get().setCamera(ent);
            myWorld.get().addSiftingFrame(ent, 0, 0);
        }

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

        return ent;
    }
}
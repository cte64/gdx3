package gameCode.Infastructure;

import com.badlogic.gdx.graphics.Pixmap;
import gameCode.Living.HeroInput;
import gameCode.Menus.CreateGameLoadingScreen;
import gameCode.Menus.MainMenu;
import gameCode.Menus.NewGame;
import gameCode.Menus.PauseGame;
import gameCode.Utilities.StringUtils;

public class MakeEntity {

    public static Entity getEntity(String name, Pixmap image) {
        Entity ent = getEntity(name);
        ent.deleteRange = -1;
        ent.spriteName = Graphics.getCoord();
        Graphics.updateSprite(ent.spriteName, image);
        return ent;
    }

    public static Entity getEntity(String name) {


        String type = StringUtils.getField(name, "type");
        String subType = StringUtils.getField(name, "subType");

        Entity ent = new Entity();
        ent.entityName = name;

        if(type.equals("hero")) {
            String x_posStr = StringUtils.getField(name, "xPos");
            String y_posStr = StringUtils.getField(name, "yPos");
            ent.x_pos = World.getNumPixels()/2;//StringUtils.stringToInt(x_posStr);
            ent.y_pos = ent.x_pos;//StringUtils.stringToInt(y_posStr);
            ent.spriteName = "tile";
            ent.entityName = "hero";
            ent.z_pos = 3;
            ent.addComponent(new HeroInput());
            World.setCamera(ent);
            World.addSiftingFrame(ent, 0, 0);
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
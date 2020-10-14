package gameCode.Factory.Factories;

import com.mygdx.game.Engine;
import gameCode.Factory.AbstractFactory;
import gameCode.Infrastructure.Entity;
import gameCode.Infrastructure.myWorld;
import gameCode.Living.HeroInput;
import gameCode.Menus.Inventory.InventoryManager;
import gameCode.Utilities.myString;

public class ActorFactory extends AbstractFactory {

    public Entity makeEntity(String id) {

        Entity ent = new Entity();
        String subType = myString.getField(id, "subType");


        if(subType.equals("hero")) {
            String x_posStr = myString.getField(id, "xPos");
            String y_posStr = myString.getField(id, "yPos");
            ent.x_pos = myWorld.get().getNumPixels()/2 - 400;//StringUtils.stringToInt(x_posStr);
            ent.y_pos = ent.x_pos + 4200;//StringUtils.stringToInt(y_posStr);
            //ent.spriteName = "miner";
            ent.entityName = "hero";
            ent.z_pos = 20;
            ent.drawMode = "normal";
            ent.addComponent(new HeroInput());
            ent.width = 38;
            ent.height = 54;
            ent.origin.first = ent.width/2;
            ent.origin.second = ent.height/2;

            //ent.addComponent(new PlaceTerrain());
            ent.addComponent(new InventoryManager(ent));
            Engine.get().getGraphics().getCameraHelper().setTarget(ent);
            Engine.get().getPhysics().addBody2(ent, 0, 0, ent.width, ent.height, "dynamic", true, 1);
            Engine.get().getPhysics().addLight(ent);
            myWorld.get().setCamera(ent);
            myWorld.get().addSiftingFrame(ent, 0, 0);
        }

        if(subType.equals("bodyPart")) {
            ent.width = 38;
            ent.height = 54;
            ent.origin.first = ent.width/2;
            ent.origin.second = ent.height/2;
            ent.z_pos = 20;
            ent.deleteRange = -2;
            ent.drawMode = "normal";
        }



        return ent;
    }
}

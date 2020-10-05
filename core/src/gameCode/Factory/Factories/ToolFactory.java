package gameCode.Factory.Factories;

import com.mygdx.game.Engine;
import gameCode.Factory.AbstractFactory;
import gameCode.Infrastructure.Entity;
import gameCode.Tools.Pickaxe;
import gameCode.Utilities.myString;

public class ToolFactory extends AbstractFactory {


    public Entity makeEntity(String id) {


        Entity ent = new Entity();
        String subType = myString.getField(id, "subType");


        if(subType.equals("silverPickaxe")) {
            ent.deleteRange = -2;
            ent.spriteName = "silverPickaxe";
            ent.width = Engine.get().getAssets().getSpriteDimensions(ent.spriteName).first;
            ent.height = Engine.get().getAssets().getSpriteDimensions(ent.spriteName).second;
            ent.origin.first = ent.width/2.0f;
            ent.origin.second = ent.height/2.0f;
            ent.addComponent(new Pickaxe());
        }


        return ent;
    }

}

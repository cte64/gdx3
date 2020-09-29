package gameCode.Factory.Factories;

import gameCode.Factory.AbstractFactory;
import gameCode.Infrastructure.Entity;
import gameCode.Utilities.myString;

public class ToolFactory extends AbstractFactory {


    public Entity makeEntity(String id) {


        Entity ent = new Entity();
        String subType = myString.getField(id, "subType");


        if(subType.equals("silverPickaxe")) {

        }


        return ent;
    }

}

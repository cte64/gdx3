package gameCode.Factory.Factories;

import gameCode.Factory.AbstractFactory;
import gameCode.Infrastructure.Entity;
import gameCode.Tools.Pickaxe;
import gameCode.Utilities.myString;

public class ToolFactory extends AbstractFactory {


    public Entity makeEntity(String id) {


        Entity ent = new Entity();
        String subType = myString.getField(id, "subType");


        if(subType.equals("silverPickaxe")) {
            ent.addComponent(new Pickaxe());
            System.out.println("siver");
        }


        return ent;
    }

}

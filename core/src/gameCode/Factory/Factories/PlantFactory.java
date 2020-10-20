package gameCode.Factory.Factories;

import com.mygdx.game.Engine;
import gameCode.Factory.AbstractFactory;
import gameCode.Infrastructure.Entity;
import gameCode.Living.Plants.Tree;
import gameCode.Utilities.myString;

public class PlantFactory extends AbstractFactory {

    public Entity makeEntity(String id) {


        Entity ent = new Entity();
        String subType = myString.getField(id, "subType");

        if(subType.equals("tree")) {


            String xStr=  myString.getField(id, "xPos");
            String yStr=  myString.getField(id, "yPos");

            float newX = 0;
            float newY = 0;
            if(!xStr.equals("")) newX = myString.stringToFloat(xStr);
            if(!yStr.equals("")) newY = myString.stringToFloat(yStr);

            ent.x_pos = newX;
            ent.y_pos = newY;

            System.out.println(ent.x_pos + " : " + ent.y_pos);

            ent.deleteRange = -2;
            ent.drawMode = "normal";
            ent.z_pos = 100;



            ent.addComponent(new Tree("regular", 5));

        }

        if(subType.equals("treeTrunk")) {
            ent.deleteRange = -1;
            ent.drawMode = "normal";
            ent.spriteName = "trunk1";
        }


        return ent;
    }

}

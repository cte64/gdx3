package gameCode.Factory.Factories;

import gameCode.Factory.AbstractFactory;
import gameCode.Infrastructure.Entity;
import gameCode.Utilities.myString;

public class TerrainTextureFactory extends AbstractFactory {

    public Entity makeEntity(String id) {

        Entity ent = new Entity();
        String subType = myString.getField(id, "subType");

        ent.deleteRange = -2;
        ent.drawMode = "hud";
        ent.spriteName = subType + "Texture";
        ent.width = 20;
        ent.height = 20;
        ent.origin.first = ent.width/2.0f;
        ent.origin.second = ent.height/2.0f;

        return ent;
    }

}

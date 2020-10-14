package gameCode.Factory;

import com.badlogic.gdx.graphics.Pixmap;
import com.mygdx.game.Engine;
import gameCode.Factory.Factories.ActorFactory;
import gameCode.Factory.Factories.MaterialsFactory;
import gameCode.Factory.Factories.MenuFactory;
import gameCode.Factory.Factories.ToolFactory;
import gameCode.Infrastructure.Entity;
import gameCode.Utilities.myString;

public class EntityFactory {

    private EntityFactory() {}

    public static Entity createEntity(String id) {

        String type = myString.getField(id, "type");
        AbstractFactory factory = null;

        if(type.equals("menu")) factory = new MenuFactory();
        if(type.equals("tool")) factory = new ToolFactory();
        if(type.equals("actor")) factory = new ActorFactory();
        if(type.equals("material")) factory = new MaterialsFactory();


        //give it some default value just in case
        Entity ent = new Entity();
        if(factory != null) ent = factory.makeEntity(id);
        ent.entityName = id;
        return ent;
    }

    public static Entity createEntity(String id, Pixmap image) {
        Entity ent = createEntity(id);
        ent.deleteRange = -1;
        ent.spriteName = Engine.get().getAssets().getCoord();
        Engine.get().getAssets().updateSprite(ent.spriteName, image);
        return ent;
    }
}

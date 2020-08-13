package gameCode.Infastructure;

import com.badlogic.gdx.graphics.Pixmap;
import gameCode.Living.HeroInput;
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

        if(type.equals("hero")) {
            String x_posStr = StringUtils.getField(name, "xPos");
            String y_posStr = StringUtils.getField(name, "yPos");
            ent.x_pos = StringUtils.stringToInt(x_posStr);
            ent.y_pos = StringUtils.stringToInt(y_posStr);
            ent.spriteName = "tile";
            ent.entityName = "hero";
            ent.z_pos = 3;
            ent.addComponent(new HeroInput());
            World.setCamera(ent);
            World.addSiftingFrame(ent, 0, 0);
        }


        return ent;
    }
}
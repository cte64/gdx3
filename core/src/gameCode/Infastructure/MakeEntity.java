package gameCode.Infastructure;

import com.badlogic.gdx.graphics.Pixmap;
import gameCode.Utilities.StringUtils;

public class MakeEntity {

    public static Entity getEntity(String name, Pixmap image) {
        Entity ent = getEntity(name);
        ent.spriteName = Graphics.getCoord();
        Graphics.updateSprite(ent.spriteName, image);
        return ent;
    }

    public static Entity getEntity(String name) {
        String type = StringUtils.getField(name, "type");
        String subType = StringUtils.getField(name, "subType");
        Entity ent = new Entity();
        return ent;
    }
}
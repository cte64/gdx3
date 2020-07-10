package gameCode.Infastructure;

import com.badlogic.gdx.graphics.Pixmap;
import gameCode.Utilities.StringUtils;

public class MakeEntity {



    public static Entity getEntity(String name, Pixmap image) {


        String type = StringUtils.getField(name, "type");
        String subType = StringUtils.getField(name, "subType");
        Entity ent = new Entity();



        ent.spriteName = Graphics.getCoord();
        Graphics.updateSprite(ent.spriteName, image);
        System.out.println(name);


        return ent;
    }

    public static Entity getEntity(String name) {
        return getEntity(name, new Pixmap(0, 0, Pixmap.Format.RGB888));
    }



}

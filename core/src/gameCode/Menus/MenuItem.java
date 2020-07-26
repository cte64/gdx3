package gameCode.Menus;

import gameCode.Infastructure.*;

public class MenuItem {

    private int clickStateL;
    private int clickStateR;
    private boolean hoverState;


    Entity ent;

    public MenuItem(String newID, String sprNm, int x, int y, int w, int h, TextComponent tc) {

        clickStateL = 0;
        clickStateR = 0;
        hoverState = false;

        ent = MakeEntity.getEntity(newID);
        ent.entityName = newID;
        ent.spriteName = sprNm;
        ent.drawMode = "hud";
        ent.x_pos = x;
        ent.y_pos = y;
        ent.width = w;
        ent.height = h;
        ent.addComponent(tc);
        World.entitiesToBeAdded.add(ent);
    }

    public void update() {

    }


}

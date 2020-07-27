package gameCode.Menus;

import gameCode.Infastructure.Entity;
import gameCode.Infastructure.Graphics;
import gameCode.Infastructure.MakeEntity;
import gameCode.Infastructure.World;

public class MenuItem {

    private int clickStateL;
    private int clickStateR;
    private boolean hoverState;
    private int fontSize;
    private String message;
    Entity ent;

    public MenuItem(String newID, String newMsg, String sprNm, int newFs, int x, int y) {

        message = newMsg;
        fontSize = newFs;
        clickStateL = 0;
        clickStateR = 0;
        hoverState = false;

        ent = MakeEntity.getEntity(newID);
        ent.spriteName = sprNm;
        ent.x_pos = x;
        ent.y_pos = y;
        World.entitiesToBeAdded.add(ent);
    }

    public void update() {
        Graphics.addText(message, fontSize, (int)ent.x_pos, (int)ent.y_pos);
    }


}

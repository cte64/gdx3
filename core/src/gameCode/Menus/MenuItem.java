package gameCode.Menus;

import gameCode.Infrastructure.Entity;

public class MenuItem {
    public int clickStateL;
    public int clickStateR;
    public int hoverState;
    public Entity ent;
    public String justify;
    public int xOffset, yOffset;
    public MenuItem() {
        clickStateL = 0;
        clickStateR = 0;
        hoverState = 0;
        ent = null;
        justify = "";
        xOffset = 0;
        yOffset = 0;
    }
}

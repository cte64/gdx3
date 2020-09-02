package gameCode.Menus;

import gameCode.Infrastructure.Entity;
import gameCode.Utilities.Timer;

public class MenuItem {
    public int clickStateL;
    public int clickStateR;
    public int hoverState;
    public Entity ent;
    public String justify;
    public int xOffset, yOffset;
    public float tick;
    public MenuItem() {
        tick = 0.0f;
        clickStateL = 0;
        clickStateR = 0;
        hoverState = 0;
        ent = null;
        justify = "";
        xOffset = 0;
        yOffset = 0;
    }
}

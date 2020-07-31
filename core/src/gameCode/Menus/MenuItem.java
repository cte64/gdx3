package gameCode.Menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import gameCode.Infastructure.*;
import gameCode.Utilities.StringUtils;

public class MenuItem {

    private int clickStateL;
    private int clickStateR;
    private boolean hoverState;
    Entity ent;
    MenuItem parent;
    String justify;
    int xOffset, yOffset;

    private void positionItem() {

        String vertical = StringUtils.getField(justify, "vertical");
        String horizontal = StringUtils.getField(justify, "horizontal");

        //get the coordinates of the parent element====================================================
        int parentX = 0;
        int parentY = 0;
        int parentW = World.getViewPortWidth();
        int parentH = World.getViewPortHeight();

        if(parent != null) {
            parentX = (int)parent.ent.x_pos;
            parentY = (int)parent.ent.y_pos;
            parentW = (int)parent.ent.getWidth();
            parentH = (int)parent.ent.getHeight();
        }

        //vertical ===================================================================================
        ent.y_pos = 0;
        if(vertical.equals("center")) ent.y_pos = parentY + (parentH / 2) - ent.getHeight() / 2 + yOffset;
        if(vertical.equals("bottom")) ent.y_pos = parentY + yOffset;
        if(vertical.equals("top")) ent.y_pos = parentY + parentH - ent.getHeight() - yOffset;

        //horizontal =================================================================================
        ent.x_pos = 0;
        if(horizontal.equals("center")) ent.x_pos = parentX + (parentW / 2) - ent.getWidth() / 2 + xOffset;
        if(horizontal.equals("left")) ent.x_pos = parentX + xOffset;
        if(horizontal.equals("right")) ent.x_pos = parentX + parentW - ent.getWidth() + xOffset;
    }

    public MenuItem(String newID, String sprNm, MenuItem parent, String justify, int x, int y, int w, int h, TextComponent tc) {

        clickStateL = 0;
        clickStateR = 0;
        hoverState = false;
        this.parent = parent;
        this.justify = justify;
        xOffset = x;
        yOffset = y;

        ent = MakeEntity.getEntity(newID);
        ent.entityName = newID;
        ent.spriteName = sprNm;
        ent.drawMode = "hud";
        ent.width = w;
        ent.height = h;
        positionItem();
        if(tc != null) ent.addComponent(tc);
        World.entitiesToBeAdded.add(ent);
    }

    private boolean hover() {

        int xPos = InputAL.getMouseX();
        int yPos = InputAL.getMouseY();

        if(xPos >= ent.x_pos && xPos <= ent.x_pos + ent.getWidth() &&
           yPos >= ent.y_pos && yPos <= ent.y_pos + ent.getHeight()) {
            return true;
        }
        return false;
    }

    public boolean isLeftClicked() {
        if (!hover() && !InputAL.isPressed("mouse left")) clickStateL = 0;
        if (clickStateL == 0 && hover() && !InputAL.isPressed("mouse left")) clickStateL = 1;
        if (clickStateL == 1 && hover() && InputAL.isPressed("mouse left")) clickStateL = 2;
        if (clickStateL == 2 && hover() && !InputAL.isPressed("mouse left")) {
            clickStateL = 0;
            return true;
        }
        return false;
    }

    public void update() {

    }


}

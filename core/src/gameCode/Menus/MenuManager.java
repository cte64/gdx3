package gameCode.Menus;

import gameCode.Infastructure.*;
import gameCode.Utilities.StringUtils;
import gameCode.Utilities.Tree;

import java.util.HashMap;

public class MenuManager {

    private class MenuItem {
        public int clickStateL;
        public int clickStateR;
        public boolean hoverState;
        public Entity ent;
        public String justify;
        public int xOffset, yOffset;
        public MenuItem() {
        }
    }

    HashMap<String, Tree<MenuItem>> items;

    public MenuManager() {
        items = new HashMap<String, Tree<MenuItem>>();
    }

    public void positionItem(Tree<MenuItem> item) {

        MenuItem mn = ((MenuItem)item.value);
        Entity ent = mn.ent;

        String vertical = StringUtils.getField(mn.justify, "vertical");
        String horizontal = StringUtils.getField(mn.justify, "horizontal");

        //get the coordinates of the parent element====================================================
        int parentX = 0;
        int parentY = 0;
        int parentW = World.getViewPortWidth();
        int parentH = World.getViewPortHeight();

        Entity parentEnt = null;
        if(item.parent != null) parentEnt = ((MenuItem)item.parent.value).ent;

        if(parentEnt != null) {
            parentX = (int)parentEnt.x_pos;
            parentY = (int)parentEnt.y_pos;
            parentW = (int)parentEnt.getWidth();
            parentH = (int)parentEnt.getHeight();
        }

        //vertical ===================================================================================
        ent.y_pos = 0;
        if(vertical.equals("center")) ent.y_pos = parentY + (parentH / 2) - ent.getHeight() / 2 + mn.yOffset;
        if(vertical.equals("bottom")) ent.y_pos = parentY + mn.yOffset;
        if(vertical.equals("top")) ent.y_pos = parentY + parentH - ent.getHeight() - mn.yOffset;

        //horizontal =================================================================================
        ent.x_pos = 0;
        if(horizontal.equals("center")) ent.x_pos = parentX + (parentW / 2) - ent.getWidth() / 2 + mn.xOffset;
        if(horizontal.equals("left")) ent.x_pos = parentX + mn.xOffset;
        if(horizontal.equals("right")) ent.x_pos = parentX + parentW - ent.getWidth() + mn.xOffset;
    }

    public boolean hover(String id) {


        if(!items.containsKey(id)) return false;

        Entity ent = ((MenuItem)items.get(id).value).ent;

        int xPos = InputAL.getMouseX();
        int yPos = InputAL.getMouseY();

        if(xPos >= ent.x_pos && xPos <= ent.x_pos + ent.getWidth() &&
                yPos >= ent.y_pos && yPos <= ent.y_pos + ent.getHeight()) {
            return true;
        }
        return false;
    }

    public void registerItem(String id, String sprNm, String parent, String justify, int x, int y, int z, int w, int h) {


        MenuItem newItem = new MenuItem();

        newItem.clickStateL = 0;
        newItem.clickStateR = 0;
        newItem.hoverState = false;
        newItem.justify = justify;
        newItem.xOffset = x;
        newItem.yOffset = y;

        newItem.ent = MakeEntity.getEntity(id);
        newItem.ent.entityName = id;
        newItem.ent.spriteName = sprNm;
        newItem.ent.drawMode = "hud";
        newItem.ent.width = w;
        newItem.ent.height = h;
        newItem.ent.z_pos = z;
        newItem.ent.deleteRange = -2;
        World.entitiesToBeAdded.add(newItem.ent);


        Tree<MenuItem> parentTree = null;
        if(items.containsKey(parent)) parentTree = items.get(parent);

        Tree<MenuItem> treeNode = new Tree<MenuItem>(newItem, parentTree);


        items.put(id, treeNode);


        //position new item and all its children
        for(Tree tree: treeNode.getTraverseArr()) { positionItem(tree); }
    }

    public void addText(String id, Component textComp) {
        if(!items.containsKey(id)) return;
        ((MenuItem)items.get(id).value).ent.addComponent(textComp);
    }

    public boolean isLeftClicked(String id) {

        if(!items.containsKey(id)) return false;
        MenuItem mn = (MenuItem)items.get(id).value;

        if (!hover(id) && !InputAL.isMousePressed("mouse left")) mn.clickStateL = 0;
        if (mn.clickStateL == 0 && hover(id) && !InputAL.isMousePressed("mouse left")) mn.clickStateL = 1;
        if (mn.clickStateL == 1 && hover(id) && InputAL.isMousePressed("mouse left")) mn.clickStateL = 2;
        if (mn.clickStateL == 2 && hover(id) && !InputAL.isMousePressed("mouse left")) {
            mn.clickStateL = 0;
            return true;
        }
        return false;
    }
}

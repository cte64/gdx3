package gameCode.Menus;

import gameCode.Infastructure.*;
import gameCode.Utilities.MathUtils;
import gameCode.Utilities.StringUtils;
import gameCode.Utilities.Tree;

import java.util.ArrayList;
import java.util.HashMap;

public class MenuManager {

    private class ScrollList {
        public int width;
        public int top;
        public int bottom;
        public int left;
        public int itemHeight;
        public float scrollIndex;
        public float scrollPerFrame = 0.01f;
        ArrayList<String> listItems;
        String scrollBar;
        ScrollList(int width, int top, int bottom) {
            scrollIndex = 0;
            this.width = width;
            this.top = top;
            this.bottom = bottom;
            this.itemHeight = 70;
            scrollBar = "";
            listItems = new ArrayList<String>();
        }
    }

    HashMap<String, Tree<MenuItem>> items;
    private HashMap<String, ScrollList> scrollList;

    public void createList(String name, int width, int top, int bottom) {
        ScrollList newItem = new ScrollList(width, top, bottom);
        scrollList.put(name, newItem);
    }

    public void addListItem(String name, String itemID, String field) {
        if(!scrollList.containsKey(name)) return;
        if(field.equals("listItem")) scrollList.get(name).listItems.add(itemID);
        if(field.equals("scrollBar")) scrollList.get(name).scrollBar = itemID;
    }

    public void deleteListItem(String name, String itemID, String field) {
        if(!scrollList.containsKey(name)) return;
        if(field.equals("listItem"))  scrollList.get(name).listItems.remove(itemID);
        if(field.equals("scrollBar")) scrollList.get(name).scrollBar = itemID;
    }

    public ScrollList getScrollList(String name) {
        if(!scrollList.containsKey(name)) return null;
        else return scrollList.get(name);
    }

    public void updateList(String name) {
        if(!scrollList.containsKey(name)) return;
        ScrollList list = scrollList.get(name);

        //Update the Scrolling =====================================================================
        if(InputAL.isKeyPressed("down")) list.scrollIndex += list.scrollPerFrame;
        if(InputAL.isKeyPressed("up")) list.scrollIndex -= list.scrollPerFrame;
        for(Integer i: InputAL.scrollQueue) {
            if(i == -1) list.scrollIndex -= list.scrollPerFrame;
            if(i == 1) list.scrollIndex += list.scrollPerFrame;
        }

        //Position Everything ======================================================================
        list.scrollIndex = MathUtils.clamp(list.scrollIndex, 0.0f, 1.0f);
        int totalAmount = (list.listItems.size() - 1) * list.itemHeight * -1;
        int scrollOffset = (int)(totalAmount * list.scrollIndex);
        scrollOffset = MathUtils.clamp(scrollOffset, totalAmount, 0);

        for(int x = 0; x < list.listItems.size(); x++)  {

            int yPos = (int) (list.top + scrollOffset + (x * list.itemHeight));
            String newDrawMode = "hud";

            //if the yPos is out of bounds hide it
            if(yPos < list.top - list.itemHeight) newDrawMode = "hidden";
            if(yPos > list.bottom) newDrawMode = "hidden";

            ((MenuItem)items.get( list.listItems.get(x) ).value).yOffset = yPos;
            for(Tree<MenuItem> item: items.get( list.listItems.get(x) ).getTraverseArr() ) {
                positionItem(item);
                item.value.ent.drawMode = newDrawMode;
            }
        }


        //Position the scroll bar =======================================================
        Tree<MenuItem> scrollBar = items.get(list.scrollBar);
        if(scrollBar != null) {
            int barTop = list.top;
            int barBot = list.bottom - list.itemHeight + 6;
            int adjPs = (int)(list.top + (barBot - list.top) * list.scrollIndex);
            adjPs = MathUtils.clamp(adjPs, list.top, list.bottom - list.itemHeight + 6);

            ((MenuItem)scrollBar.value).yOffset = (int)adjPs;
            positionItem(scrollBar);
        }



    }

    public MenuManager() {
        items = new HashMap<String, Tree<MenuItem>>();
        scrollList = new HashMap<String, ScrollList>();
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

    public void updateDrawMode(Tree<MenuItem> item, String newMode) {
        ((MenuItem)item.value).ent.drawMode = newMode;
        for(Tree<MenuItem> node: item.getChildren()) {
            ((MenuItem)node.value).ent.drawMode = newMode;
        }
    }

    public void setParent(String itemKey, String newParentKey) {

        Tree<MenuItem> item = items.get(itemKey);
        if(item == null) return;

        Tree<MenuItem> parent = items.get(newParentKey);
        item.setParent(parent);

        positionItem(item);
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

    public void registerItem(String id, String sprNm, String parent, String justify, int x, int y, int z) {

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
        newItem.ent.width = Graphics.getSpriteDimensions(sprNm).first;
        newItem.ent.height = Graphics.getSpriteDimensions(sprNm).second;
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

    public Entity getEnt(String name) {
        if(!items.containsKey(name)) return null;
        else return ((MenuItem)items.get(name).value).ent;
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

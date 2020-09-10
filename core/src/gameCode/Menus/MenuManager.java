package gameCode.Menus;

import com.mygdx.game.Engine;
import gameCode.Infrastructure.*;
import gameCode.Infrastructure.Component;
import gameCode.Utilities.myMath;
import gameCode.Utilities.myString;
import gameCode.Utilities.Tree;
import java.util.ArrayList;
import java.util.HashMap;

public class MenuManager {

    HashMap<String, Tree<MenuItem>> items;

    public MenuManager() {
        items = new HashMap<String, Tree<MenuItem>>();
    }

    public void removeItem(String name) {
        if(!items.containsKey(name)) return;
        for(Tree<MenuItem> item: items.get(name).getTraverseArr()) { World.get().entitiesToBeDeleted.add( item.value.ent ); }
        items.remove(name);
    }

    private void positionItem(Tree<MenuItem> item) {

        MenuItem mn = ((MenuItem)item.value);
        Entity ent = mn.ent;

        String vertical = myString.getField(mn.justify, "vertical");
        String horizontal = myString.getField(mn.justify, "horizontal");

        //get the coordinates of the parent element====================================================
        int parentX = 0;
        int parentY = 0;
        int parentW = World.get().getViewPortWidth();
        int parentH = World.get().getViewPortHeight();

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
        if(vertical.equals("mouse")) ent.y_pos = Engine.get().getInput().getMouseY();


        //horizontal =================================================================================
        ent.x_pos = 0;
        if(horizontal.equals("center")) ent.x_pos = parentX + (parentW / 2) - ent.getWidth() / 2 + mn.xOffset;
        if(horizontal.equals("left")) ent.x_pos = parentX + mn.xOffset;
        if(horizontal.equals("right")) ent.x_pos = parentX + parentW - ent.getWidth() + mn.xOffset;
        if(horizontal.equals("mouse")) ent.x_pos = Engine.get().getInput().getMouseX();
    }

    public void setParent(String itemKey, String newParentKey) {

        if(!items.containsKey(itemKey)) return;

        Tree<MenuItem> item = items.get(itemKey);
        if(item == null) return;

        Tree<MenuItem> parent = items.get(newParentKey);
        item.setParent(parent);

        positionItem(item);

        //set the zPos of the item to newParentKey + 1
        Entity childEnt = item.getValue().ent;
        Entity parentEnt = parent.getValue().ent;
        if(childEnt != null && parent != null) {
            childEnt.z_pos = parentEnt.z_pos + 1;
        }
    }

    public boolean hover(String id) {


        if(!items.containsKey(id)) return false;

        Entity ent = ((MenuItem)items.get(id).value).ent;

        int xPos = Engine.get().getInput().getMouseX();
        int yPos = Engine.get().getInput().getMouseY();

        if(xPos >= ent.x_pos && xPos <= ent.x_pos + ent.getWidth() &&
                yPos >= ent.y_pos && yPos <= ent.y_pos + ent.getHeight()) {
            return true;
        }
        return false;
    }

    public void hoverAction(String name, String type) {


        MenuItem item = (MenuItem)(items.get(name).value);
        if(item == null) return;

        if(!hover(name) && item.hoverState == 0) item.hoverState = 1;
        if(hover(name) && item.hoverState == 1) item.hoverState = 2;
        if(!hover(name) && item.hoverState == 2) item.hoverState = 0;

        String oldSprite = myString.getField(type, "sprite");
        String newSprite = myString.getField(type, "hoverSprite");
        if(newSprite != "" && oldSprite != "") {
            if(item.hoverState == 0) item.ent.spriteName = oldSprite;
            if(item.hoverState == 2) item.ent.spriteName = newSprite;
        }

        String hoverType = myString.getField(type, "hoverType");

        if(hoverType.equals("toggle")) {

            String scale = myString.getField(type, "scale");
            if(scale != "") {
                float scaleF = myString.stringToFloat(scale);
                if(item.hoverState == 0) {
                    item.ent.scale.first = 1f;
                    item.ent.scale.second = 1f;
                }
                if(item.hoverState == 2){
                    item.ent.scale.first = scaleF;
                    item.ent.scale.second = scaleF;
                }
            }

            String fs = myString.getField(type, "fontSize");
            if(fs != "") {
                int newSize = myMath.clamp( myString.stringToInt(fs), -20, 20);
                ArrayList<Component> textComps = item.ent.getComponents("text");
                for(Component comp: textComps) {
                    TextComponent text = (TextComponent)comp;
                    if(text != null && text.show) {
                        if(item.hoverState == 0) text.setFontSize(text.getOldFontSize());
                        if(item.hoverState == 2) text.setFontSize(text.getOldFontSize() + newSize);
                    }
                }
            }

            updatePosition(name);
        }

        if(hoverType.equals("sine")) {

            if(!hover(name)) {
                item.ent.scale.first = 1.0f;
                item.ent.scale.second = 1.0f;
                item.tick = 0.0f;
                updatePosition(name);
                return;
            }

            String ampStr = myString.getField(type, "amplitude");
            String freqStr = myString.getField(type, "frequency");

            float amp = 0.05f;
            float freq = 1.0f;
            if(ampStr != "") amp = myString.stringToFloat(ampStr);
            if(freqStr != "") freq = myString.stringToFloat(freqStr);

            item.tick += World.get().getDeltaTime();
            double arg = item.tick * 2.0f * freq * myMath.PI;
            float scale = (float)Math.sin(arg);
            float newScale = 1.0f + scale * amp;

            item.ent.scale.first = newScale;
            item.ent.scale.second = newScale;

            updatePosition(name);
        }
    }

    public void registerItem(String id, String sprNm, String parent, String justify, int x, int y, int z) {

        MenuItem newItem = new MenuItem();

        newItem.clickStateL = 0;
        newItem.clickStateR = 0;
        newItem.hoverState = 0;
        newItem.justify = justify;
        newItem.xOffset = x;
        newItem.yOffset = y;

        newItem.ent = MakeEntity.getEntity(id);
        newItem.ent.entityName = id;
        newItem.ent.spriteName = sprNm;
        newItem.ent.drawMode = "hud";
        newItem.ent.width = Engine.get().getAssets().getSpriteDimensions(sprNm).first;
        newItem.ent.height = Engine.get().getAssets().getSpriteDimensions(sprNm).second;
        newItem.ent.z_pos = z;
        newItem.ent.deleteRange = -2;
        World.get().entitiesToBeAdded.add(newItem.ent);

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

        if (!hover(id) && !Engine.get().getInput().isMousePressed("mouse left")) mn.clickStateL = 0;
        if (mn.clickStateL == 0 && hover(id) && !Engine.get().getInput().isMousePressed("mouse left")) mn.clickStateL = 1;
        if (mn.clickStateL == 1 && hover(id) && Engine.get().getInput().isMousePressed("mouse left")) mn.clickStateL = 2;
        if (mn.clickStateL == 2 && hover(id) && !Engine.get().getInput().isMousePressed("mouse left")) {
            mn.clickStateL = 0;
            return true;
        }
        return false;
    }

    public boolean isRightClicked(String id) {


        if(!items.containsKey(id)) return false;
        MenuItem mn = (MenuItem)items.get(id).value;

        if (!hover(id) && !Engine.get().getInput().isMousePressed("mouse right")) mn.clickStateR = 0;
        if (mn.clickStateR == 0 && hover(id) && !Engine.get().getInput().isMousePressed("mouse right")) mn.clickStateR = 1;
        if (mn.clickStateR == 1 && hover(id) && Engine.get().getInput().isMousePressed("mouse right")) mn.clickStateR = 2;
        if (mn.clickStateR == 2 && hover(id) && !Engine.get().getInput().isMousePressed("mouse right")) {
            mn.clickStateR = 0;
            return true;
        }

        return false;
    }


    //Field Setters ========================================================================================
    public void updateXOffset(String name, int newXOffset) {
        if(!items.containsKey(name)) return;
        items.get(name).value.xOffset = newXOffset;
        updatePosition(name);
    }

    public void updateYOffset(String name, int newYOffset) {
        if(!items.containsKey(name)) return;
        items.get(name).value.yOffset = newYOffset;
        updatePosition(name);
    }

    public void updateDrawMode(String name, String newMode) {
        if(!items.containsKey(name)) return;
        for(Tree<MenuItem> node: items.get(name).getTraverseArr()) {
            node.value.ent.drawMode = newMode;
        }
    }

    public void updatePosition(String name) {
        if(!items.containsKey(name)) return;
        for(Tree<MenuItem> item: items.get(name).getTraverseArr()) {
            positionItem(item);
        }
    }
}

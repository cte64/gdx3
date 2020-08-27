package gameCode.Menus;

import gameCode.Infrastructure.InputAL;
import gameCode.Utilities.MathUtils;
import gameCode.Utilities.Tree;

import java.util.ArrayList;

public class ScrollList {
    
    public int width;
    public int top;
    public int bottom;
    public int left;
    public int itemHeight;
    public int itemWidth;
    public float scrollIndex;
    public int padding;
    public float scrollPerFrame = 0.01f;
    ArrayList<String> listItems;
    String scrollBar;
    MenuManager menuMngr;


    public ScrollList(MenuManager menuMngr) {

        this.menuMngr = menuMngr;

        listItems = new ArrayList<String>();
        scrollIndex = 0;
        width = 1;
        top = 0;
        padding = 0;
        bottom = 100;
        itemHeight = 70;
        itemWidth = 40;
        scrollBar = "";
    }

    public void addItem(String newItem) {
        listItems.add(newItem);
    }

    public boolean isLeftClicked(String name) {


        int mouseY = InputAL.getMouseY();
        /*
        if(mouseY > top && mouseY < bottom &&
           menuMngr.getEnt(name).drawMode.equals("hud") &&
           menuMngr.isLeftClicked(name))  return true;
        else return false;

         */


        if(menuMngr.getEnt(name).drawMode.equals("hud") && menuMngr.isLeftClicked(name)) return true;
        else return false;
    }

    public void deleteListItem(String name) {
        listItems.remove(name);
        menuMngr.removeItem(name);
    }

    public void updateList2() {

        //Update the Scrolling =====================================================================
        if(InputAL.isKeyPressed("down")) scrollIndex += scrollPerFrame;
        if(InputAL.isKeyPressed("up")) scrollIndex -= scrollPerFrame;
        for(Integer i: InputAL.scrollQueue) {
            if(i == -1) scrollIndex -= scrollPerFrame;
            if(i == 1) scrollIndex += scrollPerFrame;
        }

        //Position Everything ======================================================================
        scrollIndex = MathUtils.clamp(scrollIndex, 0.0f, 1.0f);
        int totalAmount = (listItems.size() - 1) * itemHeight * -1;
        int scrollOffset = (int)(totalAmount * scrollIndex);
        scrollOffset = MathUtils.clamp(scrollOffset, totalAmount, 0);

        for(int x = 0; x < listItems.size(); x++)  {

            int yPos = padding + top + scrollOffset + (x/ width)*(itemHeight + padding);
            int xPos = padding + left + (x % width)*(itemWidth + padding);

            String newDrawMode = "hud";

            //if the yPos is out of bounds hide it
            if(yPos < top - itemHeight) newDrawMode = "hidden";
            if(yPos > bottom) newDrawMode = "hidden";

            menuMngr.getItem(listItems.get(x)).value.yOffset = yPos;
            menuMngr.getItem(listItems.get(x)).value.xOffset = xPos;

            for(Tree<MenuItem> item: menuMngr.getItem(listItems.get(x)).getTraverseArr()) {
                menuMngr.positionItem(item);
                item.value.ent.drawMode = newDrawMode;
            }
        }

        //Position the scroll bar =======================================================
        Tree<MenuItem> sb = menuMngr.getItem(scrollBar);
        if(sb != null) {
            int sbHeight = (int)((MenuItem)(sb.value)).ent.getWidth();
            int barTop = top + padding;
            int barBot = bottom - sbHeight - padding;
            int adjPs = (int)(padding + top + (barBot - top) * scrollIndex);
            adjPs = MathUtils.clamp(adjPs, top, barBot);
            ((MenuItem)sb.value).yOffset = (int)adjPs;
            menuMngr.positionItem(sb);
        }
    }
}

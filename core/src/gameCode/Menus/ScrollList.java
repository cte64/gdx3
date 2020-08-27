package gameCode.Menus;

import gameCode.Infastructure.InputAL;
import gameCode.Utilities.MathUtils;
import gameCode.Utilities.Tree;

import java.util.ArrayList;

public class ScrollList {
    
    public int width;
    public int top;
    public int bottom;
    public int left;
    public int itemHeight;
    public float scrollIndex;
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
        bottom = 100;
        itemHeight = 70;
        scrollBar = "";
    }

    public void addItem(String newItem) {
        listItems.add(newItem);
    }

    public void deleteListItem(String name) {
        listItems.remove(name);
        menuMngr.removeItem(name);
    }

    public void updateList() {

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

            int yPos = (int) (top + scrollOffset + (x * itemHeight));
            String newDrawMode = "hud";

            //if the yPos is out of bounds hide it
            if(yPos < top - itemHeight) newDrawMode = "hidden";
            if(yPos > bottom) newDrawMode = "hidden";

            menuMngr.getItem(listItems.get(x)).value.yOffset = yPos;
            for(Tree<MenuItem> item: menuMngr.getItem(listItems.get(x)).getTraverseArr()) {
                menuMngr.positionItem(item);
                item.value.ent.drawMode = newDrawMode;
            }
        }

        //Position the scroll bar =======================================================
        Tree<MenuItem> sb = menuMngr.getItem(scrollBar);
        if(sb != null) {
            int barTop = top;
            int barBot = bottom - itemHeight + 6;
            int adjPs = (int)(top + (barBot - top) * scrollIndex);
            adjPs = MathUtils.clamp(adjPs, top, bottom - itemHeight + 6);
            ((MenuItem)sb.value).yOffset = (int)adjPs;
            menuMngr.positionItem(sb);
        }
    }
}

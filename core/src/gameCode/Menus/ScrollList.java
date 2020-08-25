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


    public ScrollList(int width, int top, int bottom) {
        

        /*
        scrollIndex = 0;
        this.width = width;
        this.top = top;
        this.bottom = bottom;
        this.itemHeight = 70;
        scrollBar = "";
        listItems = new ArrayList<String>();

         */
    }


    public void updateList(String name) {

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


        /*
            ((MenuItem)items.get( listItems.get(x) ).value).yOffset = yPos;
            for(Tree<MenuManager.MenuItem> item: items.get( listItems.get(x) ).getTraverseArr() ) {
                positionItem(item);
                item.value.ent.drawMode = newDrawMode;
            }
            */
        }


        /*
        //Position the scroll bar =======================================================
        Tree<MenuManager.MenuItem> scrollBar = items.get(scrollBar);
        if(scrollBar != null) {
            int barTop = top;
            int barBot = bottom - itemHeight + 6;
            int adjPs = (int)(top + (barBot - top) * scrollIndex);
            adjPs = MathUtils.clamp(adjPs, top, bottom - itemHeight + 6);

            ((MenuManager.MenuItem)scrollBar.value).yOffset = (int)adjPs;
            positionItem(scrollBar);
        }
        
         */



    }
}

package gameCode.Menus;

import com.mygdx.game.Engine;
import gameCode.Infrastructure.Entity;
import gameCode.Infrastructure.myWorld;
import gameCode.Utilities.myMath;

import java.util.ArrayList;

public class ScrollList {
    
    public int width;
    public int top;
    public int bottom;
    public int left;
    public float scrollIndex;
    public int vPadding;
    public int hPadding;
    public float scrollPixPerSecond;
    ArrayList<String> listItems;
    public String parent;
    public String scrollBar;
    MenuManager menuMngr;

    //Scroll stuff ======================================
    int sbState;

    public ScrollList(MenuManager menuMngr) {
        this.menuMngr = menuMngr;
        listItems = new ArrayList<String>();
        scrollIndex = 0;
        sbState = 0;
        width = 1;
        scrollPixPerSecond = 300;
        top = 0;
        vPadding = 1;
        hPadding = 0;
        bottom = 100;
        scrollBar = "";
    }

    public void addItem(String newItem) {
        listItems.add(newItem);
    }

    private boolean boundCheck(int mouseY) {
        int adjTop = top;
        int adjBot = bottom;

        Entity ent = menuMngr.getEnt(parent);
        if(ent != null) {
            adjTop += ent.y_pos;
            adjBot += ent.y_pos;
        }

        if(mouseY > adjTop && mouseY < adjBot) return true;
        else return false;
    }

    public boolean isLeftClicked(String name) {
        int mouseY = myWorld.get().getViewPortHeight() - Engine.get().getInput().getMouseY();
        if(boundCheck(mouseY) &&
           menuMngr.getEnt(name).drawMode.equals("hud") &&
           menuMngr.isLeftClicked(name))  return true;
        else return false;
    }

    public boolean hover(String name) {
        int mouseY = myWorld.get().getViewPortHeight() - Engine.get().getInput().getMouseY();
        if(boundCheck(mouseY) && menuMngr.hover(name)) return true;
        else return false;
    }

    public void deleteListItem(String name) {
        listItems.remove(name);
        menuMngr.removeItem(name);
    }

    public void updateList2() {

        //Update the Scrolling =====================================================================
        int totalPixelsToScroll = 0;
        int rows = -1 + (listItems.size() + width - 1) / width;
        for(int x = 0; x < rows; x++) {
            Entity listEnt = menuMngr.getEnt(listItems.get(x));
            if(listEnt != null) totalPixelsToScroll -= (listEnt.getHeight() + vPadding);
        }

        float fps = 1.0f / myWorld.get().getDeltaTime();
        float pixThisFrame = scrollPixPerSecond / fps;
        float adjScrollPerFrame = pixThisFrame / totalPixelsToScroll;

        if(Engine.get().getInput().isKeyPressed("down")) scrollIndex -= adjScrollPerFrame;
        if(Engine.get().getInput().isKeyPressed("up")) scrollIndex += adjScrollPerFrame;
        for(Integer i: Engine.get().getInput().scrollQueue) {
            if(i == 1) scrollIndex -= adjScrollPerFrame;
            if(i == -1) scrollIndex += adjScrollPerFrame;
        }

        if(hover(scrollBar) && Engine.get().getInput().isMousePressed("mouse left") && sbState == 0) sbState = 1;
        if(!Engine.get().getInput().isMousePressed("mouse left") ) sbState = 0;

        if(sbState == 1) {
            int adjTop = top;
            int adjBot = bottom;
            Entity ent = menuMngr.getEnt(parent);
            if(ent != null) {
                adjTop += ent.y_pos;
                adjBot += ent.y_pos;
            }

            float mouseY = myWorld.get().getViewPortHeight() - Engine.get().getInput().getMouseY();
            float sbHeight = menuMngr.getEnt(scrollBar).getHeight();
            float totalLength = adjBot - adjTop - sbHeight;
            float sbHeightComp = (sbHeight / 2.0f) / totalLength;
            scrollIndex = -sbHeightComp + (mouseY - adjTop) / totalLength;
        }


        //Position Everything ======================================================================
        scrollIndex = myMath.clamp(scrollIndex, 0.0f, 1.0f);
        int scrollOffset = (int)(totalPixelsToScroll * scrollIndex);
        scrollOffset = myMath.clamp(scrollOffset, totalPixelsToScroll, 0);

        for(int x = 0; x < listItems.size(); x++)  {

            int itemHeight = 0;
            int itemWidth = 0;
            Entity listEnt = menuMngr.getEnt(listItems.get(x));
            if(listEnt != null) {
                itemHeight = (int)listEnt.getHeight();
                itemWidth = (int)listEnt.getWidth();
            }

            int yPos = vPadding + top + scrollOffset + (x/ width)*(itemHeight + vPadding);
            int xPos = hPadding + left + (x % width)*(itemWidth + hPadding);

            String newDrawMode = "hud";
            if(yPos < top - itemHeight) newDrawMode = "hidden";
            if(yPos > bottom) newDrawMode = "hidden";

            menuMngr.updateYOffset(listItems.get(x), yPos);
            menuMngr.updateXOffset(listItems.get(x), xPos);
            menuMngr.updateDrawMode(listItems.get(x), newDrawMode);
        }

        //Position the scroll bar =======================================================
        Entity sb = menuMngr.getEnt(scrollBar);
        if(sb != null) {
            float sbHeight = sb.getHeight();
            float barTop = top;
            float barBot = bottom - sbHeight + 1;
            float adjPs =  barTop + (barBot - barTop) * scrollIndex;
            adjPs = myMath.clamp(adjPs, barTop, barBot);
            menuMngr.updateYOffset(scrollBar, (int)adjPs);
        }
    }
}

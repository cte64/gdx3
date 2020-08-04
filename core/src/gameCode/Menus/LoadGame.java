package gameCode.Menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import gameCode.Infastructure.Component;
import gameCode.Infastructure.Entity;
import gameCode.Infastructure.TextComponent;
import gameCode.Utilities.MathUtils;
import gameCode.Utilities.StringUtils;
import gameCode.Infastructure.InputAL;
import java.util.ArrayList;


public class LoadGame extends Component {


    private MenuItem background;
    private MenuItem overlay;
    private MenuItem title;
    private MenuItem scrollBar;

    private float scrollIndex;
    private float scrollPerFrame = 6.0f;
    private int itemHeight = 75;
    private int menuTop = 125;
    private int menuBottom = 552;

    ArrayList<MenuItem> gameItems = new ArrayList<MenuItem>();


    public LoadGame() {

        type = "logic";

        int padding = 5;
        int yOff = padding + 50;
        scrollIndex = 0;

        //background
        background = new MenuItem("[type: menu][name: background]", "loadGameBackground", null, "[vertical: center][horizontal: center]", 0, 0, 0, 664, 634);
        overlay = new MenuItem("[type: menu][name: overlay]", "loadGameOverlay", background.treeNode, "[vertical: center][horizontal: center]", 0, 0, 2, 664, 634);
        scrollBar = new MenuItem("[type: menu][name: scrollBar]", "scrollBar", background.treeNode, "[vertical: top][horizontal: right]", -60, 0, 2, 60, 68);

        //load game files
        for(int x = 0; x < 20; x++) {
            String name = "[type: menu][name: ]";
            name = StringUtils.setField(name, "name", StringUtils.toString(x));
            MenuItem gameItem = new MenuItem(name, "loadGameListItem", background.treeNode, "[vertical: top][horizontal: center]", -35, 0, 1, 500, 74);
            gameItem.addText(new TextComponent("this is the hook", 10, "center", 0, 0));
            gameItems.add(gameItem);
        }

        positionItems();
    }

    private void positionItems() {

        float upperBound = (gameItems.size() - 1) * itemHeight * -1;
        scrollIndex = MathUtils.clamp(scrollIndex, upperBound, 0.0f);

        //Position the game files in a list order =====================================
        for(int x = 0; x < gameItems.size(); x++)  {

            int yPos = (int) (menuTop + scrollIndex + (x * itemHeight));
            gameItems.get(x).ent.drawMode = "hud";

            //if the yPos is out of bounds hide it
            if(yPos < menuTop - itemHeight) gameItems.get(x).ent.drawMode = "hidden";
            if(yPos > menuBottom) gameItems.get(x).ent.drawMode = "hidden";

            gameItems.get(x).yOffset = yPos;
            gameItems.get(x).positionItem();
        }

        //Position the scroll bar =======================================================
        int barTop = menuTop;
        int barBot = menuBottom - itemHeight + 6;
        float adjPs = menuTop + (barBot - menuTop) * (scrollIndex / upperBound);

        scrollBar.xOffset = -43;
        scrollBar.yOffset = (int)adjPs;
        scrollBar.positionItem();
    }

    public void update(Entity entity) {


        if(InputAL.isKeyPressed("down")) {
            scrollIndex -= scrollPerFrame;
            positionItems();
        }

        if(InputAL.isKeyPressed("up")) {
            scrollIndex += scrollPerFrame;
            positionItems();
        }



        if(scrollBar.hover() && InputAL.isMousePressed("mouse left")) {


            int y = InputAL.getMouseY();



        }

    }

}

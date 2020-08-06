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



    private class listItem {
        MenuItem list;
        MenuItem play;
        MenuItem delete;
        public listItem() {}
    }

    ArrayList<listItem> listItems = new ArrayList<listItem>();


    public LoadGame() {

        type = "logic";

        int padding = 5;
        int yOff = padding + 50;
        scrollIndex = 0;

        //background
        background = new MenuItem("[type: menu][name: background]", "loadGameBackground", null, "[vertical: center][horizontal: center]", 0, 0, 0, 664, 634);
        overlay = new MenuItem("[type: menu][name: overlay]", "loadGameOverlay", background.treeNode, "[vertical: center][horizontal: center]", 0, 0, 3, 664, 634);
        scrollBar = new MenuItem("[type: menu][name: scrollBar]", "scrollBar", background.treeNode, "[vertical: top][horizontal: right]", -60, 0, 2, 60, 68);

        //load game files
        for(int x = 0; x < 10; x++) {

            String itemName = "[type: menu][item: listItem][id: ]";
            String playName = "[type: menu][item: playItem][id: ]";
            String deleteName = "[type: menu][item: deleteItem][id: ]";


            itemName = StringUtils.setField(itemName, "id", StringUtils.toString(x));
            playName = StringUtils.setField(playName, "id", StringUtils.toString(x));
            deleteName = StringUtils.setField(deleteName, "id", StringUtils.toString(x));

            listItem newItem = new listItem();
            newItem.list = new MenuItem(itemName, "loadGameListItem", background.treeNode, "[vertical: top][horizontal: center]", -35, 0, 1, 500, 74);
            newItem.play = new MenuItem(playName, "loadGamePlay", newItem.list.treeNode, "[vertical: center][horizontal: right]", -100, 0, 2, 65, 65);
            newItem.delete = new MenuItem(deleteName, "loadGameDelete", newItem.list.treeNode, "[vertical: center][horizontal: right]", -50, 0, 2, 65, 65);

            //gameItem.addText(new TextComponent("this is the hook", 10, "[vertical: center][horizontal: right]", 0, 0));
            listItems.add(newItem);
        }

        positionItems();
    }

    private void positionItems() {

        float upperBound = (listItems.size() - 1) * itemHeight * -1;
        scrollIndex = MathUtils.clamp(scrollIndex, upperBound, 0.0f);

        //Position the game files in a list order =====================================
        for(int x = 0; x < listItems.size(); x++)  {

            int yPos = (int) (menuTop + scrollIndex + (x * itemHeight));
            String newDrawMode = "hud";

            //if the yPos is out of bounds hide it
            if(yPos < menuTop - itemHeight) newDrawMode = "hidden";
            if(yPos > menuBottom) newDrawMode = "hidden";

            listItems.get(x).list.yOffset = yPos;
            listItems.get(x).list.update();
            listItems.get(x).list.updateDrawMode(newDrawMode);
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



            /*
            int y = InputAL.getMouseY();

            float total = (float)(menuBottom - menuTop);
            float slider = (float)(y - menuTop);
            float percent = slider / total;

            System.out.println( percent );

             */
        }

    }

}

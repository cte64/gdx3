package gameCode.Menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import gameCode.Infastructure.*;
import gameCode.Utilities.MathUtils;
import gameCode.Utilities.StringUtils;

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
        public MenuItem list;
        public MenuItem play;
        public MenuItem delete;
        public String name;
        public listItem() {}
    }

    private class areYouSure {
        public MenuItem background;
        public MenuItem yes;
        public MenuItem no;
        public areYouSure() {}
    }


    areYouSure deleteCheck;
    ArrayList<listItem> listItems = new ArrayList<listItem>();

    public LoadGame() {

        type = "logic";

        int padding = 5;
        int yOff = padding + 50;
        scrollIndex = 0;

        //background
        background = new MenuItem("[type: menu][name: background]", "loadGameBackground", null, "[vertical: center][horizontal: center]", 0, 0, 0, 664, 634);
        overlay = new MenuItem("[type: menu][name: overlay]", "loadGameOverlay", background.treeNode, "[vertical: center][horizontal: center]", 0, 0, 3, 664, 634);
        overlay.addText( new TextComponent("Load Game", 10, "[vertical: top][horizontal: center]", 0, 0));
        scrollBar = new MenuItem("[type: menu][name: scrollBar]", "scrollBar", background.treeNode, "[vertical: top][horizontal: right]", -60, 0, 4, 60, 68);

        //load game files
        ArrayList<String> files = FileSystem.getSaveNames();
        for(int x = 0; x < files.size(); x++) {

            //parse out the data from the metadata files
            String dateCreated = StringUtils.getField(files.get(x), "dateCreated");
            String numChunks = StringUtils.getField(files.get(x), "numChunks");
            String worldName = StringUtils.getField(files.get(x), "worldName");

            //create new unique names for each list item
            String itemName = "[type: menu][item: list][id: ]";
            String playName = "[type: menu][item: play][id: ]";
            String deleteName = "[type: menu][item: delete][id: ]";

            //set the names accordingly
            itemName = StringUtils.setField(itemName, "id", worldName);
            playName = StringUtils.setField(playName, "id", worldName);
            deleteName = StringUtils.setField(deleteName, "id", worldName);

            //create a new list item with the following items and add it to the list
            listItem newItem = new listItem();
            newItem.name = worldName;
            newItem.list = new MenuItem(itemName, "loadGameListItem", background.treeNode, "[vertical: top][horizontal: center]", -35, 0, 1, 500, 74);
            newItem.play = new MenuItem(playName, "loadGamePlay", newItem.list.treeNode, "[vertical: center][horizontal: right]", -65, 0, 2, 65, 65);
            newItem.delete = new MenuItem(deleteName, "loadGameDelete", newItem.list.treeNode, "[vertical: center][horizontal: right]", 0, 0, 2, 65, 65);

            newItem.list.addText(new TextComponent(worldName, 10, "[vertical: top][horizontal: left]", 0, 0));
            newItem.list.addText(new TextComponent("Date Created: " + dateCreated, 10, "[vertical: bottom][horizontal: left]", 0, 0));
            newItem.list.addText(new TextComponent("Size: " + numChunks, 10, "[vertical: center][horizontal: left]", 0, 0));

            listItems.add(newItem);
        }

        //deleteCheck
        deleteCheck = null;

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

    private void toggleDeleteCheck(boolean onOff) {


        if(onOff) {

            deleteCheck = new areYouSure();
            deleteCheck.background = new MenuItem("[type: menu][name: areYouSure][id: back]", "areYouSureBackground", background.treeNode, "[vertical: center][horizontal: center]", 0, 0, 5, 300, 286);


        }

        else deleteCheck = null;

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

            float total = (float)(menuBottom - menuTop);
            float slider = (float)(y - menuTop);
            float percent = slider / total;

            System.out.println( percent );

        }


        //ignore mouse clicks outside of the active region
        int mouseY = InputAL.getMouseY();

        for(listItem item: listItems) {


            if(mouseY > menuTop && mouseY < menuBottom && item.play.isLeftClicked()) {

            }

            if(mouseY > menuTop && mouseY < menuBottom && item.delete.isLeftClicked()) {

                //flash a screen that asks them if the are sure they want to delete the world
                toggleDeleteCheck(true);

            }
        }


    }

}

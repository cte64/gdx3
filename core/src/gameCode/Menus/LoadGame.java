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
    private MenuItem back;
    private MenuItem scrollBar;
    private MenuItem loadGame;

    private float scrollIndex;
    private float scrollPerFrame = 6.0f;
    private int itemHeight = 70;
    private int menuTop = 105;
    private int menuBottom = 526;


    private class listItem {
        public MenuItem list;
        public MenuItem play;
        public MenuItem delete;
        public String name;
        public listItem() {}
    }

    private class areYouSure {
        public MenuItem background;
        public MenuItem input;
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
        background = new MenuItem("[type: menu][name: background]", "loadGameBack", null, "[vertical: center][horizontal: center]", 0, 0, 0, 479, 600);
        overlay = new MenuItem("[type: menu][name: overlay]", "loadGameFront", background.treeNode, "[vertical: center][horizontal: center]", 0, 0, 3, 479, 600);
        overlay.addText( new TextComponent("Load Game", 10, "[vertical: top][horizontal: center]", 0, 0));

        scrollBar = new MenuItem("[type: menu][name: scrollBar]", "scrollBar", background.treeNode, "[vertical: top][horizontal: right]", -60, 0, 4, 51, 52);

        //bottom buttons
        back = new MenuItem("[type: menu][name: back]", "halfMenuItem", background.treeNode, "[vertical: bottom][horizontal: left]", 15, 15, 4, 173, 40);
        back.addText( new TextComponent("Back", 10, "[vertical: center][horizontal: center]", 0, 0));

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
            newItem.list = new MenuItem(itemName, "listItem", background.treeNode, "[vertical: top][horizontal: left]", 30, 0, 2, 353, 71);
            newItem.play = new MenuItem(playName, "play", newItem.list.treeNode, "[vertical: center][horizontal: right]", -55, 0, 2, 40, 40);
            newItem.delete = new MenuItem(deleteName, "loadGameDelete", newItem.list.treeNode, "[vertical: center][horizontal: right]", -7, 0, 2, 40, 40);

            //add the text
            newItem.list.addText(new TextComponent(worldName, 10, "[vertical: top][horizontal: left]", 8, 7));
            newItem.list.addText(new TextComponent("Size: " + numChunks, 10, "[vertical: center][horizontal: left]", 8, 0));
            newItem.list.addText(new TextComponent("Date Created: " + dateCreated, 10, "[vertical: bottom][horizontal: left]", 8, 8));

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
        if(scrollBar != null) {

            int barTop = menuTop;
            int barBot = menuBottom - itemHeight + 6;
            float adjPs = menuTop + (barBot - menuTop) * (scrollIndex / upperBound);

            scrollBar.xOffset = -32;
            scrollBar.yOffset = (int)adjPs;
            scrollBar.positionItem();
        }
    }

    private void toggleDeleteCheck(String name, boolean onOff) {

        if(onOff) {
            deleteCheck = new areYouSure();
            deleteCheck.background = new MenuItem("[type: menu][name: areYouSure][id: back]", "createGameBack", background.treeNode, "[vertical: center][horizontal: center]", 0, 0, 5, 360, 140);
            deleteCheck.background.addText(new TextComponent("Type " + name + " to delete", 10, "[vertical: top][horizontal: center]", 0, -10));

            deleteCheck.input = new MenuItem("[type: menu][name: input]", "menuItem", deleteCheck.background.treeNode, "[vertical: top][horizontal: center]", 0, 50, 6, 350, 40);
            TextComponent textDisplay = new TextComponent("", 10, "[vertical: center][horizontal: left]", 60, 0);
            TextInput textInput = new TextInput(textDisplay, 30);
            deleteCheck.input.addText(new TextComponent("Name: ", 10, "[vertical: center][horizontal: left]", 10, 0));
            deleteCheck.input.addText(textDisplay);
            deleteCheck.input.ent.addComponent(textInput);

            deleteCheck.no = new MenuItem("[type: menu][name: no]", "halfMenuItem", deleteCheck.background.treeNode, "[vertical: bottom][horizontal: left]", 5, 10, 6, 173, 40);
            deleteCheck.no.addText(new TextComponent("Go Back", 10, "[vertical: center][horizontal: center]", 0, 0));

            deleteCheck.yes = new MenuItem("[type: menu][name: yes]", "halfMenuItem", deleteCheck.background.treeNode, "[vertical: bottom][horizontal: right]", -5, 10, 6, 173, 40);
            deleteCheck.yes.addText(new TextComponent("Delete", 10, "[vertical: center][horizontal: center]", 0, 0));
        }

        else deleteCheck = null;
    }

    private void updateBackground() {

        //update the scrolling ========================================
        if(InputAL.isKeyPressed("down")) {
            scrollIndex -= scrollPerFrame;
            positionItems();
        }

        if(InputAL.isKeyPressed("up")) {
            scrollIndex += scrollPerFrame;
            positionItems();
        }

        //update the play and delete button ============================
        int mouseY = InputAL.getMouseY();
        for(listItem item: listItems) {
            if(mouseY > menuTop && mouseY < menuBottom && item.play.isLeftClicked()) { }
            if(mouseY > menuTop && mouseY < menuBottom && item.delete.isLeftClicked()) { toggleDeleteCheck(item.name,true); }
        }


        //back button
        if(back.isLeftClicked()) World.setCurrentState("mainMenu");
    }

    private void updateDeleteMenu() {



    }

    public void update(Entity entity) {



        if(deleteCheck == null) updateBackground();
        else updateDeleteMenu();



        /*
        if(scrollBar.hover() && InputAL.isMousePressed("mouse left")) {

            int y = InputAL.getMouseY();

            float total = (float)(menuBottom - menuTop);
            float slider = (float)(y - menuTop);
            float percent = slider / total;

            System.out.println( percent );

        }

         */









    }

}

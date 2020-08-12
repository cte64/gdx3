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
    private float scrollPerFrame = 0.01f;
    private int itemHeight = 70;
    private int menuTop = 105;
    private int menuBottom = 526;


    private class listItem {
        public MenuItem list;
        public MenuItem play;
        public MenuItem delete;
        public String name;
        public listItem() {}
        public void delete() {
            World.entitiesToBeDeleted.add(list.ent);
            World.entitiesToBeDeleted.add(play.ent);
            World.entitiesToBeDeleted.add(delete.ent);
        }
    }

    private class areYouSure {
        public String name;
        public MenuItem background;
        public MenuItem input;
        public MenuItem yes;
        public MenuItem no;
        public TextInput input_text;
        public areYouSure() {
            background = null;
            input = null;
            yes = null;
            no = null;
            input_text = null;
        }
    }


    areYouSure deleteCheck;
    ArrayList<listItem> listItems = new ArrayList<listItem>();

    public LoadGame() {

        type = "logic";

        int padding = 5;
        int yOff = padding + 50;
        scrollIndex = 0.0f;

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
            newItem.delete = new MenuItem(deleteName, "loadGameDeleteClosed1", newItem.list.treeNode, "[vertical: center][horizontal: right]", -7, 0, 2, 40, 40);

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

        scrollIndex = MathUtils.clamp(scrollIndex, 0.0f, 1.0f);
        int totalAmount = (listItems.size() - 1) * itemHeight * -1;
        int scrollOffset = (int)(totalAmount * scrollIndex);
        scrollOffset = MathUtils.clamp(scrollOffset, totalAmount, 0);

        //Position the game files in a list order =====================================
        for(int x = 0; x < listItems.size(); x++)  {

            int yPos = (int) (menuTop + scrollOffset + (x * itemHeight));
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
            int adjPs = (int)(menuTop + (barBot - menuTop) * scrollIndex);
            adjPs = MathUtils.clamp(adjPs, menuTop, menuBottom - itemHeight + 6);

            scrollBar.xOffset = -32;
            scrollBar.yOffset = (int)adjPs;
            scrollBar.positionItem();
        }
    }

    private void toggleDeleteCheck(String name, boolean onOff) {

        if(onOff) {
            deleteCheck = new areYouSure();
            deleteCheck.background = new MenuItem("[type: menu][subType: areYouSure][id: back]", "createGameBack", background.treeNode, "[vertical: center][horizontal: center]", 0, 0, 5, 360, 140);
            deleteCheck.background.addText(new TextComponent("Type \"" + name + "\" to delete", 10, "[vertical: top][horizontal: center]", 0, -10));

            deleteCheck.input = new MenuItem("[type: menu][subType: areYouSure][id: input]", "menuItem", deleteCheck.background.treeNode, "[vertical: top][horizontal: center]", 0, 50, 6, 350, 40);
            TextComponent textDisplay = new TextComponent("", 10, "[vertical: center][horizontal: left]", 60, 0);
            TextInput textInput = new TextInput(textDisplay, 30);
            deleteCheck.input.addText(new TextComponent("Name: ", 10, "[vertical: center][horizontal: left]", 10, 0));
            deleteCheck.input.addText(textDisplay);
            deleteCheck.input.ent.addComponent(textInput);
            deleteCheck.input_text = textInput;
            deleteCheck.name = name;

            deleteCheck.no = new MenuItem("[type: menu][subType: areYouSure][id: no]", "halfMenuItem", deleteCheck.background.treeNode, "[vertical: bottom][horizontal: left]", 5, 10, 6, 173, 40);
            deleteCheck.no.addText(new TextComponent("Go Back", 10, "[vertical: center][horizontal: center]", 0, 0));
        }

        else {
            State.deleteType("subType", "areYouSure");
            deleteCheck = null;
        }
    }

    private void updateBackground() {

        //update the scrolling ========================================
        if(InputAL.isKeyPressed("down")) {
            scrollIndex += scrollPerFrame;
            positionItems();
        }

        if(InputAL.isKeyPressed("up")) {
            scrollIndex -= scrollPerFrame;
            positionItems();
        }

        for(Integer i: InputAL.scrollQueue) {
            if(i == -1) scrollIndex -= scrollPerFrame;
            if(i == 1) scrollIndex += scrollPerFrame;
            positionItems();
        }


        //update the play and delete button ============================
        int mouseY = InputAL.getMouseY();
        for(listItem item: listItems) {

            //load game hover action
            if(item.delete.hover()) item.delete.ent.spriteName = "loadGameDeleteOpen";
            else item.delete.ent.spriteName = "loadGameDeleteClosed1";

            //play game hover action
            if(item.play.hover()) item.play.ent.spriteName = "playHover";
            else item.play.ent.spriteName = "play";

            //click action update
            if(mouseY > menuTop && mouseY < menuBottom) {
                if(item.play.isLeftClicked()) {
                    FileSystem.setGameSubDirectory(item.name);
                    World.setCurrentState("[action: play]");
                }
                if(item.delete.isLeftClicked()) { toggleDeleteCheck(item.name,true); }
            }
        }



        //back button
        if(back.isLeftClicked()) World.setCurrentState("mainMenu");
    }

    private void updateDeleteMenu() {

        if(deleteCheck == null) return;

        //delete
        String text = deleteCheck.input_text.getText();
        if(text.equals(deleteCheck.name) && deleteCheck.yes == null) {
            deleteCheck.yes = new MenuItem("[type: menu][subType: areYouSure][id: yes]", "halfMenuItem", deleteCheck.background.treeNode, "[vertical: bottom][horizontal: right]", -5, 10, 6, 173, 40);
            deleteCheck.yes.addText(new TextComponent("Delete", 10, "[vertical: center][horizontal: center]", 0, 0));
        }

        else if (!text.equals(deleteCheck.name) && deleteCheck.yes != null) {
            World.entitiesToBeDeleted.add(deleteCheck.yes.ent);
            deleteCheck.yes = null;
        }

        //bottom buttons
        if(deleteCheck.yes != null && deleteCheck.yes.isLeftClicked()) {
            for(listItem item: listItems) {
                if(item.name.equals(deleteCheck.name)) {
                    FileSystem.deleteDirectory(item.name);
                    item.delete();
                    listItems.remove(item);
                    toggleDeleteCheck("", false);
                    positionItems();
                    break;
                }
            }
        }

        if(deleteCheck.no.isLeftClicked()) { toggleDeleteCheck("", false); }
    }

    public void update(Entity entity) {
        if(deleteCheck == null) updateBackground();
        else updateDeleteMenu();
    }

}

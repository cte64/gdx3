package gameCode.Menus;

import gameCode.Infastructure.*;
import gameCode.Utilities.MathUtils;
import gameCode.Utilities.StringUtils;

import java.util.ArrayList;

public class LoadGame extends Component {


    MenuManager menu;

    private float scrollIndex;

    boolean pause;

    public class listItem {
        public String list;
        public String play;
        public String delete;
        public String name;
        public listItem() {}
        public void delete() {
            World.entitiesToBeDeleted.add(menu.getEnt(list));
            World.entitiesToBeDeleted.add(menu.getEnt(play));
            World.entitiesToBeDeleted.add(menu.getEnt(delete));
        }
    }

    public String background;
    private String overlay;
    private String back;
    private String scrollBar;
    private String loadGame;

    private ScrollList scrollList;

    ArrayList<listItem> listItems = new ArrayList<listItem>();
    boolean paused;

    public LoadGame() {

        type = "logic";

        menu = new MenuManager();
        scrollList = new ScrollList(menu);
        scrollList.top = 105;
        scrollList.left = 30;
        scrollList.scrollPerFrame = 0.01f;
        scrollList.itemHeight = 70;
        scrollList.itemWidth = 353;
        scrollList.width = 1;
        scrollList.bottom = 526;

        background = "[type: menu][name: background]";
        overlay = "[type: menu][name: overlay]";
        back = "[type: menu][name: back]";
        scrollBar = "[type: menu][name: scrollBar]";
        loadGame = "[type: menu][name: loadGame]";

        //background
        menu.registerItem(background, "loadGameBack", null, "[vertical: center][horizontal: center]", 0, 0, 1);
        menu.registerItem(overlay, "loadGameFront", background, "[vertical: center][horizontal: center]", 0, 0, 4);
        menu.addText(overlay, new TextComponent("Load Game", 10, "[vertical: top][horizontal: center]", 0, 0));

        //scroll bar
        menu.registerItem(scrollBar, "scrollBar", background, "[vertical: top][horizontal: right]", -32, 0, 5);
        scrollList.scrollBar = scrollBar;

        //bottom buttons
        menu.registerItem(back, "halfMenuItem", background, "[vertical: bottom][horizontal: left]", 15, 15, 5);
        menu.addText(back, new TextComponent("Back", 10, "[vertical: center][horizontal: center]", 0, 0));

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
            newItem.list = itemName; menu.registerItem(itemName, "listItem", background, "[vertical: top][horizontal: left]", 30, 0, 2);
            newItem.play = playName; menu.registerItem(playName, "play", newItem.list, "[vertical: center][horizontal: right]", -55, 0, 3);
            newItem.delete = deleteName; menu.registerItem(deleteName, "loadGameDeleteClosed1", newItem.list, "[vertical: center][horizontal: right]", -7, 5, 3);

            //add the text
            menu.addText(itemName, new TextComponent(worldName, 10, "[vertical: top][horizontal: left]", 8, 7));
            menu.addText(itemName, new TextComponent("Size: " + numChunks, 10, "[vertical: center][horizontal: left]", 8, 0));
            menu.addText(itemName, new TextComponent("Date Created: " + dateCreated, 10, "[vertical: bottom][horizontal: left]", 8, 8));

            listItems.add(newItem);
            scrollList.addItem(newItem.list);
        }
    }

    private void toggleDeleteOn(String directory) {
        Entity ent = new Entity();
        ent.entityName = "[type: menu][subType: deleteGame][id: manager]";
        ent.drawMode = "hud";
        ent.deleteRange = -2;
        ent.addComponent(new DeleteGameMenu(directory, this));
        World.entitiesToBeAdded.add(ent);
        pause = true;
    }

    public void toggleDeleteOff() {
        State.deleteType("subType", "deleteGame");
        pause = false;
    }

    public void deleteWorld(String directory) {

        for(listItem item: listItems) {
            if(item.name.equals(directory)) {
                //FileSystem.deleteDirectory(item.directory);
                //scrollList.deleteListItem(item.list);
                //item.delete();
                //scrollList.deleteListItem(item.list);
                //listItems.remove(item);
                //listItems.remove(item.name);
                //System.out.println();
               // scrollList.deleteListItem(item.list);
                scrollList.deleteListItem(item.list);
                listItems.remove(item);
                scrollList.updateList2();
                break;
            }
        }

        State.deleteType("subType", "deleteGame");
        pause = false;
    }

    public void update(Entity entity) {

        if(paused) return;

        scrollList.updateList2();

        //update the play and delete button ============================
        int mouseY = InputAL.getMouseY();
        for(listItem item: listItems) {

            //load game hover action
            if(menu.hover(item.delete)) menu.getEnt(item.delete).spriteName = "loadGameDeleteOpen";
            else menu.getEnt(item.delete).spriteName = "loadGameDeleteClosed1";

            //play game hover action
            if(menu.hover(item.play)) menu.getEnt(item.play).spriteName = "playHover";
            else menu.getEnt(item.play).spriteName = "play";

            //click action update
            if(mouseY > scrollList.top && mouseY < scrollList.bottom) {
                if(menu.isLeftClicked(item.play)) {
                    StringUtils newState = new StringUtils("[action: play][directory: ]");
                    StringUtils.setField(newState, "directory", item.name);
                    State.play(newState.data);
                }
                if(menu.isLeftClicked(item.delete)) {
                    toggleDeleteOn(item.name);
                }
            }
        }

        //back button
        if(menu.isLeftClicked(back)) State.mainMenu();
    }
}

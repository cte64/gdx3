package gameCode.Menus.MenuScreens;

import com.mygdx.game.Engine;
import com.mygdx.game.InputAL;
import gameCode.Infrastructure.*;
import gameCode.Menus.MenuManager;
import gameCode.Menus.ScrollList;
import gameCode.Menus.TextComponent;
import gameCode.Utilities.StringUtils;

import java.util.ArrayList;

public class LoadGame extends Component {


    MenuManager menu;

    private float scrollIndex;

    public class listItem {
        public String list;
        public String play;
        public String delete;
        public String name;
        public listItem() {}
        public void delete() {
            World.get().entitiesToBeDeleted.add(menu.getEnt(list));
            World.get().entitiesToBeDeleted.add(menu.getEnt(play));
            World.get().entitiesToBeDeleted.add(menu.getEnt(delete));
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
        scrollList.scrollPixPerSecond = 350;
        scrollList.width = 1;
        scrollList.bottom = 513;

        background = "[type: menu][name: background]";
        overlay = "[type: menu][name: overlay]";
        back = "[type: menu][name: back]";
        scrollBar = "[type: menu][name: scrollBar]";
        loadGame = "[type: menu][name: loadGame]";

        //background
        menu.registerItem(background, "loadGameBack", null, "[vertical: center][horizontal: center]", 0, 0, 1);
        menu.registerItem(overlay, "loadGameFront", background, "[vertical: center][horizontal: center]", 0, 0, 4);
        menu.addText(overlay, new TextComponent("Load Game", 30, "[vertical: top][horizontal: center]", 0, 0));

        //scroll bar
        menu.registerItem(scrollBar, "scrollBar", background, "[vertical: top][horizontal: right]", -32, 0, 5);
        scrollList.scrollBar = scrollBar;
        scrollList.parent = background;

        //bottom buttons
        menu.registerItem(back, "halfMenuItem", background, "[vertical: bottom][horizontal: left]", 15, 15, 5);
        menu.addText(back, new TextComponent("Back", 20, "[vertical: center][horizontal: center]", 0, 0));

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
            menu.addText(itemName, new TextComponent(worldName, 22, "[vertical: top][horizontal: left]", 8, 7));
            menu.addText(itemName, new TextComponent("Size: " + numChunks, 16, "[vertical: center][horizontal: left]", 8, 0));
            menu.addText(itemName, new TextComponent("Date Created: " + dateCreated, 16, "[vertical: bottom][horizontal: left]", 8, 8));

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
        World.get().entitiesToBeAdded.add(ent);
        paused = true;
    }

    public void toggleDeleteOff() {
        State.deleteType("subType", "deleteGame");
        paused = false;
    }

    public void deleteWorld(String directory) {

        for(listItem item: listItems) {
            if(item.name.equals(directory)) {
                //FileSystem.deleteDirectory(item.directory);
                scrollList.deleteListItem(item.list);
                listItems.remove(item);
                scrollList.updateList2();
                break;
            }
        }

        State.deleteType("subType", "deleteGame");
        paused = false;
    }

    public void update(Entity entity) {

        if(paused) return;

        scrollList.updateList2();

        //update the play and delete button ============================
        int mouseY = Engine.get().getInput().getMouseY();
        for(listItem item: listItems) {

            //load game hover action
            //if(scrollList.hover(item.delete)) menu.getEnt(item.delete).spriteName = "loadGameDeleteOpen";
            //else menu.getEnt(item.delete).spriteName = "loadGameDeleteClosed1";
            menu.hoverAction(item.delete, "[hoverType: sine][amplitude: 0.05][sprite: loadGameDeleteClosed1][hoverSprite: loadGameDeleteOpen]");

            //play game hover action
            //if(scrollList.hover(item.play)) menu.getEnt(item.play).spriteName = "playHover";
            //else menu.getEnt(item.play).spriteName = "play";
            menu.hoverAction(item.play, "[hoverType: sine][amplitude: 0.05][frequency: 1]");

            //click stuff ========================================================
            if(scrollList.isLeftClicked(item.play)) {
                StringUtils newState = new StringUtils("[action: play][directory: ]");
                StringUtils.setField(newState, "directory", item.name);
                State.play(newState.data);
            }

            if(scrollList.isLeftClicked(item.delete)) {
                toggleDeleteOn(item.name);
            }
        }

        //back button
        if(menu.isLeftClicked(back)) State.mainMenu();
        menu.hoverAction(back, "[hoverType: toggle][fontSize: 1][scale: 1.006]");
    }
}

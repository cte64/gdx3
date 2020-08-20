package gameCode.Menus;

import gameCode.Infastructure.Component;
import gameCode.Infastructure.Entity;
import gameCode.Infastructure.State;
import gameCode.Infastructure.World;
import gameCode.Utilities.StringUtils;
import java.util.ArrayList;



public class InventoryManager extends Component {

    private final int invenX = 7;
    private final int invenY = 5;
    private final int padding = 2;
    int selected;
    int fontSize;
    String pauseState;

    private class itemNode {
        public MenuItem tile;
        public MenuItem item;
        int itemCount;
        public itemNode() {
            itemCount = 0;
            tile = null;
            item = null;
        }
    }

    ArrayList<itemNode> nodes = new ArrayList<itemNode>();
    ArrayList<itemNode> crafting = new ArrayList<itemNode>();
    itemNode craftedItem;
    itemNode clipboard;

    private MenuItem background;

    public InventoryManager() {
        type = "logic";

        //background that anchors all the items
        background = new MenuItem("[type: inventory][subType: background]", null, null, "[vertical: bottom][horizontal: left]", 0, 0, 4, 500, 100);
        pauseState = StringUtils.getField(State.getState(), "action");

        int width = 52;

        //Inventory items ============================================================================
        for(int y = 0; y<invenY; y++) {
        for(int x = 0; x<invenX; x++) {
            itemNode node = new itemNode();

            StringUtils nodeName = new StringUtils("[type: inventory][subType: inventoryTray][xPos: ][yPos: ]");
            StringUtils.setField(nodeName, "xPos", StringUtils.toString(x));
            StringUtils.setField(nodeName, "yPos", StringUtils.toString(y));

            int xPos = padding + x*(padding + width);
            int yPos = padding + y*(padding + width);

            node.tile = new MenuItem(nodeName.data, "inventoryTray", background.treeNode, "[vertical: bottom][horizontal: left]", xPos, yPos, 5, 52, 52);
            nodes.add(node);
        }}


        /*

        //Crafting slots =============================================================================
        for(int y = 0; y<3; y++) {
        for(int x = 0; x<3; x++) {
            itemNode node = new itemNode();

            StringUtils nodeName = new StringUtils("[type: inventory][subType: craftingTray][xPos: ][yPos: ]");
            StringUtils.setField(nodeName, "xPos", StringUtils.toString(x));
            StringUtils.setField(nodeName, "yPos", StringUtils.toString(y));

            int xPos = padding + (x + invenX)*(padding + width);
            int yPos = padding + y*(padding + width);

            node.menuItem = new MenuItem(nodeName.data, "inventoryTray", background.treeNode, "[vertical: bottom][horizontal: left]", xPos, yPos, 5, 52, 52);
            crafting.add(node);
        }}

        //Crafted Item ================================================================================
        craftedItem = new itemNode();
        StringUtils nodeName = new StringUtils("[type: inventory][subType: craftedTray][details: ]");
        int xPos = padding + (3 + invenX)*(padding + width);
        int yPos = padding;
        craftedItem.menuItem = new MenuItem(nodeName.data, "inventoryTray", background.treeNode, "[vertical: bottom][horizontal: left]", xPos, yPos, 5, 52, 52);

        //Clipboard ===================================================================================
        clipboard = new itemNode();

         */
    }

    public void pauseAction() {


        String newAction = StringUtils.getField(State.getState(), "action");
        if(!pauseState.equals(newAction)) {
            pauseState = newAction;

            //Hide Inventory Items ===================================================
            for(int y = 1; y<invenY; y++) {
            for(int x = 0; x<invenX; x++) {
                MenuItem nodePtr = nodes.get((y * invenX) + x).tile;
                if(pauseState.equals("play")) nodePtr.updateDrawMode("hidden");
                if(pauseState.equals("paused")) nodePtr.updateDrawMode("hud");
            }}

            /*
            //Inventory Items ========================================================
            for(int y = 1; y<invenY; y++) {
                for(int x = 0; x<invenX; x++) {

                    Entity *tray = world.getEntByName(nodes[y][x].tray);
                    if(tray != nullptr) {
                        if(world.currentState == "play") tray->drawable = false;
                        if(world.currentState == "paused") tray->drawable = true;
                    }

                    Entity *item = world.getEntByName(nodes[y][x].itemType);
                    if(item != nullptr) {
                        if(world.currentState == "play") item->drawable = false;
                        if(world.currentState == "paused") item->drawable = true;
                    }
                }
            }

             */




        }

    }

    private void switchWithClipboard() {

    }

    private void leftClick() {

        for(itemNode node: nodes) {
            if(node.tile.isLeftClicked()) {

            }
        }

    }

    public void update(Entity entity) {


        pauseAction();
        leftClick();



    }
}

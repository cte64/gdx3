package gameCode.Menus;

import gameCode.Infastructure.*;
import gameCode.Utilities.MathUtils;
import gameCode.Utilities.StringUtils;
import gameCode.Utilities.myPair;

import java.util.ArrayList;



public class InventoryManager extends Component {


    private final int invenX = 8;
    private final int invenY = 5;
    private final int padding = 1;
    int selected;
    int fontSize;
    String pauseState;

    MenuManager menu;

    private class itemNode {
        public String tile;
        public String item;
        int itemCount;
        public itemNode() {
            itemCount = 0;
            tile = "";
            item = "";
        }
    }

    ArrayList<itemNode> nodes = new ArrayList<itemNode>();
    ArrayList<itemNode> crafting = new ArrayList<itemNode>();
    ArrayList<String> uniqueNames = new ArrayList<String>();
    itemNode craftedItem;
    itemNode clipboard;

    private String background;

    public InventoryManager() {
        type = "logic";

        int width = 52;
        menu = new MenuManager();
        pauseState = StringUtils.getField(State.getState(), "action");


        //unique names =====================================================================================
        int numNames = (invenX * invenY);
        for(int x = 0; x < numNames; x++) {
            String name = StringUtils.toString(x);
            uniqueNames.add(name);
        }

        //background that anchors all the items
        background = "[type: inventory][subType: background]";
        menu.registerItem(background, null, null, "[vertical: bottom][horizontal: left]", 0, 0, 4);

        //Inventory items ============================================================================
        for(int y = 0; y<invenY; y++) {
        for(int x = 0; x<invenX; x++) {
            itemNode node = new itemNode();


            StringUtils nodeName = new StringUtils("[type: inventory][subType: inventoryTray][xPos: ][yPos: ]");
            StringUtils.setField(nodeName, "xPos", StringUtils.toString(x));
            StringUtils.setField(nodeName, "yPos", StringUtils.toString(y));

            int xPos = padding + x*(padding + width);
            int yPos = padding + y*(padding + width);


            menu.registerItem(nodeName.data, "inventoryTray", background, "[vertical: bottom][horizontal: left]", xPos, yPos, 5);
            node.tile = nodeName.data;
            nodes.add(node);

        }}


        addItem("[type: item][subType: banana]", 1, new myPair(0, 0));



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

    public String createItem(String name) {

        StringUtils newName = new StringUtils("[type: inventoryItem][subType: ][id: ]");
        StringUtils.setField(newName, "subType", name);
        StringUtils.setField(newName, "id", uniqueNames.get(0));
        uniqueNames.remove(0);

        menu.registerItem(newName.data, name, null, "[vertical: center][horizontal: left]", 0, 0, 8);
        return newName.data;
    }

    public void addItem(String name, int amount, myPair<Integer, Integer> slot) {

        String nameType = StringUtils.getField(name, "subType");
        amount = MathUtils.clamp(amount, 1, 1000);

        int slotX = slot.first;
        int slotY = slot.second;

        if (slotX < 0 || slotY < 0) {
            for (int y = 0; y < invenY; y++) {
            for (int x = 0; x < invenX; x++) {
                String itemSubType = StringUtils.getField(nodes.get(y * invenX + x).item, "subType");
                if (nameType.equals(itemSubType)) {
                    nodes.get(y * invenX + x).itemCount += amount;
                    return;
                }
            }}

            outer:
            for (int y = 0; y < invenY; y++) {
            for (int x = 0; x < invenX; x++) {
                if (nodes.get(y * invenX + x).item == null) {
                    slotX = x;
                    slotY = y;
                    break outer;
                }
            }}

            return;
        }


        if (nodes.get(slotY * invenX + slotX).item == "" && nodes.get(slotY * invenX + slotX).itemCount == 0) {
            nodes.get(slotY * invenX + slotX).item = createItem(nameType);
            nodes.get(slotY * invenX + slotX).itemCount = amount;
            menu.setParent(nodes.get(slotY * invenX + slotX).item, nodes.get(slotY * invenX + slotX).tile);
        }
    }

    public void pauseAction() {


            /*
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





        }
             */

    }

    private void switchWithClipboard(itemNode node) {



        //itemNode temp = node;






    }

    private void leftClick() {


        /*
        for(itemNode node: nodes) {
            if(node.tile.isLeftClicked()) {
                switchWithClipboard(node);
            }
        }

         */

    }

    public void update(Entity entity) {


        pauseAction();
        leftClick();



    }


}

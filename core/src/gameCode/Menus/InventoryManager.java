package gameCode.Menus;

import gameCode.Infrastructure.*;
import gameCode.Utilities.MathUtils;
import gameCode.Utilities.StringUtils;

import java.util.ArrayList;

public class InventoryManager extends Component {

    //constants and lucky numbers =============================================
    private final int currentNumItems = 8;
    private final int itemWidth = 52;
    private final int padding = 1;
    private int maxInvenItems = 56;

    //important stuff =========================================================
    MenuManager menu;
    ScrollList scrollList;
    String scrollBar;
    String textBack;
    private class itemNode {
        public String tile;
        public String item;
        int itemCount;

        public void setItemCount(int count) {
            itemCount = count;

            Entity ent = menu.getEnt(item);
            if(ent == null) return;




        }
        public itemNode() {
            itemCount = 0;
            tile = "";
            item = "";
        }
    }
    private boolean iToggle;

    //lists and containers ====================================================
    ArrayList<itemNode> inventoryItems;
    itemNode[] currentItems;
    itemNode[] craftingTray;
    itemNode[] equippedItems;
    itemNode clipboard;
    itemNode craftedItem;

    //ids of menu items =======================================================
    String background;
    String foreground;

    public InventoryManager() {

        type = "logic";

        currentItems = new itemNode[currentNumItems];
        inventoryItems = new ArrayList<itemNode>();
        craftingTray = new itemNode[9];
        equippedItems = new itemNode[4];
        clipboard = new itemNode();
        craftedItem = new itemNode();

        iToggle = false;
        menu = new MenuManager();
        scrollList = new ScrollList(menu);
        scrollList.left = 20;
        scrollList.top = 55;
        scrollList.bottom = 374;
        scrollList.itemHeight = 52;
        scrollList.scrollPerFrame = 0.003f;
        scrollList.itemWidth = 52;
        scrollList.width = 6;
        scrollList.padding = 1;

        //set the clipboard
        clipboard.tile = "[type: inventory][subType: clipboard]";
        menu.registerItem(clipboard.tile, null, null, "[vertical: mouse][horizontal: mouse]", 0, 0, 9);

        //set up the background and foreGround
        background = "[type: inventory][subType: background]";
        foreground = "[type: inventory][subType: foreground]";
        scrollBar = "[type: inventory][subType: scrollBar]";
        textBack = "[type: inventory][subType: textBack]";
        menu.registerItem(background, "invenBack", null, "[vertical: center][horizontal: center]", 0, 0, 4);
        menu.registerItem(foreground, "invenFront", background, "[vertical: center][horizontal: left]", 0, 0, 7);
        menu.registerItem(scrollBar, "invenScrollBar", foreground, "[vertical: top][horizontal: center]", 50, 0, 8);
        menu.registerItem(textBack, "invenTextBack", foreground, "[vertical: top][horizontal: right]", -10, 25, 8);
        scrollList.scrollBar = scrollBar;

        //inventory selection part ================================================

        /*
        for(int x = 0; x < currentNumItems; x++) {
            StringUtils nodeName = new StringUtils("[type: inventory][subType: inventoryCurrentTray][index: ]");
            StringUtils.setField(nodeName, "index", StringUtils.toString(x));

            int xPos = 10 + x*(itemWidth + padding);
            int yPos = 10;

            itemNode node = new itemNode();
            menu.registerItem(nodeName.data, "inventoryTray", null, "[vertical: bottom][horizontal: left]", xPos, yPos, 8);
            node.tile = nodeName.data;
            currentItems[x] = node;
        }

         */

        //Inventory Items  ========================================================
        for(int x = 0; x < maxInvenItems; x++) {
            StringUtils nodeName = new StringUtils("[type: inventory][subType: inventoryHidden][index: ]");
            StringUtils.setField(nodeName, "index", StringUtils.toString(x));

            int xPos = scrollList.left + x*(itemWidth + padding);
            int yPos = scrollList.top;

            itemNode node = new itemNode();
            node.tile = nodeName.data;

            menu.registerItem(nodeName.data, "inventoryTray", background, "[vertical: top][horizontal: left]", xPos, yPos, 5);
            scrollList.addItem(nodeName.data);
            inventoryItems.add(node);
        }

        //Crafting Trays ==========================================================
        for(int x = 0; x < craftingTray.length; x++) {
            StringUtils nodeName = new StringUtils("[type: inventory][subType: craftingTray][index: ]");
            StringUtils.setField(nodeName, "index", StringUtils.toString(x));

            int xPos = 389 + (x % 3)*(itemWidth + padding);
            int yPos = 25 + padding + (x / 3)*(itemWidth + padding);

            itemNode node = new itemNode();
            node.tile = nodeName.data;

            menu.registerItem(nodeName.data, "inventoryTray", background, "[vertical: bottom][horizontal: left]", xPos, yPos, 9);
            craftingTray[x] = node;
        }

        //Equipped Trays ==========================================================
        for(int x = 0; x < equippedItems.length; x++) {
            StringUtils nodeName = new StringUtils("[type: inventory][subType: equippedTrays][index: ]");
            StringUtils.setField(nodeName, "index", StringUtils.toString(x));

            int xPos = 0;
            int yPos = x*(itemWidth + padding);;

            itemNode node = new itemNode();
            node.tile = nodeName.data;

            menu.registerItem(nodeName.data, "inventoryTray", textBack, "[vertical: top][horizontal: left]", xPos, yPos, 9);
            equippedItems[x] = node;
        }

        //Crafted Item ============================================================

        craftedItem.tile = "[type: inventory][subType: craftedItem]";
        menu.registerItem(craftedItem.tile, "inventoryTray", foreground, "[vertical: bottom][horizontal: right]", -10, 80, 9);




        /*
        //this is also a test ======================================================
        addItem("[type: banana][amount: 1][preferredSlot: inventory][preferredSlotIndex: 1]");
        addItem("[type: apple][amount: 1][preferredSlot: inventory][preferredSlotIndex: 8]");
        addItem("[type: watermelon][amount: 1][preferredSlot: inventory][preferredSlotIndex: 2]");
        addItem("[type: asparagus][amount: 1][preferredSlot: inventory][preferredSlotIndex: 3]");
        addItem("[type: potato][amount: 1][preferredSlot: inventory][preferredSlotIndex: 4]");

         */

        //Hide all the items by default ============================================
        menu.updateDrawMode(background, "hidden");
    }

    public String createItem(String name) {

        StringUtils newName = new StringUtils("[type: inventoryItem][subType: ][id: ]");
        StringUtils.setField(newName, "subType", name);

        menu.registerItem(newName.data, name, null, "[vertical: center][horizontal: center]", 0, 0, 6);
        menu.addText(newName.data, new TextComponent("0", 10, "[vertical: bottom][horizontal: center]", 0, 15));

        return newName.data;
    }

    private void clipboardSwap(itemNode node) {

        String oldClipItem = clipboard.item;

        menu.setParent(node.item, clipboard.tile);
        Entity nodeEnt = menu.getEnt(node.item);
        if(nodeEnt != null) nodeEnt.z_pos = 10;
        clipboard.item = node.item;

        menu.setParent(oldClipItem, node.tile);
        Entity clipEnt = menu.getEnt(oldClipItem);
        Entity tile = menu.getEnt(node.tile);
        if(clipEnt != null && tile != null) clipEnt.z_pos = tile.z_pos + 1;
        node.item = oldClipItem;
    }

    private void leftClick() {


        /*

        for(itemNode node: currentItems) {
            if(menu.isLeftClicked(node.tile)) {
                clipboardSwap(node);
            }
        }

        for(itemNode node: inventoryItems) {
            if(scrollList.isLeftClicked(node.tile)) {
                clipboardSwap(node);
            }
        }

        for(itemNode node: craftingTray) {
            if(scrollList.isLeftClicked(node.tile)) {
                clipboardSwap(node);
            }
        }

        for(itemNode node: equippedItems) {
            if(scrollList.isLeftClicked(node.tile)) {
                clipboardSwap(node);
            }
        }

         */

    }

    public void addItem(String itemToAdd) {


        /*FORMAT ======= [type: ][amount: ][preferredSlot: ][preferredSlotIndex: ]
         preferredSlot can be "current" or "inventory"
         preferredSlotIndex can be -1 (take first available spot) or any number 0 and above
         */

        String typeToAdd = StringUtils.getField(itemToAdd, "type");
        String preferredSlot = StringUtils.getField(itemToAdd, "preferredSlot");

        String amountStr = StringUtils.getField(itemToAdd, "amount");
        int amount = MathUtils.clamp(StringUtils.stringToInt(amountStr), 0, 10000);

        String slotStr = StringUtils.getField(itemToAdd, "preferredSlotIndex");
        int preferredSlotIndex = MathUtils.clamp(StringUtils.stringToInt(slotStr), -1, 1000);

        /*

        This is for when preferredSlot is "current"

        If preferredSlotIndex is 0 or above, check to see if spot at preferredSlot and preferredSlotIndex is taken.
        If not taken put item there and return
        If taken by item of the same type, increment slot by amount and return
        If taken by item of different type, set preferredSlotIndex to -1 and call recursively

        If preferredSlotIndex is -1, loop from beginning to end and see if there are any items of same type.
        If there are, increment that slot by amount and return.
        If there are no items of the same type, loop from beginning and find first empty spot, put item there, and return
        If there are no empty spots, then set preferredSlot to "inventory" and preferredSlotIndex to -1 and call recursively
         */


        if(preferredSlot.equals("current")) {
            if(preferredSlotIndex >= 0 && preferredSlotIndex < currentNumItems) {
                String slotType = StringUtils.getField(currentItems[preferredSlotIndex].item, "subType");
                if(slotType.equals("")) {
                    String newName = createItem(typeToAdd);
                    currentItems[preferredSlotIndex].item = newName;
                    menu.setParent(newName, currentItems[preferredSlotIndex].tile);
                    return;
                }
                if(slotType.equals(itemToAdd)) {
                    currentItems[preferredSlotIndex].itemCount++;
                    return;
                }
                if(!slotType.equals(itemToAdd)){
                    String newStr = StringUtils.setField(itemToAdd, "preferredSlotIndex", "-1");
                    addItem(newStr);
                    return;
                }
            }
            if(preferredSlotIndex == -1) {
                for(itemNode node: currentItems) {
                    String nodeType = StringUtils.getField(node.item, "subType");
                    if(nodeType.equals(typeToAdd)) {
                        node.itemCount += amount;
                        return;
                    }
                }
                for(itemNode node: currentItems) {
                    if(node.item.equals("")) {
                        String newName = createItem(typeToAdd);
                        node.item = newName;
                        menu.setParent(newName, node.tile);
                        return;
                    }
                }
                String newName = StringUtils.setField(itemToAdd, "preferredSlot", "inventory");
                addItem(newName);
                return;
            }
        }


        /*
        This is for when preferredSlot is "inventory"

        If preferredSlotIndex is 0 and above, then check to see if spot at preferredSlotIndex in "inventory" is taken
        If it is not taken put item there and return
        If it is taken by item of the same type, increment by "amount" and return
        If it is taken by item of different type set preferredSlotIndex to -1 and call recursively

        If preferredSlotIndex is -1, then loop everything to find items of same type
        If we find slot with item of the same type, increment by "amount" and return
        If not, then append to inventoryList
         */


        if(preferredSlot.equals("inventory")) {

            if(preferredSlotIndex >= 0 && preferredSlotIndex < inventoryItems.size()) {
                String invenType = StringUtils.getField(inventoryItems.get(preferredSlotIndex).item, "subType");
                if(invenType.equals("")) {
                    String newName = createItem(typeToAdd);
                    inventoryItems.get(preferredSlotIndex).item = newName;
                    menu.setParent(newName, inventoryItems.get(preferredSlotIndex).tile);
                    return;
                }

                if(invenType.equals(typeToAdd)) {
                    inventoryItems.get(preferredSlotIndex).itemCount += amount;
                    return;
                }
            }

            if(preferredSlotIndex == -1) {

            }
        }

    }

    public void update(Entity entity) {

        Entity ent = menu.getEnt(background);
        if(ent == null) return;

        String worldState = StringUtils.getField(State.getState(), "action");

        if(!worldState.equals("paused")) {
            if(InputAL.isKeyPressed("i") && !iToggle) iToggle = true;
            if(!InputAL.isKeyPressed("i") && iToggle) {
                iToggle = false;
                if(ent.drawMode.equals("hidden")) {
                    menu.updateDrawMode(background, "hud");
                    State.setState("[action: inventory]");
                }
                else if(ent.drawMode.equals("hud")) {
                    menu.updateDrawMode(background, "hidden");
                    State.setState("[action: play]");
                }
            }
        }

        if(ent.drawMode.equals("hud")) {
            scrollList.updateList2();
            leftClick();
            menu.updateItem(clipboard.tile);
        }
    }
}

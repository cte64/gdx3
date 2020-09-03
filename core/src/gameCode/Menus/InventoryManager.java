package gameCode.Menus;

import gameCode.Infrastructure.*;
import gameCode.Utilities.MathUtils;
import gameCode.Utilities.StringUtils;
import gameCode.Utilities.myPair;
import org.w3c.dom.Text;

import java.util.ArrayList;

public class InventoryManager extends Component {

    //constants and lucky numbers =============================================
    private final int currentNumItems = 3;
    private final int itemWidth = 52;
    private final int padding = 1;
    private int maxInvenItems = 56;

    //important stuff =========================================================
    MenuManager menu;
    ScrollList scrollList;
    String scrollBar;
    String textBack;
    String currentBackground;
    private class itemNode {
        public String tile;
        public String item;
        private int itemCount;

        public void setItemCount(int count) {
            itemCount = count;
            Entity ent = menu.getEnt(item);
            if(ent == null) return;
            TextComponent tc = (TextComponent) ent.getComponent("text");
            if(tc != null) tc.setText(StringUtils.toString(count));
        }
        public int getItemCount() { return itemCount; }

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

        //set up the background and foreGround
        background = "[type: inventory][subType: background]";
        foreground = "[type: inventory][subType: foreground]";
        scrollBar = "[type: inventory][subType: scrollBar]";
        textBack = "[type: inventory][subType: textBack]";
        currentBackground = "[type: inventory][subType: currentBackground]";

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
        scrollList.bottom = 371;
        scrollList.parent = background;
        scrollList.scrollPixPerSecond = 330;
        scrollList.width = 6;
        scrollList.vPadding = 1;
        scrollList.hPadding = 1;

        //set the clipboard
        clipboard.tile = "[type: inventory][subType: clipboard]";
        menu.registerItem(clipboard.tile, null, null, "[vertical: mouse][horizontal: mouse]", 0, 0, 9);

        menu.registerItem(background, "invenBack", null, "[vertical: center][horizontal: center]", 0, 0, 4);
        menu.registerItem(foreground, "invenFront", background, "[vertical: center][horizontal: left]", 0, 0, 7);
        menu.registerItem(scrollBar, "invenScrollBar", foreground, "[vertical: top][horizontal: center]", 50, 0, 8);
        menu.registerItem(textBack, "invenTextBack", foreground, "[vertical: top][horizontal: right]", -10, 25, 8);
        menu.registerItem(currentBackground, "currentBackground", null, "[vertical: bottom][horizontal: center]", 0, 0, 6);
        scrollList.scrollBar = scrollBar;

        //inventory selection part ================================================
        for(int x = 0; x < currentNumItems; x++) {
            StringUtils nodeName = new StringUtils("[type: inventory][subType: inventoryCurrentTray][index: ]");
            StringUtils.setField(nodeName, "index", StringUtils.toString(x));

            int xPos = 2 + x*(itemWidth + padding);
            int yPos = 2;

            itemNode node = new itemNode();
            menu.registerItem(nodeName.data, "inventoryTray", currentBackground, "[vertical: bottom][horizontal: left]", xPos, yPos, 7);
            node.tile = nodeName.data;
            currentItems[x] = node;
        }


        /*
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
         */

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


        addItem("banana", "current", -1, 1);
        addItem("watermelon", "current", -1, 1);
        addItem("asparagus", "current", -1, 17);
        addItem("apple", "current", 0, 17);
        addItem("potato", "current", 0, 17);


        //Hide all the items by default ============================================
        menu.updateDrawMode(background, "hidden");
    }

    public String createItem(String name) {

        StringUtils newName = new StringUtils("[type: inventoryItem][subType: ][id: ]");
        StringUtils.setField(newName, "subType", name);

        //I will find a more elegant solution for this later
        myPair<Integer, Integer> comp = Graphics.getSpriteDimensions(name);
        int xComp = -(52 - comp.first) / 2 + 3;
        int yComp = -(52 - comp.second) / 2 + 3;

        menu.registerItem(newName.data, name, null, "[vertical: center][horizontal: center]", 0, 0, 7);
        menu.addText(newName.data, new TextComponent("0", 18, "[vertical: bottom][horizontal: left]", xComp, yComp));

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

    private void leftClick(String trays) {

        for(itemNode node: currentItems) {
            if(menu.isLeftClicked(node.tile))
                clipboardSwap(node);
        }

        if(!trays.equals("hud")) return;

        for(itemNode node: inventoryItems) {
            if(scrollList.isLeftClicked(node.tile))
                clipboardSwap(node);
        }

        for(itemNode node: craftingTray) {
            if(menu.isLeftClicked(node.tile))
                clipboardSwap(node);
        }

        for(itemNode node: equippedItems) {
            if(menu.isLeftClicked(node.tile))
                clipboardSwap(node);
        }
    }

    private void rightClick(String trays) {


        for(itemNode node: currentItems) {
            if(menu.isRightClicked(node.tile)) {
                System.out.println(node.tile);
            }

        }

        /*

        if(!trays.equals("hud")) return;

        for(itemNode node: inventoryItems) {
            if(scrollList.isLeftClicked(node.tile))
                clipboardSwap(node);
        }

        for(itemNode node: craftingTray) {
            if(menu.isLeftClicked(node.tile))
                clipboardSwap(node);
        }

        for(itemNode node: equippedItems) {
            if(menu.isLeftClicked(node.tile))
                clipboardSwap(node);
        }

         */



    }

    public void addItem(String type, String ps, int psi, int amount) {


        /*
        type = item type
        ps = preferred slot (either "current" or "inventory")
        psi = preferred slot index (can be -1 -1 (take first available spot) or any number 0 and above)
        amount = number of items to add (must be 1 or more)
        */
        
        //some basic checks here 
        if( !ps.equals("current") && !ps.equals("inventory") ) return;
        amount = MathUtils.clamp(amount, 1, 1000);
        

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
        if(ps.equals("current")) {


            if(psi >= 0 && psi < currentNumItems) {
                String slotType = StringUtils.getField(currentItems[psi].item, "subType");
                if(slotType.equals("")) {
                    String newName = createItem(type);
                    currentItems[psi].item = newName;
                    currentItems[psi].setItemCount(amount);
                    menu.setParent(newName, currentItems[psi].tile);
                    return;
                }
                if(slotType.equals(type)) {
                    int newCount = currentItems[psi].itemCount + amount;
                    currentItems[psi].setItemCount(newCount);
                    return;
                }
                if(!slotType.equals(type)){
                    addItem(type, ps, -1, amount);
                    return;
                }
            }
            if(psi == -1) {
                for(itemNode node: currentItems) {
                    String nodeType = StringUtils.getField(node.item, "subType");
                    if(nodeType.equals(type)) {
                        int newAmount = node.itemCount + amount;
                        node.setItemCount(newAmount);
                        return;
                    }
                }
                for(itemNode node: currentItems) {
                    if(node.item.equals("")) {
                        String newName = createItem(type);
                        node.item = newName;
                        node.setItemCount(amount);
                        menu.setParent(newName, node.tile);
                        return;
                    }
                }
                addItem(type, "inventory", psi, amount);
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


        if(ps.equals("inventory")) {
            if(psi >= 0 && psi < inventoryItems.size()) {
                String invenType = StringUtils.getField(inventoryItems.get(psi).item, "subType");
                if(invenType.equals("")) {
                    String newName = createItem(type);
                    inventoryItems.get(psi).item = newName;
                    inventoryItems.get(psi).setItemCount(amount);
                    menu.setParent(newName, inventoryItems.get(psi).tile);
                    return;
                }
                if(invenType.equals(type)) {
                    int newAmount = inventoryItems.get(psi).getItemCount() + amount;
                    inventoryItems.get(psi).setItemCount(newAmount);
                    return;
                }
                if(!invenType.equals(type)) {
                    addItem(type, ps, -1, amount);
                    return;
                }
            }
            if(psi == -1) {

                for(itemNode node: inventoryItems) {
                    String nodeType = StringUtils.getField(node.item, "subType");
                    if(nodeType.equals(type)) {
                        int newAmount = node.itemCount + amount;
                        node.setItemCount(newAmount);
                        return;
                    }
                }

                itemNode newNode = new itemNode();
                StringUtils nodeName = new StringUtils("[type: inventory][subType: inventoryHidden][index: ]");
                StringUtils.setField(nodeName, "index", StringUtils.toString(inventoryItems.size()));
                newNode.tile = nodeName.data;
                menu.registerItem(nodeName.data, "inventoryTray", background, "[vertical: top][horizontal: left]", 0, 0, 5);
                scrollList.addItem(nodeName.data);
                inventoryItems.add(newNode);

                String newItemName = createItem(type);
                newNode.item = newItemName;
                menu.setParent(newItemName, newNode.tile);
                return;
            }
        }

    }

    public void subtractItem() {



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

        leftClick(ent.drawMode);
        rightClick(ent.drawMode);

        menu.updatePosition(clipboard.tile);

        if(ent.drawMode.equals("hud")) {
            scrollList.updateList2();
        }



    }
}

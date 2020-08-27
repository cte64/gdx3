package gameCode.Menus;

import gameCode.Infastructure.Component;
import gameCode.Infastructure.Entity;
import gameCode.Utilities.MathUtils;
import gameCode.Utilities.StringUtils;
import gameCode.Utilities.myPair;

import java.util.ArrayList;

public class InventoryManager1 extends Component {



    //constants and lucky numbers =============================================
    private final int currentNumItems = 8;
    private final int inventoryWidth = 7;
    private final int itemWidth = 52;
    private final int padding = 1;
    private int maxInvenItems = 10;


    //important stuff =========================================================
    MenuManager menu;
    ScrollList scrollList;
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

    //lists and containers ====================================================
    ArrayList<itemNode> inventoryItems;
    itemNode[] currentItems;

    //ids of menu items =======================================================
    String background;
    String foreground;

    public InventoryManager1() {

        type = "logic";

        menu = new MenuManager();
        scrollList = new ScrollList(menu);
        scrollList.left = 34;
        scrollList.top = 58;
        scrollList.bottom = 500;
        scrollList.itemHeight = 52;
        scrollList.itemWidth = 52;
        scrollList.width = 7;
        scrollList.padding = 1;

        currentItems = new itemNode[currentNumItems];
        inventoryItems = new ArrayList<itemNode>();

        //set up the background and foreGround
        background = "[type: inventory][subType: background]";
        foreground = "[type: inventory][subType: foreground]";
        menu.registerItem(background, "invenBackground", null, "[vertical: center][horizontal: center]", 0, 0, 4);
        menu.registerItem(foreground, "invenForeground", background, "[vertical: center][horizontal: left]", 0, 0, 7);

        //inventory selection part ================================================
        for(int x = 0; x < currentNumItems; x++) {
            StringUtils nodeName = new StringUtils("[type: inventory][subType: inventoryCurrentTray][index: ]");
            StringUtils.setField(nodeName, "index", StringUtils.toString(x));

            int xPos = 22 + x*(itemWidth + padding);
            int yPos = 15;

            itemNode node = new itemNode();
            menu.registerItem(nodeName.data, "inventoryTray", background, "[vertical: bottom][horizontal: left]", xPos, yPos, 8);
            node.tile = nodeName.data;
            currentItems[x] = node;
        }

        //this is for the items ===================================================
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

        //this is also a test ======================================================
        addItem("[type: banana][amount: 1][preferredSlot: inventory][preferredSlotIndex: 1]");
    }

    public String createItem(String name) {

        StringUtils newName = new StringUtils("[type: inventoryItem][subType: ][id: ]");
        StringUtils.setField(newName, "subType", name);

        menu.registerItem(newName.data, name, null, "[vertical: center][horizontal: center]", 0, 0, 6);
        return newName.data;
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

        scrollList.updateList2();

    }




}

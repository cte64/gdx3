package gameCode.Menus.Inventory;
import com.mygdx.game.Engine;
import gameCode.Factory.EntityFactory;
import gameCode.Infrastructure.*;
import gameCode.Menus.MenuManager;
import gameCode.Menus.ScrollList;
import gameCode.Menus.TextComponent;
import gameCode.Utilities.myMath;
import gameCode.Utilities.myString;
import gameCode.Utilities.myPair;
import java.util.ArrayList;

public class InventoryManager extends Component {

    //constants and lucky numbers ===================================================
    private Entity parent;
    private final int currentNumItems = 7;
    private final int itemWidth = 52;
    private final int padding = 1;
    private int maxInvenItems = 56;
    private int selected;

    //important stuff ===============================================================
    MenuManager menu;
    ScrollList scrollList;
    String scrollBar;
    String textBack;
    String currentBackground;
    String background;
    String foreground;
    private boolean iToggle;

    //lists and containers =========================================================
    ArrayList<ItemNode> inventoryItems;
    ItemNode[] currentItems;
    ItemNode[] craftingTray;
    ItemNode[] equippedItems;
    ItemNode clipboard;
    ItemNode craftedItem;
    ArrayList<String> uniqueIds;
    InventoryLookup invenLookup;
    Entity selectedItem;

    public InventoryManager(Entity parent) {

        type = "logic";
        this.parent = parent;


        //set up the background and foreGround
        background = "[type: inventory][subType: background]";
        foreground = "[type: inventory][subType: foreground]";
        scrollBar = "[type: inventory][subType: scrollBar]";
        textBack = "[type: inventory][subType: textBack]";
        currentBackground = "[type: inventory][subType: currentBackground]";

        currentItems = new ItemNode[currentNumItems];
        inventoryItems = new ArrayList<ItemNode>();
        craftingTray = new ItemNode[9];
        equippedItems = new ItemNode[4];
        clipboard = new ItemNode();
        craftedItem = new ItemNode();
        uniqueIds = new ArrayList<String>();
        selectedItem = null;

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
        invenLookup = new InventoryLookup();
        selected = -1;

        //set the clipboard
        clipboard.tile = "[type: inventory][subType: clipboard]";
        menu.registerItem(clipboard.tile, null, null, "[vertical: mouse][horizontal: mouse]", 0, 0, 9);

        menu.registerItem(background, "invenBack", null, "[vertical: center][horizontal: center]", 0, 0, 4);
        menu.registerItem(foreground, "invenFront", background, "[vertical: center][horizontal: left]", 0, 0, 7);
        menu.registerItem(scrollBar, "invenScrollBar", foreground, "[vertical: top][horizontal: center]", 50, 0, 8);
        menu.registerItem(textBack, "invenTextBack", foreground, "[vertical: top][horizontal: right]", -10, 25, 8);
        menu.registerItem(currentBackground, "currentBackground", null, "[vertical: bottom][horizontal: center]", 0, 0, 6);
        scrollList.scrollBar = scrollBar;

        //Unique Ids ==============================================================
        for(int x = 0; x < 100; x++) { uniqueIds.add(myString.toString(x)); }

        //inventory selection part ================================================
        for(int x = 0; x < currentNumItems; x++) {
            myString nodeName = new myString("[type: inventory][subType: inventoryCurrentTray][index: ]");
            nodeName.setField( "index", myString.toString(x));

            int xPos = 2 + x*(itemWidth + padding);
            int yPos = 2;

            ItemNode node = new ItemNode();
            menu.registerItem(nodeName.data, "inventoryTray", currentBackground, "[vertical: bottom][horizontal: left]", xPos, yPos, 7);
            node.tile = nodeName.data;
            currentItems[x] = node;
        }

        //Inventory Items  ========================================================
        for(int x = 0; x < maxInvenItems; x++) {
            myString nodeName = new myString("[type: inventory][subType: inventoryHidden][index: ]");
            nodeName.setField( "index", myString.toString(x));

            int xPos = scrollList.left + x*(itemWidth + padding);
            int yPos = scrollList.top;

            ItemNode node = new ItemNode();
            node.tile = nodeName.data;

            menu.registerItem(nodeName.data, "inventoryTray", background, "[vertical: top][horizontal: left]", xPos, yPos, 5);
            scrollList.addItem(nodeName.data);
            inventoryItems.add(node);
        }

        //Crafting Trays ==========================================================
        for(int x = 0; x < craftingTray.length; x++) {
            myString nodeName = new myString("[type: inventory][subType: craftingTray][index: ]");
            nodeName.setField("index", myString.toString(x));

            int xPos = 389 + (x % 3)*(itemWidth + padding);
            int yPos = 25 + padding + (x / 3)*(itemWidth + padding);

            ItemNode node = new ItemNode();
            node.tile = nodeName.data;

            menu.registerItem(nodeName.data, "inventoryTray", background, "[vertical: bottom][horizontal: left]", xPos, yPos, 9);
            craftingTray[x] = node;
        }

        //Equipped Trays ==========================================================
        for(int x = 0; x < equippedItems.length; x++) {
            myString nodeName = new myString("[type: inventory][subType: equippedTrays][index: ]");
            nodeName.setField( "index", myString.toString(x));

            int xPos = 0;
            int yPos = x*(itemWidth + padding);;

            ItemNode node = new ItemNode();
            node.tile = nodeName.data;

            menu.registerItem(nodeName.data, "inventoryTray", textBack, "[vertical: top][horizontal: left]", xPos, yPos, 9);
            equippedItems[x] = node;
        }

        //Crafted Item ============================================================
        craftedItem.tile = "[type: inventory][subType: craftedItem]";
        menu.registerItem(craftedItem.tile, "inventoryTray", foreground, "[vertical: bottom][horizontal: right]", -10, 80, 9);


        addItem("lumber", "inventory", 4, 20);
        addItem("stone", "inventory", 5, 20);
        addItem("silverPickaxe", "current", 0, 1);
        addItem("goldShovel", "current", 1, 1);

        //Hide all the items by default ============================================
        menu.updateDrawMode(background, "hidden");
    }

    //Item Modifier ================================================================
    public String createItem(String name) {

        myString newName = new myString("[type: inventoryItem][subType: ][id: ]");
        newName.setField("subType", name);
        newName.setField("id", uniqueIds.get(0));
        uniqueIds.remove(0);

        //I will find a more elegant solution for this later
        myPair<Integer, Integer> comp = Engine.get().getAssets().getSpriteDimensions(name);
        int xComp = -(52 - comp.first) / 2 + 3;
        int yComp = -(52 - comp.second) / 2 + 3;

        menu.registerItem(newName.data, name, null, "[vertical: center][horizontal: center]", 0, 0, 7);
        TextComponent newComp = new TextComponent("0", 18, "[vertical: bottom][horizontal: left]", xComp, yComp);
        newComp.setColor(0.0f, 0.0f, 0.0f, 1.0f);
        menu.addText(newName.data, newComp);

        return newName.data;
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
        amount = myMath.clamp(amount, 1, 1000);


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
                String slotType = myString.getField(currentItems[psi].item, "subType");
                if(slotType.equals("")) {
                    String newName = createItem(type);
                    currentItems[psi].item = newName;
                    setItemCount(currentItems[psi], amount);
                    menu.setParent(newName, currentItems[psi].tile);
                    return;
                }
                if(slotType.equals(type)) {
                    int newCount = currentItems[psi].getItemCount() + amount;
                    setItemCount(currentItems[psi], newCount);
                    return;
                }
                if(!slotType.equals(type)){
                    addItem(type, ps, -1, amount);
                    return;
                }
            }
            if(psi == -1) {
                for(ItemNode node: currentItems) {
                    String nodeType = myString.getField(node.item, "subType");
                    if(nodeType.equals(type)) {
                        int newAmount = node.getItemCount() + amount;
                        setItemCount(node, newAmount);
                        return;
                    }
                }
                for(ItemNode node: currentItems) {
                    if(node.item.equals("")) {
                        String newName = createItem(type);
                        node.item = newName;
                        setItemCount(node, amount);
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
                String invenType = myString.getField(inventoryItems.get(psi).item, "subType");
                if(invenType.equals("")) {
                    String newName = createItem(type);
                    inventoryItems.get(psi).item = newName;
                    setItemCount(inventoryItems.get(psi), amount);
                    menu.setParent(newName, inventoryItems.get(psi).tile);
                    return;
                }
                if(invenType.equals(type)) {
                    int newAmount = inventoryItems.get(psi).getItemCount() + amount;
                    setItemCount(inventoryItems.get(psi), newAmount);
                    return;
                }
                if(!invenType.equals(type)) {
                    addItem(type, ps, -1, amount);
                    return;
                }
            }
            if(psi == -1) {
                for(ItemNode node: inventoryItems) {
                    String nodeType = myString.getField(node.item, "subType");
                    if(nodeType.equals(type)) {
                        int newAmount = node.getItemCount() + amount;
                        setItemCount(node, newAmount);
                        return;
                    }
                }

                for(ItemNode node: inventoryItems) {
                    if(node.item.equals("")) {
                        String newName = createItem(type);
                        node.item = newName;
                        setItemCount(node, amount);
                        menu.setParent(newName, node.tile);
                        return;
                    }
                }
            }
        }
    }

    public void subtractItem(ItemNode node, int amount) {

        amount = myMath.clamp(amount, 1, 1000);
        int newAmount = node.getItemCount() - amount;

        if(newAmount > 0) setItemCount(node, newAmount);
        else {
            uniqueIds.add(myString.getField(node.item, "id"));
            menu.removeItem(node.item);
            node.item = "";
        }
    }

    public void setItemCount(ItemNode node, int count) {
        node.setItemCount(count);
        Entity ent = menu.getEnt(node.item);
        if(ent == null) return;
        TextComponent tc = (TextComponent) ent.getComponent("text");
        if(tc != null) tc.setText(myString.toString(count));
    }


    //Clicking Stuff ===============================================================
    private void clipboardSwap(ItemNode node) {

        String oldClipItem = clipboard.item;
        int oldClipCount = clipboard.getItemCount();

        menu.setParent(node.item, clipboard.tile);
        Entity nodeEnt = menu.getEnt(node.item);
        if(nodeEnt != null) nodeEnt.z_pos = 12;
        clipboard.item = node.item;
        setItemCount(clipboard, node.getItemCount());

        menu.setParent(oldClipItem, node.tile);
        node.item = oldClipItem;
        setItemCount(node, oldClipCount);
    }

    private void grabCraftedItem() {

        if(craftedItem.item == "") return;
        String type = myString.getField(craftedItem.item, "subType");
        int amount = craftedItem.getItemCount();

        ArrayList<myPair<String, Integer>> toTest = new ArrayList<>();
        for(ItemNode node: craftingTray) {
            String subType = myString.getField(node.item, "subType");
            myPair item = new myPair(subType, node.getItemCount());
            toTest.add(item);
        }

        ArrayList<myPair<String, Integer>> recipe = invenLookup.getRecipe(toTest);
        for(int x = 0; x < craftingTray.length; x++) {
            int reduceAmount = recipe.get(x).second * amount;
            subtractItem(craftingTray[x], reduceAmount);
        }

        clipboardSwap(craftedItem);
    }

    private void updateCrafting() {

        //remove the old one
        menu.removeItem(craftedItem.item);
        craftedItem.item = "";

        ArrayList<myPair<String, Integer>> toTest = new ArrayList<>();
        for(ItemNode node: craftingTray) {
            String subType = myString.getField(node.item, "subType");
            myPair item = new myPair(subType, node.getItemCount());
            toTest.add(item);
        }

        myPair<String, Integer> newItem = invenLookup.getCraftedItem(toTest);
        if(!newItem.first.equals("") && newItem.second != 0) {
            String newName = createItem(newItem.first);
            craftedItem.item = newName;
            setItemCount(craftedItem, newItem.second);
            menu.setParent(newName, craftedItem.tile);
        }
    }

    private void rightClickAction(ItemNode node) {
        if(myString.compareFields(node.item, clipboard.item, "subType") ) {
            int newAmount = node.getItemCount() + 1;
            setItemCount(node, newAmount);
            subtractItem(clipboard, 1);
        }
        if(node.item.equals("")) {
            String newItem = createItem(myString.getField(clipboard.item, "subType"));
            node.item = newItem;
            menu.setParent(newItem, node.tile);
            setItemCount(node,1);
            subtractItem(clipboard, 1);
        }
    }

    private void leftClickAction(ItemNode node) {
        if(myString.compareFields(node.item, clipboard.item, "subType")) {
            int newAmount = node.getItemCount() + clipboard.getItemCount();
            setItemCount(node, newAmount);
            subtractItem(clipboard, clipboard.getItemCount());
        }
        else {
            clipboardSwap(node);
        }
    }

    private void rightClick(String trays) {

        for(ItemNode node: currentItems) {
            if(menu.isRightClicked(node.tile) && !clipboard.item.equals(""))
                rightClickAction(node);
        }

        if(!trays.equals("hud")) return;

        for(ItemNode node: inventoryItems) {
            if(menu.isRightClicked(node.tile) && !clipboard.item.equals(""))
                rightClickAction(node);
        }

        for(ItemNode node: craftingTray) {
            if(menu.isRightClicked(node.tile) && !clipboard.item.equals("")) {
                rightClickAction(node);
                updateCrafting();
            }
        }
    }

    private void leftClick(String trays) {

        for(ItemNode node: currentItems) {
            if(menu.isLeftClicked(node.tile))
                leftClickAction(node);
        }

        if(!trays.equals("hud")) return;

        for(ItemNode node: inventoryItems) {
            if(scrollList.isLeftClicked(node.tile))
                leftClickAction(node);
        }

        for(ItemNode node: craftingTray) {
            if(menu.isLeftClicked(node.tile)) {
                leftClickAction(node);
                updateCrafting();
            }
        }

        for(ItemNode node: equippedItems) {
            if(menu.isLeftClicked(node.tile))
                leftClickAction(node);
        }

        if(menu.isLeftClicked(craftedItem.tile)) grabCraftedItem();
    }

    private void SetCurrentItem(String itemID) {

        Entity ent = EntityFactory.createEntity(itemID);

        //make hero the parent of this object
        for(Component comp: ent.components) {
            if(comp instanceof AddEntity)
                ((AddEntity) comp).addEntity(parent);
        }
    }

    private void setSelected() {

        int selectedBefore = selected;
        if(Engine.get().getInput().isKeyPressed("1")) selected = 0;
        if(Engine.get().getInput().isKeyPressed("2")) selected = 1;
        if(Engine.get().getInput().isKeyPressed("3")) selected = 2;
        if(Engine.get().getInput().isKeyPressed("4")) selected = 3;
        if(Engine.get().getInput().isKeyPressed("5")) selected = 4;
        if(Engine.get().getInput().isKeyPressed("6")) selected = 5;
        if(Engine.get().getInput().isKeyPressed("7")) selected = 6;
        selected = myMath.clamp(selected, 0, currentNumItems);

        if(selectedBefore == selected) return;

        for(int x = 0; x < currentNumItems; x++) {
            Entity tileEnt = menu.getEnt( currentItems[x].tile );
            if(tileEnt == null) continue;
            if(x == selected) {
                tileEnt.scale.first = 1.0f;
                tileEnt.scale.second = 1.03f;
            }
            else {
                tileEnt.scale.first = 1.0f;
                tileEnt.scale.second = 1.0f;
            }
        }

        if( !currentItems[selected].item.equals("") )
            SetCurrentItem(currentItems[selected].item);
    }


    //Update =======================================================================
    public void update(Entity entity) {

        Entity ent = menu.getEnt(background);
        if(ent == null) return;

        String worldState = myString.getField(State.getState(), "action");
        if(!worldState.equals("paused")) {
            if(Engine.get().getInput().isKeyPressed("i") && !iToggle) iToggle = true;
            if(!Engine.get().getInput().isKeyPressed("i") && iToggle) {
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


        setSelected();
        leftClick(ent.drawMode);
        rightClick(ent.drawMode);

        menu.updatePosition(clipboard.tile);
        if(ent.drawMode.equals("hud")) {
            scrollList.updateList2();
        }
    }
}

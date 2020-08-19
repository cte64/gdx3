package gameCode.Menus;

import gameCode.Infastructure.Component;
import gameCode.Infastructure.Entity;
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
        public MenuItem menuItem;
        int itemCount;
        public itemNode() {
            itemCount = 0;
            menuItem = null;
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
        pauseState = StringUtils.getField(World.getCurrentState(), "action");

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

            node.menuItem = new MenuItem(nodeName.data, "inventoryTray", background.treeNode, "[vertical: bottom][horizontal: left]", xPos, yPos, 5, 52, 52);
            nodes.add(node);
        }}

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
    }



    public void pauseAction() {


        String newAction = StringUtils.getField(World.getCurrentState(), "action");
        /*
        if(!pauseState.equals(newAction)) {
            System.out.println(newAction + " : " + pauseState);
            pauseState = newAction;
        }


         */

        System.out.println(newAction + " : " + pauseState);

    }

    public void update(Entity entity) {

        pauseAction();



    }
}

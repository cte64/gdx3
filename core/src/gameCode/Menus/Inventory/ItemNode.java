package gameCode.Menus.Inventory;

import gameCode.Infrastructure.Entity;
import gameCode.Menus.MenuManager;
import gameCode.Menus.TextComponent;
import gameCode.Utilities.StringUtils;

public class ItemNode {
    public String tile;
    public String item;
    private int itemCount;

    public void setItemCount(int count) { itemCount = count; }

    public int getItemCount() { return itemCount; }

    public ItemNode() {
        itemCount = 0;
        tile = "";
        item = "";
    }
}

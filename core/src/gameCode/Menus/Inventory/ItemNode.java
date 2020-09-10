package gameCode.Menus.Inventory;

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

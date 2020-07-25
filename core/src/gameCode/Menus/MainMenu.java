package gameCode.Menus;

import gameCode.Infastructure.Component;
import gameCode.Infastructure.Entity;

import java.awt.*;
import java.util.HashMap;

public class MainMenu extends Component {

    //public MenuItem(String newID, String newMsg, String sprNm, int newFs, int x, int y)
    MenuItem background;
    MenuItem title;


    private void regItem(String name) {

    }


    public MainMenu() {
        type = "logic";
        background = new MenuItem("id1", "stuff is cool", "menuBack", 10, 100, 100);
    }

    public void update(Entity entity) {
        for(MenuItem m: menuItems.values()) {
            m.update();
        }
    }
}

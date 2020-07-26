package gameCode.Menus;

import gameCode.Infastructure.Component;
import gameCode.Infastructure.Entity;
import gameCode.Infastructure.TextComponent;

import java.awt.*;
import java.util.HashMap;

public class MainMenu extends Component {


    private MenuItem one;


    public MainMenu() {
        type = "logic";
        one = new MenuItem("oneid", "menuBack", 100, 100, 500, 0, new TextComponent("New Game", 10, "center", 0, 0));
    }

    public void update(Entity entity) {

    }
}

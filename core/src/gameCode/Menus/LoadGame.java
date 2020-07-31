package gameCode.Menus;

import gameCode.Infastructure.Component;
import gameCode.Infastructure.Entity;
import gameCode.Infastructure.TextComponent;

public class LoadGame extends Component {


    private MenuItem background;
    private MenuItem title;

    private MenuItem one, two, three;

    public LoadGame() {


        /*
        int padding = 5;
        background = new MenuItem("[type: menu][name: background]", "scrollBackground", null, "[vertical: center][horizontal: center]", 0, 0, 486, 548, null);
        title = new MenuItem("[type: menu][name: title]", "", background, "[vertical: top][horizontal: center]", 0, 10, 0, 0, new TextComponent("Create New Game", 15, "center", 0, 0));
        one = new MenuItem("[type: menu][name: one]", "scrollItem", background, "[vertical: top][horizontal: left]", 29, 104, 361, 73, null);
        two = new MenuItem("[type: menu][name: two]", "scrollItem", background, "[vertical: top][horizontal: left]", 29, 179, 361, 73, null);
        */
    }

    public void update(Entity entity) {

    }

}

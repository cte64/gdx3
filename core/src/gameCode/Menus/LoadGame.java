package gameCode.Menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import gameCode.Infastructure.Component;
import gameCode.Infastructure.Entity;
import gameCode.Infastructure.TextComponent;
import gameCode.Utilities.StringUtils;
import gameCode.Infastructure.InputAL;
import java.util.ArrayList;


public class LoadGame extends Component {


    private MenuItem background;
    private MenuItem overlay;
    private MenuItem title;


    ArrayList<MenuItem> gameItems = new ArrayList<MenuItem>();


    public LoadGame() {

        type = "logic";

        int padding = 5;
        int yOff = padding + 50;


        //background
        background = new MenuItem("[type: menu][name: background]", "loadGameBackground", null, "[vertical: center][horizontal: center]", 0, 0, 0, 664, 634);
        overlay = new MenuItem("[type: menu][name: overlay]", "loadGameOverlay", background.treeNode, "[vertical: center][horizontal: center]", 0, 0, 2, 664, 634);


        //load game files
        for(int x = 0; x < 1; x++) {
            String name = "[type: menu][name: ]";
            name = StringUtils.setField(name, "name", StringUtils.toString(x));
            MenuItem gameItem = new MenuItem(name, "loadGameListItem", background.treeNode, "[vertical: top][horizontal: center]", -35, 115, 1, 500, 74);
            gameItems.add(gameItem);
        }

    }

    public void update(Entity entity) {



        if(Gdx.input.isButtonPressed(Input.Keys.M)) {
            background.xOffset++;
            background.positionItem();
            System.out.println("we got here");
        }

    }

}

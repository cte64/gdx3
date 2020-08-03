package gameCode.Menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import gameCode.Infastructure.Component;
import gameCode.Infastructure.Entity;
import gameCode.Infastructure.TextComponent;
import gameCode.Infastructure.World;
import gameCode.Infastructure.State;

import gameCode.Utilities.Tree;

import java.awt.*;
import java.util.HashMap;

public class MainMenu extends Component {



    private MenuItem background;
    private MenuItem newGame;


    public MainMenu() {
        type = "logic";

        int padding = 5;
        int yOff = padding + 50;


        //background
        background = new MenuItem("[type: menu][name: background]", "background", null, "[vertical: center][horizontal: center]", 0, 0, 600, 400);

        //newGame
        newGame = new MenuItem("[type: menu][name: newGame]", "menuBack", background.treeNode, "[vertical: top][horizontal: center]", 0, padding, 400, 50);
        newGame.addText(new TextComponent("New Game", 10, "center", 0, 0));


        /*

        test = new MenuItem("[type: menu][name: test]", "tile", background, "[vertical: center][horizontal: center]", 0, 0, 20, 20);

        loadGame = new MenuItem("[type: menu][name: loadGame]", "menuBack", background, "[vertical: top][horizontal: center]", 0, padding + yOff, 400, 50, new TextComponent("Load Game", 10, "center", 0, 0));
        options = new MenuItem("[type: menu][name: options]", "menuBack", background, "[vertical: top][horizontal: center]", 0, padding + 2*yOff, 400, 50, new TextComponent("Options", 10, "center", 0, 0));

         */
    }

    public void update(Entity entity) {

        //update everything ==============================================
        //if(newGame.isLeftClicked()) { State.deleteMenuItems(); World.setCurrentState("newGame"); }

        //MenuItem bc = (MenuItem)background.value;
        //bc.setXOffset( bc.getXOffset() + 1);


    }
}

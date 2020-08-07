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
    private MenuItem createGame;
    private MenuItem loadGame;
    private MenuItem options;

    public MainMenu() {

        type = "logic";

        int height = 43;
        int start = 6;

        background = new MenuItem("[type: menu][name: background]", "mainMenuBack", null, "[vertical: center][horizontal: center]", 0, 0, 0, 360, 180);
        background.addText(new TextComponent("Main Menu", 10, "[vertical: top][horizontal: center]", 0, 0));

        createGame = new MenuItem("[type: menu][name: createGame]", "menuItem", background.treeNode, "[vertical: top][horizontal: center]", 0, start + 1*height, 1, 350, 40);
        createGame.addText(new TextComponent("Create New World", 10, "[vertical: center][horizontal: center]", 0, 0));

        loadGame = new MenuItem("[type: menu][name: loadGame]", "menuItem", background.treeNode, "[vertical: top][horizontal: center]", 0, start + 2*height, 1, 350, 40);
        loadGame.addText(new TextComponent("Load World", 10, "[vertical: center][horizontal: center]", 0, 0));

        options = new MenuItem("[type: menu][name: options]", "menuItem", background.treeNode, "[vertical: top][horizontal: center]", 0, start + 3*height, 1, 350, 40);
        options.addText(new TextComponent("Options", 10, "[vertical: center][horizontal: center]", 0, 0));
    }

    public void update(Entity entity) {

        if(createGame.isLeftClicked()) World.setCurrentState("newGame");



    }
}

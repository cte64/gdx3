package gameCode.Menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import gameCode.Infastructure.Component;
import gameCode.Infastructure.Entity;
import gameCode.Infastructure.TextComponent;
import gameCode.Infastructure.World;

import java.awt.*;
import java.util.HashMap;

public class MainMenu extends Component {


    private MenuItem background;
    private MenuItem newGame;
    private MenuItem loadGame;
    private MenuItem options;


    public MainMenu() {
        type = "logic";


        int padding = 5;
        int yOff = padding + 50;

        background = new MenuItem("[type: menuItem][name: background]", "background", null, "[vertical: center][horizontal: center]", 0, 0, 600, 400, null);
        newGame = new MenuItem("[type: menuItem][name: newGame]", "menuBack", background, "[vertical: top][horizontal: center]", 0, padding, 400, 50, new TextComponent("New Game", 10, "center", 0, 0));
        loadGame = new MenuItem("[type: menuItem][name: loadGame]", "menuBack", background, "[vertical: top][horizontal: center]", 0, padding + yOff, 400, 50, new TextComponent("Load Game", 10, "center", 0, 0));
        options = new MenuItem("[type: menuItem][name: options]", "menuBack", background, "[vertical: top][horizontal: center]", 0, padding + 2*yOff, 400, 50, new TextComponent("Options", 10, "center", 0, 0));
    }

    public void update(Entity entity) {

        //update everything ==============================================
        if(newGame.isLeftClicked()) {System.out.println("new game"); }



    }
}

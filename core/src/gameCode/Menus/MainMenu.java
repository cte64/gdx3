package gameCode.Menus;

import gameCode.Infrastructure.Component;
import gameCode.Infrastructure.Entity;
import gameCode.Infrastructure.TextComponent;
import gameCode.Infrastructure.State;

public class MainMenu extends Component {



    private String background;
    private String createGame;
    private String loadGame;
    private String options;

    MenuManager menu;


    public MainMenu() {

        type = "logic";


        background = "[type: menu][name: background]";
        createGame = "[type: menu][name: createGame]";
        loadGame = "[type: menu][name: loadGame]";
        options = "[type: menu][name: options]";


        int height = 43;
        int start = 6;

        menu = new MenuManager();

        menu.registerItem(background, "mainMenuBack", null, "[vertical: center][horizontal: center]", 0, 0, 0);
        menu.addText(background, new TextComponent("Main Menu", 10, "[vertical: top][horizontal: center]", 0, 0));

        menu.registerItem(createGame, "menuItem", background, "[vertical: top][horizontal: center]", 0, start + 1*height, 1);
        menu.addText(createGame, new TextComponent("Create New World", 10, "[vertical: center][horizontal: center]", 0, 0));

        menu.registerItem(loadGame, "menuItem", background, "[vertical: top][horizontal: center]", 0, start + 2*height, 1);
        menu.addText(loadGame, new TextComponent("Load World", 10, "[vertical: center][horizontal: center]", 0, 0));

        menu.registerItem(options, "menuItem", background, "[vertical: top][horizontal: center]", 0, start + 3*height, 1);
        menu.addText(options, new TextComponent("Options", 10, "[vertical: center][horizontal: center]", 0, 0));
    }

    public void update(Entity entity) {
        if(menu.isLeftClicked(createGame)) State.newGame();
        if(menu.isLeftClicked(loadGame)) State.loadGame();
    }
}

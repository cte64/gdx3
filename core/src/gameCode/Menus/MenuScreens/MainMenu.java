package gameCode.Menus.MenuScreens;

import gameCode.Infrastructure.Component;
import gameCode.Infrastructure.Entity;
import gameCode.Infrastructure.State;
import gameCode.Menus.MenuManager;
import gameCode.Menus.TextComponent;

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
        menu.addText(background, new TextComponent("Main Menu", 26, "[vertical: top][horizontal: center]", 0, 0));

        menu.registerItem(createGame, "menuItem", background, "[vertical: top][horizontal: center]", 0, start + 1*height, 1);
        menu.addText(createGame, new TextComponent("Create New World", 18, "[vertical: center][horizontal: center]", 0, 0));

        menu.registerItem(loadGame, "menuItem", background, "[vertical: top][horizontal: center]", 0, start + 2*height, 1);
        menu.addText(loadGame, new TextComponent("Load World", 18, "[vertical: center][horizontal: center]", 0, 0));

        menu.registerItem(options, "menuItem", background, "[vertical: top][horizontal: center]", 0, start + 3*height, 1);
        menu.addText(options, new TextComponent("Options", 18, "[vertical: center][horizontal: center]", 0, 0));
    }

    public void update(Entity entity) {

        //Update Click State =====================================================
        if(menu.isLeftClicked(createGame)) State.newGame();
        if(menu.isLeftClicked(loadGame)) State.loadGame();

        //Update Hover State =====================================================
        menu.hoverAction(createGame, "[hoverType: toggle][fontSize: 1][scale: 1.009]");
        menu.hoverAction(loadGame, "[hoverType: toggle][fontSize: 1][scale: 1.009]");
        menu.hoverAction(options, "[hoverType: toggle][fontSize: 1][scale: 1.009]");
    }
}

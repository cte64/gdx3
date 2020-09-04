package gameCode.Menus;

import gameCode.Infrastructure.Component;
import gameCode.Infrastructure.Entity;

public class DeleteGameMenu extends Component {

    public String directory;

    public LoadGame loadGame;
    public String background;
    public String input;
    public String yes;
    public String back;
    MenuManager menu;
    TextInput textInput;


    public DeleteGameMenu(String directory, LoadGame loadGame) {

        type = "logic";
        menu = new MenuManager();
        this.loadGame = loadGame;
        this.directory = directory;

        background = "[type: menu][subType: deleteGame][id: background]";
        input = "[type: menu][subType: deleteGame][id: input]";
        back = "[type: menu][subType: deleteGame][id: no]";
        yes = "[type: menu][subType: deleteGame][id: yes]";

        menu.registerItem(background, "createGameBack", this.loadGame.background, "[vertical: center][horizontal: center]", 0, 0, 5);
        menu.addText(background, new TextComponent("Type \"" + this.directory + "\" to delete", 20, "[vertical: top][horizontal: center]", 0, -5));

        menu.registerItem(back, "halfMenuItem", background, "[vertical: bottom][horizontal: left]", 5, 10, 6);
        menu.addText(back, new TextComponent("Go Back", 18, "[vertical: center][horizontal: center]", 0, 0));

        menu.registerItem(yes, "halfMenuItem", background, "[vertical: bottom][horizontal: right]", -5, 10, 6);
        menu.addText(yes, new TextComponent("Delete", 18, "[vertical: center][horizontal: center]", 0, 0));
        menu.getEnt(yes).drawMode = "hidden";

        menu.registerItem(input, "menuItem", background, "[vertical: top][horizontal: center]", 0, 46, 6);
        textInput = new TextInput(menu, input, "Name: ", 18);
    }

    public void update(Entity entity) {

        textInput.update();

        //delete
        String text = textInput.text;
        if(text.equals(directory) && menu.getEnt(yes).drawMode == "hidden")
            menu.getEnt(yes).drawMode = "hud";

        else if (!text.equals(directory) && menu.getEnt(yes).drawMode != "hidden")
            menu.getEnt(yes).drawMode = "hidden";

        if(menu.isLeftClicked(back)) { loadGame.toggleDeleteOff(); }
        if(menu.getEnt(yes).drawMode != "hidden" && menu.isLeftClicked(yes)) loadGame.deleteWorld(directory);
    }
}

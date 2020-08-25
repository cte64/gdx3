package gameCode.Menus;

import gameCode.Infastructure.Component;
import gameCode.Infastructure.Entity;
import gameCode.Infastructure.TextComponent;

public class DeleteGameMenu extends Component {

    public String directory;


    /*
    public LoadGame1 loadGame;
    public MenuItem background;
    public MenuItem input;
    public MenuItem yes;
    public MenuItem back;
    public TextInput input_text;
     */


    public LoadGame loadGame;
    public String background;
    public String input;
    public String yes;
    public String back;
    public TextInput input_text;
    MenuManager menu;


    public DeleteGameMenu(String directory, LoadGame loadGame) {

        type = "logic";
        menu = new MenuManager();

        //Names =====================================================================================
        background = "[type: menu][subType: deleteGame][id: background]";
        input = "[type: menu][subType: deleteGame][id: input]";
        back = "[type: menu][subType: deleteGame][id: no]";
        yes = "[type: menu][subType: deleteGame][id: yes]";

        this.loadGame = loadGame;
        this.directory = directory;
        menu.registerItem(background, "createGameBack", this.loadGame.background, "[vertical: center][horizontal: center]", 0, 0, 5);
        menu.addText(background, new TextComponent("Type \"" + this.directory + "\" to delete", 10, "[vertical: top][horizontal: center]", 0, -10));

        menu.registerItem(input, "menuItem", background, "[vertical: top][horizontal: center]", 0, 50, 6);
        TextComponent textDisplay = new TextComponent("", 10, "[vertical: center][horizontal: left]", 60, 0);
        TextInput textInput = new TextInput(textDisplay, 30);
        menu.addText(input, new TextComponent("Name: ", 10, "[vertical: center][horizontal: left]", 10, 0));
        menu.addText(input, textDisplay);
        menu.getEnt(input).addComponent(textInput);
        input_text = textInput;

        menu.registerItem(back, "halfMenuItem", background, "[vertical: bottom][horizontal: left]", 5, 10, 6);
        menu.addText(back, new TextComponent("Go Back", 10, "[vertical: center][horizontal: center]", 0, 0));

        menu.registerItem(yes, "halfMenuItem", background, "[vertical: bottom][horizontal: right]", -5, 10, 6);
        menu.addText(yes, new TextComponent("Delete", 10, "[vertical: center][horizontal: center]", 0, 0));
        menu.getEnt(yes).drawMode = "hidden";

    }


    public void update(Entity entity) {



        //delete
        String text = input_text.getText();
        if(text.equals(directory) && menu.getEnt(yes).drawMode == "hidden")
            menu.getEnt(yes).drawMode = "hud";

        else if (!text.equals(directory) && menu.getEnt(yes).drawMode != "hidden")
            menu.getEnt(yes).drawMode = "hidden";


        if(menu.isLeftClicked(back)) { loadGame.toggleDeleteOff(); }
        if(menu.getEnt(yes).drawMode != "hidden" && menu.isLeftClicked(yes)) loadGame.deleteWorld(directory);
    }



}

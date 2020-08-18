package gameCode.Menus;

import gameCode.Infastructure.Component;
import gameCode.Infastructure.Entity;
import gameCode.Infastructure.TextComponent;
import gameCode.Infastructure.World;

public class DeleteGameMenu extends Component {

    public String directory;


    public LoadGame loadGame;
    public MenuItem background;
    public MenuItem input;
    public MenuItem yes;
    public MenuItem back;
    public TextInput input_text;





    public DeleteGameMenu(String directory, LoadGame loadGame) {


        type = "logic";

        this.loadGame = loadGame;
        this.directory = directory;
        background = new MenuItem("[type: menu][subType: deleteGame][id: background]", "createGameBack", this.loadGame.background.treeNode, "[vertical: center][horizontal: center]", 0, 0, 5, 360, 140);
        background.addText(new TextComponent("Type \"" + this.directory + "\" to delete", 10, "[vertical: top][horizontal: center]", 0, -10));

        input = new MenuItem("[type: menu][subType: deleteGame][id: input]", "menuItem", background.treeNode, "[vertical: top][horizontal: center]", 0, 50, 6, 350, 40);
        TextComponent textDisplay = new TextComponent("", 10, "[vertical: center][horizontal: left]", 60, 0);
        TextInput textInput = new TextInput(textDisplay, 30);
        input.addText(new TextComponent("Name: ", 10, "[vertical: center][horizontal: left]", 10, 0));
        input.addText(textDisplay);
        input.ent.addComponent(textInput);
        input_text = textInput;


        back = new MenuItem("[type: menu][subType: deleteGame][id: no]", "halfMenuItem", background.treeNode, "[vertical: bottom][horizontal: left]", 5, 10, 6, 173, 40);
        back.addText(new TextComponent("Go Back", 10, "[vertical: center][horizontal: center]", 0, 0));

        yes = null;
    }


    public void update(Entity entity) {

        //delete
        String text = input_text.getText();
        if(text.equals(directory) && yes == null) {
            yes = new MenuItem("[type: menu][subType: areYouSure][id: yes]", "halfMenuItem", background.treeNode, "[vertical: bottom][horizontal: right]", -5, 10, 6, 173, 40);
            yes.addText(new TextComponent("Delete", 10, "[vertical: center][horizontal: center]", 0, 0));
        }

        else if (!text.equals(directory) && yes != null) {
            World.entitiesToBeDeleted.add(yes.ent);
            yes = null;
        }

        if(back.isLeftClicked()) { loadGame.toggleDeleteOff(); }


        
        //bottom buttons
        if(yes != null && yes.isLeftClicked()) {
            for(LoadGame.listItem item: loadGame.listItems) {
                if(item.name.equals(directory)) {
                    //FileSystem.deleteDirectory(item.directory);
                    item.delete();
                    loadGame.listItems.remove(item);
                    toggleDeleteCheck("", false);
                    positionItems();
                    break;
                }
            }
        }

        /*
         */


    }


}

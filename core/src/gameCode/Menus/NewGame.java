package gameCode.Menus;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import gameCode.Infastructure.*;
import gameCode.Terrain.MakeWorld;
import gameCode.Utilities.MathUtils;
import gameCode.Utilities.StringUtils;

public class NewGame extends Component {

    MenuManager menu;

    String background;
    String newName;
    String sizeSelect;
    String minus;
    String plus;
    String back;
    String createWorld;

    TextComponent sizeTextComp;
    TextInput textInput;

    int numChunks;
    int min, max;

    public NewGame() {


        type = "logic";

        //Ids ===================================================
        background = "[type: menu][name: background]";
        newName = "[type: menu][name: newName]";
        sizeSelect = "[type: menu][name: sizeSelect]";
        minus = "[type: menu][name: minus]";
        plus = "[type: menu][name: plus]";
        back = "[type: menu][name: back]";
        createWorld = "[type: menu][name: createWorld]";


        int height = 43;
        int start = 6;
        numChunks = 10;
        min = 4;
        max = 30;

        menu = new MenuManager();

        menu.registerItem(background, "mainMenuBack", null, "[vertical: center][horizontal: center]", 0, 0, 0);
        menu.addText(background, new TextComponent("Create New World", 10, "[vertical: top][horizontal: center]", 0, 0));

        menu.registerItem(newName, "menuItem", background, "[vertical: top][horizontal: center]", 0, start + 1*height, 1);
        TextComponent textDisplay = new TextComponent("", 10, "[vertical: center][horizontal: left]", 60, 0);
        textInput = new TextInput(textDisplay, 30);

        menu.addText(newName, new TextComponent("Name: ", 10, "[vertical: center][horizontal: left]", 10, 0));
        menu.addText(newName, textDisplay);
        menu.addText(newName, textInput);

        menu.registerItem(sizeSelect, "menuItem", background, "[vertical: top][horizontal: center]", 0, start + 2*height, 1);
        sizeTextComp = new TextComponent(StringUtils.toString(numChunks), 10, "[vertical: center][horizontal: center]", 0, 0);
        menu.addText(sizeSelect, new TextComponent("Size: ", 10, "[vertical: center][horizontal: left]", 10, 0));
        menu.addText(sizeSelect, sizeTextComp);
        menu.registerItem(minus, "minus", sizeSelect, "[vertical: center][horizontal: center]", -40, 0, 2);
        menu.registerItem(plus, "plus", sizeSelect, "[vertical: center][horizontal: center]", 40, 0, 2);

        menu.registerItem(back, "halfMenuItem", background, "[vertical: top][horizontal: left]", 5, start + 3*height, 1);
        menu.addText(back, new TextComponent("Back", 10, "[vertical: center][horizontal: center]", 0, 0));

        menu.registerItem(createWorld, "halfMenuItem", background, "[vertical: top][horizontal: right]", -5, start + 3*height, 1);
        menu.addText(createWorld, new TextComponent("Create", 10, "[vertical: center][horizontal: center]", 0, 0));
    }

    public void update(Entity entity) {

        // + and - buttons =======================================
        if(menu.isLeftClicked(minus)) {
            numChunks--;
            numChunks = MathUtils.clamp(numChunks, min, max);
            sizeTextComp.text = StringUtils.toString(numChunks);
        }

        if(menu.isLeftClicked(plus)) {
            numChunks++;
            numChunks = MathUtils.clamp(numChunks, min, max);
            sizeTextComp.text = StringUtils.toString(numChunks);
        }


        //back ====================================================
        if(menu.isLeftClicked(back)) State.mainMenu();

        //createWorld =============================================
        if(menu.isLeftClicked(createWorld)) {
            StringUtils newState = new StringUtils("[action: createNewWorld][name: ][numChunks: ]");
            StringUtils.setField(newState, "name", textInput.getText());
            StringUtils.setField(newState, "numChunks", StringUtils.toString(numChunks));
            State.creatingGame(newState.data);
        }
    }

}

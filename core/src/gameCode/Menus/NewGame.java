package gameCode.Menus;


import gameCode.Infrastructure.*;
import gameCode.Utilities.StringUtils;
import gameCode.Utilities.MathUtils;

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
    TextInput textInput1;

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
        menu.addText(background, new TextComponent("Create New World", 26, "[vertical: top][horizontal: center]", 0, 0));

        menu.registerItem(newName, "menuItem", background, "[vertical: top][horizontal: center]", 0, start + 1*height, 1);
        textInput1 = new TextInput(menu, newName, "Name: ", 18);


        menu.registerItem(sizeSelect, "menuItem", background, "[vertical: top][horizontal: center]", 0, start + 2*height, 1);
        sizeTextComp = new TextComponent(StringUtils.toString(numChunks), 18, "[vertical: center][horizontal: center]", 0, 0);
        menu.addText(sizeSelect, new TextComponent("Size: ", 18, "[vertical: center][horizontal: left]", 10, 0));
        menu.addText(sizeSelect, sizeTextComp);
        menu.registerItem(minus, "minus", sizeSelect, "[vertical: center][horizontal: center]", -40, 0, 2);
        menu.registerItem(plus, "plus", sizeSelect, "[vertical: center][horizontal: center]", 40, 0, 2);

        menu.registerItem(back, "halfMenuItem", background, "[vertical: top][horizontal: left]", 5, start + 3*height, 1);
        menu.addText(back, new TextComponent("Back", 18, "[vertical: center][horizontal: center]", 0, 0));

        menu.registerItem(createWorld, "halfMenuItem", background, "[vertical: top][horizontal: right]", -5, start + 3*height, 1);
        menu.addText(createWorld, new TextComponent("Create", 18, "[vertical: center][horizontal: center]", 0, 0));
    }

    public void update(Entity entity) {

        textInput1.update();

        // + and - buttons =======================================
        menu.hoverAction(minus, "[hoverType: sine][amplitude: 0.05][frequency: 1]");
        if(menu.isLeftClicked(minus)) {
            numChunks--;
            numChunks = MathUtils.clamp(numChunks, min, max);
            sizeTextComp.text = StringUtils.toString(numChunks);
        }

        menu.hoverAction(plus, "[hoverType: sine][amplitude: 0.05][frequency: 1]");
        if(menu.isLeftClicked(plus)) {
            numChunks++;
            numChunks = MathUtils.clamp(numChunks, min, max);
            sizeTextComp.text = StringUtils.toString(numChunks);
        }

        //back ====================================================
        menu.hoverAction(back, "[hoverType: toggle][fontSize: 1][scale: 1.006]");
        if(menu.isLeftClicked(back)) State.mainMenu();

        //createWorld =============================================
        menu.hoverAction(createWorld, "[hoverType: toggle][fontSize: 1][scale: 1.006]");
        if(menu.isLeftClicked(createWorld)) {
            StringUtils newState = new StringUtils("[action: createNewWorld][name: ][numChunks: ]");
            StringUtils.setField(newState, "name", textInput1.text);
            StringUtils.setField(newState, "numChunks", StringUtils.toString(numChunks));
            State.creatingGame(newState.data);
        }
    }

}

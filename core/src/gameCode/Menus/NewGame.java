package gameCode.Menus;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import gameCode.Infastructure.*;
import gameCode.Terrain.MakeWorld;
import gameCode.Utilities.MathUtils;
import gameCode.Utilities.StringUtils;

public class NewGame extends Component {

    private MenuItem background;
    private MenuItem newName;
    private MenuItem sizeSelect;
    private MenuItem minus;
    private MenuItem plus;
    private MenuItem back;
    private MenuItem createWorld;
    private TextComponent sizeText;
    private TextInput textInput;
    private int numChunks;
    private int min, max;


    public NewGame() {


        type = "logic";

        int height = 43;
        int start = 6;
        numChunks = 10;
        min = 4;
        max = 30;

        background = new MenuItem("[type: menu][name: background]", "mainMenuBack", null, "[vertical: center][horizontal: center]", 0, 0, 0, 360, 180);
        background.addText(new TextComponent("Create New World", 10, "[vertical: top][horizontal: center]", 0, 0));

        newName = new MenuItem("[type: menu][name: newName]", "menuItem", background.treeNode, "[vertical: top][horizontal: center]", 0, start + 1*height, 1, 350, 40);
        TextComponent textDisplay = new TextComponent("", 10, "[vertical: center][horizontal: left]", 60, 0);
        textInput = new TextInput(textDisplay, 30);

        newName.addText(new TextComponent("Name: ", 10, "[vertical: center][horizontal: left]", 10, 0));
        newName.addText(textDisplay);
        newName.ent.addComponent(textInput);

        sizeSelect = new MenuItem("[type: menu][name: sizeSelect]", "menuItem", background.treeNode, "[vertical: top][horizontal: center]", 0, start + 2*height, 1, 350, 40);
        sizeText = new TextComponent(StringUtils.toString(numChunks), 10, "[vertical: center][horizontal: center]", 0, 0);
        sizeSelect.addText(new TextComponent("Size: ", 10, "[vertical: center][horizontal: left]", 10, 0));
        sizeSelect.addText(sizeText);
        minus = new MenuItem("[type: menu][name: minus]", "minus", sizeSelect.treeNode, "[vertical: center][horizontal: center]", -40, 0, 2, 32, 32);
        plus = new MenuItem("[type: menu][name: plus]", "plus", sizeSelect.treeNode, "[vertical: center][horizontal: center]", 40, 0, 2, 32, 32);

        back = new MenuItem("[type: menu][name: back]", "halfMenuItem", background.treeNode, "[vertical: top][horizontal: left]", 5, start + 3*height, 1, 173, 40);
        back.addText(new TextComponent("Back", 10, "[vertical: center][horizontal: center]", 0, 0));

        createWorld = new MenuItem("[type: menu][name: createWorld]", "halfMenuItem", background.treeNode, "[vertical: top][horizontal: right]", -5, start + 3*height, 1, 173, 40);
        createWorld.addText(new TextComponent("Create", 10, "[vertical: center][horizontal: center]", 0, 0));
    }

    public void update(Entity entity) {

        // + and - buttons =======================================
        if(minus.isLeftClicked()) {
            numChunks--;
            numChunks = MathUtils.clamp(numChunks, min, max);
            sizeText.text = StringUtils.toString(numChunks);
        }

        if(plus.isLeftClicked()) {
            numChunks++;
            numChunks = MathUtils.clamp(numChunks, min, max);
            sizeText.text = StringUtils.toString(numChunks);
        }

        //back ====================================================
        if(back.isLeftClicked()) World.setCurrentState("[action: mainMenu]");

        //createWorld =============================================
        if(createWorld.isLeftClicked()) {
            StringUtils newState = new StringUtils("[action: createNewWorld][name: ][numChunks: ]");
            StringUtils.setField(newState, "name", textInput.getText());
            StringUtils.setField(newState, "numChunks", StringUtils.toString(numChunks));
            World.setCurrentState(newState.data);
        }
    }

}

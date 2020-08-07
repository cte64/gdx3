package gameCode.Menus;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import gameCode.Infastructure.Component;
import gameCode.Infastructure.Entity;
import gameCode.Infastructure.InputAL;
import gameCode.Infastructure.TextComponent;
import gameCode.Utilities.MathUtils;
import gameCode.Utilities.StringUtils;

public class NewGame extends Component {

    private MenuItem background;
    private MenuItem newName;
    private MenuItem sizeSelect;
    private MenuItem minus;
    private MenuItem plus;
    private TextComponent sizeText;
    private int numChunks;
    private int min, max;


    public NewGame() {


        type = "logic";

        int height = 43;
        int start = 8;
        numChunks = 10;
        min = 10;
        max = 30;

        background = new MenuItem("[type: menu][name: background]", "createGameBack", null, "[vertical: center][horizontal: center]", 0, 0, 0, 360, 140);
        background.addText(new TextComponent("Create New World", 10, "[vertical: top][horizontal: center]", 0, 0));

        newName = new MenuItem("[type: menu][name: newName]", "menuItem", background.treeNode, "[vertical: top][horizontal: center]", 0, start + 1*height, 1, 350, 40);
        TextComponent textDisplay = new TextComponent("", 10, "[vertical: center][horizontal: left]", 60, 0);
        TextInput textInput = new TextInput(textDisplay, 30);
        newName.addText(new TextComponent("Name: ", 10, "[vertical: center][horizontal: left]", 10, 0));
        newName.addText(textDisplay);
        newName.ent.addComponent(textInput);

        sizeSelect = new MenuItem("[type: menu][name: sizeSelect]", "menuItem", background.treeNode, "[vertical: top][horizontal: center]", 0, start + 2*height, 1, 350, 40);
        sizeText = new TextComponent(StringUtils.toString(numChunks), 10, "[vertical: center][horizontal: center]", 0, 0);
        sizeSelect.addText(new TextComponent("Size: ", 10, "[vertical: center][horizontal: left]", 10, 0));
        sizeSelect.addText(sizeText);
        minus = new MenuItem("[type: menu][name: minus]", "minus", sizeSelect.treeNode, "[vertical: center][horizontal: center]", -50, 0, 2, 32, 32);
        plus = new MenuItem("[type: menu][name: plus]", "plus", sizeSelect.treeNode, "[vertical: center][horizontal: center]", 50, 0, 2, 32, 32);
    }

    public void update(Entity entity) {


        //buttons
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


    }




}

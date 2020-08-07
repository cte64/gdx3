package gameCode.Menus;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import gameCode.Infastructure.Component;
import gameCode.Infastructure.Entity;
import gameCode.Infastructure.InputAL;
import gameCode.Infastructure.TextComponent;

public class NewGame extends Component {

    private MenuItem background;



    public NewGame() {

        type = "logic";
        int padding = 5;

        background = new MenuItem("[type: menu][name: background]", "areYouSureBackground", null, "[vertical: center][horizontal: center]", 0, 0, 0, 300, 286);
        TextComponent title = new TextComponent("", 10, "[vertical: center][horizontal: left]", 0, 0);
        TextInput newInput = new TextInput(title, 15);

        background.addText(title);
        background.ent.addComponent(newInput);
    }

    public void update(Entity entity) {



    }




}

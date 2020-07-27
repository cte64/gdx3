package gameCode.Infastructure;
import gameCode.Infastructure.Component;

public class TextComponent extends Component {


    public String message;
    private int fontSize;

    public TextComponent() {
        type = "text";
    }

    public void update(Entity ent) {

    }
}

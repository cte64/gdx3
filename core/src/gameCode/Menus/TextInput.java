package gameCode.Menus;

import gameCode.Infrastructure.*;
import gameCode.Utilities.Timer;

public class TextInput extends Component {

    private String text;
    private TextComponent textComp;
    int charLimit;
    boolean toggle;
    Timer timer;
    float blinkTime;
    boolean blinkState;

    public TextInput(TextComponent newTC, int charLimit) {
        type = "logic";
        text = "";
        textComp = newTC;
        this.charLimit = charLimit;
        toggle = true;
        timer = new Timer();
        timer.addTimer("blinker");
        blinkTime = 0.5f;
        blinkState = false;
    }

    public void update(Entity entity) {
        if(!toggle) return;
        int sizeBefore = text.length();
        for(char c: InputAL.charsQueue) {
            if(text.length() > 0 && (int)c == 8) text = text.substring(0, text.length() - 1);
            else if(sizeBefore < charLimit) text += c;
        }
        if(text.length() != sizeBefore) {
            textComp.text = text;
        }

        //blinker
        timer.update(World.getDeltaTime());
        if(timer.getTime("blinker") > blinkTime) {
            timer.resetTimer("blinker");
            blinkState = !blinkState;
            if(blinkState) textComp.text = text + "|";
            else textComp.text = text + " ";
        }
    }

    public void toggle(boolean onOff) { toggle = onOff; }

    public String getText() { return text; }
}

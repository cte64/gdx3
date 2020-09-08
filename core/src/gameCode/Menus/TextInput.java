package gameCode.Menus;

import com.mygdx.game.Engine;
import com.mygdx.game.InputAL;
import gameCode.Infrastructure.World;
import gameCode.Utilities.Timer;

public class TextInput {

    MenuManager menu;
    public String background;
    public TextComponent input;
    public TextComponent nameText;

    Timer timer;
    float blinkTime;
    boolean blinkState;

    public String text;
    int charLimit;
    public int fontSize;
    String acceptableChars;

    public TextInput(MenuManager menu, String background, String name, int fontSize) {

        this.menu = menu;
        this.background = background;

        nameText = new TextComponent(name, fontSize, "[vertical: center][horizontal: left]", 10, 0);
        menu.addText(background, nameText);

        int leftOffset = (int) Engine.get().getGraphics().getTextBounds(name, fontSize).x;
        input = new TextComponent("", fontSize, "[vertical: center][horizontal: left]", leftOffset + 10, 0);
        menu.addText(background, input);

        timer = new Timer();
        timer.addTimer("blinker");
        blinkTime = 0.5f;
        blinkState = false;
        charLimit = 30;
        acceptableChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890.() ";
        text = "";
    }

    private boolean isAcceptable(char c) {
        for(int x = 0; x < acceptableChars.length(); x++) {
            if(c == acceptableChars.charAt(x)) {
                return true;
            }
        }
        return false;
    }

    public void update() {

        int sizeBefore = text.length();
        for(char c: Engine.get().getInput().charsQueue) {
            if(text.length() > 0 && (int)c == 8) text = text.substring(0, text.length() - 1);
            else if(isAcceptable(c) && sizeBefore < charLimit) text += c;
        }
        if(text.length() != sizeBefore) {
            input.text = text;
        }

        //blinker
        timer.update(World.get().getDeltaTime());
        if(timer.getTime("blinker") > blinkTime) {
            timer.resetTimer("blinker");
            blinkState = !blinkState;
            if(blinkState) input.text = text + "|";
            else input.text = text + " ";
        }
    }
}

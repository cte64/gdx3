package gameCode.Infastructure;
import gameCode.Infastructure.Component;
import gameCode.Infastructure.Graphics;

import java.awt.*;

public class TextComponent extends Component {


    public String text;
    private int fontSize;
    private int xPos, yPos;
    private String justify;

    public TextComponent(String newText, int newFontSize, String newJustify, int newX, int newY) {
        type = "text";
        text = newText;
        fontSize = newFontSize;
        justify = newJustify;
        xPos = newX;
        yPos = newY;
    }

    public void update(Entity ent) {
        //this is just temporary
        if(justify == "center") {
            xPos = (int)(ent.x_pos + ent.getWidth()/2 - Graphics.getTextBounds(text, fontSize).x/2  );
            yPos = (int)(ent.y_pos + ent.getHeight()/2 + Graphics.getTextBounds(text, fontSize).y/2  );
        }
    }

    //getters ====================================================================
    public String getText() { return text; }
    public int getFontSize() { return fontSize; }
    public int getXPos() { return xPos; }
    public int getYPos() { return yPos; }

    //setters ====================================================================
    public void setText(String newText) { text = newText; }
    public void setFontSize(int newFontSize) { fontSize = newFontSize; }
    public void setXPos(int newX) { xPos = newX; }
    public void setYPos(int newY) { yPos = newY; }
}

package gameCode.Infastructure;
import gameCode.Infastructure.Component;
import gameCode.Infastructure.Graphics;
import gameCode.Utilities.StringUtils;

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

        String vertical = StringUtils.getField(justify, "vertical");
        String horizontal = StringUtils.getField(justify, "horizontal");

        //vertical
        if(vertical.equals("center")) yPos = (int)(ent.y_pos + ent.getHeight()/2 + Graphics.getTextBounds(text, fontSize).y/2  );
        if(vertical.equals("bottom")) yPos = (int)(ent.y_pos + Graphics.getTextBounds(text, fontSize).y);
        if(vertical.equals("top")) yPos = (int)(ent.y_pos + ent.getHeight() - Graphics.getTextBounds(text, fontSize).y);

        //horizontal
        if(horizontal.equals("center")) xPos = (int)(ent.x_pos + ent.getWidth()/2 - Graphics.getTextBounds(text, fontSize).x/2  );
        if(horizontal.equals("left")) xPos = (int)(ent.x_pos);
        if(horizontal.equals("right")) xPos = (int)(ent.x_pos + ent.getWidth() - Graphics.getTextBounds(text, fontSize).x);
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

package gameCode.Menus;
import com.mygdx.game.Engine;
import gameCode.Infrastructure.Component;
import gameCode.Infrastructure.Entity;
import com.mygdx.game.Graphics;
import gameCode.Utilities.StringUtils;

public class TextComponent extends Component {


    public String text;
    public int currentFontSize, oldFontSize;
    public int yPos, xPos, xOffset, yOffset;
    public String justify;
    public boolean show;
    float r, g, b, a;

    public TextComponent(String newText, int newFontSize, String newJustify, int newX, int newY) {
        type = "text";
        text = newText;
        currentFontSize = newFontSize;
        oldFontSize = currentFontSize;
        justify = newJustify;
        xOffset = newX;
        yOffset = newY;
        show = true;
        r = 1.0f;
        g = 1.0f;
        b = 0.0f;
        a = 1.0f;
    }

    public void update(Entity ent) {

        String vertical = StringUtils.getField(justify, "vertical");
        String horizontal = StringUtils.getField(justify, "horizontal");

        //vertical
        if(vertical.equals("center")) yPos = (int)(ent.y_pos + ent.getHeight()/2 + Engine.get().getGraphics().getTextBounds(text, currentFontSize).y/2  ) + yOffset;
        if(vertical.equals("bottom")) yPos = (int)(ent.y_pos + Engine.get().getGraphics().getTextBounds(text, currentFontSize).y)+ yOffset;
        if(vertical.equals("top")) yPos = (int)(ent.y_pos + ent.getHeight() - Engine.get().getGraphics().getTextBounds(text, currentFontSize).y)+ yOffset;

        //horizontal
        if(horizontal.equals("center")) xPos = (int)(ent.x_pos + ent.getWidth()/2 - Engine.get().getGraphics().getTextBounds(text, currentFontSize).x/2  ) + xOffset;
        if(horizontal.equals("left")) xPos = (int)(ent.x_pos) + xOffset;
        if(horizontal.equals("right")) xPos = (int)(ent.x_pos + ent.getWidth() - Engine.get().getGraphics().getTextBounds(text, currentFontSize).x) + xOffset;
    }

    //getters ====================================================================
    public String getText() { return text; }
    public int getFontSize() { return currentFontSize; }
    public int getOldFontSize() { return oldFontSize; }
    public int getXPos() { return xPos; }
    public int getYPos() { return yPos; }
    public float getR() { return r; }
    public float getG() { return g; }
    public float getB() { return b; }
    public float getA() { return a; }


    //setters ====================================================================
    public void setText(String newText) { text = newText; }
    public void setFontSize(int newFontSize) { currentFontSize = newFontSize; }
    public void setXPos(int newX) { xPos = newX; }
    public void setYPos(int newY) { yPos = newY; }
    public void setColor(float r, float g, float b, float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }
}

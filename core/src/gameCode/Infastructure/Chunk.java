package gameCode.Infastructure;

import com.badlogic.gdx.graphics.Pixmap;
import java.util.ArrayList;
import gameCode.Utilities.MathUtils;

public class Chunk {

    private String name;
    private boolean active;
    private Pixmap image;
    private ArrayList<String> serializedObjects;

    public Chunk() {
        name = "";
        active = false;
        image = new Pixmap(0, 0, Pixmap.Format.RGB888);
    }

    public boolean isImageEmpty() {
        if(image.getWidth() == 0 && image.getHeight() == 0) return true;
        else return false;
    }

    public boolean isImageBlank() {

        for(int y = 0; y < image.getHeight(); y++) {
            for(int x = 0; x < image.getWidth(); x++) {
                if(image.getPixel(x, y) != 0)
                    return false;
            }
        }

        return true;
    }

    public void createImage(int width, int height) {
        width = MathUtils.clamp(width, 0, 100);
        height = MathUtils.clamp(height, 0, 100);
        deleteImage();
        image = new Pixmap(width, height, Pixmap.Format.RGB888);
    }

    //getters
    public Pixmap getImage() { return image; }
    public String getName() { return name; }
    public boolean getActive() { return active; }
    public ArrayList<String> getObjects() { return serializedObjects; }

    //setters
    public void setName(String newName) { name = newName; }
    public void setActive(boolean newActive) { active = newActive; }
    public void setImage(Pixmap newImage) { image = newImage; }
    public void deleteImage() { image.dispose(); }
    public void addObject(String newObj) { /*serializedObjects.add(newObj); */ }


    //ADD THESE LATER ========================================
    public void setPixel() {}
    public int getPixel() {return 0;}
}

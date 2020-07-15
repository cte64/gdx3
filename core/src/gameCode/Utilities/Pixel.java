package gameCode.Utilities;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import gameCode.Infastructure.World;
import gameCode.Utilities.StringUtils;

public class Pixel {


    private static class Colors {
        String type;
        int color;
        Pixmap image;

        Colors(String newType, int newColor, String filename) {
            type = newType;
            color = newColor;
            if(filename == "") return;
            FileHandle file = Gdx.files.internal("core/assets/terrainTextures/" + filename);
            if(!file.exists()) {
                System.out.println("Texture wasn't found: " + filename);
                image = null;
            }
            else {
                image = new Pixmap(file);
            }
        }
    }

    private static Colors[] colors;

    public static void init() {

        //set all of them to empty values first
        colors = new Colors[256];
        for(int x = 0; x < 256; x++) { colors[x] = new Colors("", 0, ""); }

        //now add stuff
        colors[0] = new Colors("empty", 0, "");
        colors[1] = new Colors("dirts", 1, "clayTexture.png");
    }

    public static int charToColor(char b) {
        int index = (int)b;
        index = MathUtils.clamp(index, 0, 255);
        return colors[index].color;
    }

    public static char getCharFromType(String type) {
        char retVal = (byte)0;
        for(int x = 0; x < 256; x++) {
            if(colors[x].type == type)
                retVal = (char)x;
        }
        return retVal;
    }

    public static void insertPixel(StringUtils tiles, int xPixel, int yPixel, char terrainChar) {
    }


    public static Pixmap stringToImage(StringUtils data) {

        int pixPerTile = World.tileSize * World.tileSize;
        Pixmap image = new Pixmap(World.tileSize, World.tileSize, Pixmap.Format.RGB888);

        if(data.data.length() == 1) {
            int color = charToColor(data.data.charAt(0));
            for(int y = 0; y < World.tileSize; y++) {
            for(int x = 0; x < World.tileSize; x++) {
                image.drawPixel(x, y, color);
            }}
        }

        if(data.data.length() == pixPerTile) {
            for(int y = 0; y < World.tileSize; y++) {
            for(int x = 0; x < World.tileSize; x++) {
                int index = (y * World.tileSize) + x;
                index = MathUtils.clamp(index, 0, pixPerTile);
                int color = charToColor( data.data.charAt(index) );
                image.drawPixel(x, y, color);
            }}
        }

        return image;
    }

}

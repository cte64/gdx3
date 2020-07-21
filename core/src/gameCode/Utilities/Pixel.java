package gameCode.Utilities;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import gameCode.Infastructure.World;
import gameCode.Utilities.StringUtils;

public class Pixel {

    private static class Colors {
        public String type;
        public int defaultColor;
        public Pixmap image;
        Colors(String newType, int newColor, String filename) {

            type = newType;
            defaultColor = newColor;
            image = null;

            if(filename == "") return;
            FileHandle file = Gdx.files.internal("core/assets/terrainTextures/" + filename);
            if(!file.exists()) { System.out.println("Texture wasn't found: " + filename); }
            else { image = new Pixmap(file); }
        }
    }

    private static Colors[] colors;

    public static void init() {

        //set all of them to empty values first
        colors = new Colors[256];
        for(int x = 0; x < 256; x++) { colors[x] = new Colors("", 0, ""); }

        //now add stuff
        colors[0] = new Colors("empty", 0, "emptyTexture.png");
        colors[1] = new Colors("dirt", 0, "dirtTexture.png");
        colors[2] = new Colors("clay", 0, "clayTexture.png");
        colors[3] = new Colors("coal", 0, "coalTexture.png");
        colors[4] = new Colors("stone", 0, "stoneTexture.png");
    }

    public static int charToColor(char b) {
        int index = (int)b;
        index = MathUtils.clamp(index, 0, 255);
        return colors[index].defaultColor;
    }

    public static int charToColor(char b, int x, int y) {

        int index = (int)b;
        index = MathUtils.clamp(index, 0, 255);
        if(colors[index].image == null) return colors[index].defaultColor;

        x = MathUtils.clamp(x, 0, colors[index].image.getWidth());
        y = MathUtils.clamp(y, 0, colors[index].image.getHeight());
        return colors[index].image.getPixel(x, y);
    }

    public static char getCharFromType(String type) {
        char retVal = (byte)0;
        for(int x = 0; x < 256; x++) {
            if(colors[x].type == type )
                retVal = (char)x;
        }
        return retVal;
    }

    public static void insertPixel(StringUtils tiles, int xPixel, int yPixel, char terrainChar) {

        int tSize = World.tileSize * World.tileSize;
        yPixel = 59 - yPixel;

        if(tiles.data.length() == tSize) tiles.replaceIndex(yPixel*World.tileSize + xPixel, terrainChar);

        if( tiles.data.length() == 1) {
            char firstPixel = tiles.data.charAt(0);
            tiles.data = "";
            for(int x = 0; x<tSize; x++) { tiles.data += firstPixel; }
            tiles.replaceIndex(yPixel*World.tileSize + xPixel, terrainChar);
        }

        if( tiles.data.length() == 0) {
            char emptyPixel = getCharFromType("empty");
            tiles.data = "";
            for(int x = 0; x<tSize; x++) { tiles.data += emptyPixel; }
            tiles.replaceIndex(yPixel*World.tileSize + xPixel, terrainChar);
        }
    }

    public static Pixmap stringToImage(StringUtils data) {

        int pixPerTile = World.tileSize * World.tileSize;
        Pixmap image = new Pixmap(World.tileSize, World.tileSize, Pixmap.Format.RGB888);

        if(data.data.length() == 0) {
            int color = 16777215;
            for(int y = 0; y < World.tileSize; y++) {
            for(int x = 0; x < World.tileSize; x++) {
                image.drawPixel(x, y, color);
            }}
        }

        else if(data.data.length() == 1) {

            for(int y = 0; y < World.tileSize; y++) {
            for(int x = 0; x < World.tileSize; x++) {
                int color = charToColor(data.data.charAt(0), x, y);
                image.drawPixel(x, y, color);
            }}
        }

        else if(data.data.length() == pixPerTile) {
            for(int y = 0; y < World.tileSize; y++) {
            for(int x = 0; x < World.tileSize; x++) {
                int index = (y * World.tileSize) + x;
                index = MathUtils.clamp(index, 0, pixPerTile);
                int color = charToColor( data.data.charAt(index), x, y);
                image.drawPixel(x, y, color);
            }}
        }

        return image;
    }
}

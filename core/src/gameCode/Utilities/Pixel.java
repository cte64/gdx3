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
        for(int x = 0; x < 256; x++) {
            colors[x] = new Colors("", 0, "");
        }

        //now add stuff
        colors[0] = new Colors("empty", 0, "");
        colors[1] = new Colors("dirt", 1, "clayTexture.png");
    }


    public static int byteToColor(byte b) {
        int index = (int)b;
        index = MathUtils.clamp(index, 0, 255);
        return 16777215;//colors[index].color;
    }

    public static byte getCharFromType(String type) {
        byte retVal = (byte)0;
        return retVal;
    }

    public static void insertPixel(Byte tiles, int xPixel, int yPixel, byte terrainChar) {
    }


    public static Pixmap stringToImage(Byte data) {

        int pixPerTile = World.tileSize * World.tileSize;
        Pixmap image = new Pixmap(World.tileSize, World.tileSize, Pixmap.Format.RGB888);

        if(data.data.length == 1) {
            int color = byteToColor(data.data[0]);
            for(int y = 0; y < World.tileSize; y++) {
            for(int x = 0; x < World.tileSize; x++) {
                image.drawPixel(x, y, color);
            }}
        }

        if(data.data.length == pixPerTile) {
            for(int y = 0; y < World.tileSize; y++) {
            for(int x = 0; x < World.tileSize; x++) {
                int index = (y * World.tileSize) + x;
                index = MathUtils.clamp(index, 0, pixPerTile);
                int color = byteToColor( data.data[index] );
                image.drawPixel(x, y, color);
            }}
        }

        return image;
    }

}

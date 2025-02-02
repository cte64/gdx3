package gameCode.Utilities;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.mygdx.game.Engine;
import gameCode.Infrastructure.myWorld;



/*

Material Hardness ranges from 0 to 100 for now

 */




public class Pixel {

    private static class Colors {
        public String type;
        public int defaultColor;
        public int hardness;

        Colors(String newType, int newColor, int hardness) {
            type = newType;
            defaultColor = newColor;
            this.hardness = hardness; //max hardness is 100 for now
        }
    }

    private static Colors[] colors;

    public static void init() {

        //set all of them to empty values first
        colors = new Colors[256];
        for(int x = 0; x < 256; x++) { colors[x] = new Colors("", 0, 0); }

        //now add stuff
        colors[0] = new Colors("empty", 0, 0);
        colors[1] = new Colors("dirt", 0, 10);
        colors[2] = new Colors("clay", 0,  10);
        colors[3] = new Colors("coal", 0,  10);
        colors[4] = new Colors("stone", 0,  30);
        colors[5] = new Colors("ruby", 0,  50);
        colors[6] = new Colors("emerald", 0, 70);
        colors[7] = new Colors("sulfur", 0,  40);
        colors[8] = new Colors("lava", 0,  0);
        colors[9] = new Colors("sand", 0,  5);
        colors[10] = new Colors("bloodstone", 0,  45);
    }

    public static int charToColor(char b) {
        int index = (int)b;
        index = myMath.clamp(index, 0, 255);
        return colors[index].defaultColor;
    }

    public static int charToColor(char b, int x, int y) {
        int index = (int)b;
        index = myMath.clamp(index, 0, 255);



        return Engine.get().getAssets().getPixel(colors[index].type + "Texture", x, y);
       // return Engine.get().getAssets().getPixel(colors[index].type + "Texture", x, y);
    }

    public static char getCharFromType(String type) {
        char retVal = (byte)0;
        for(int x = 0; x < 256; x++) {
            if(colors[x].type == type )
                retVal = (char)x;
        }
        return retVal;
    }

    public static void insertPixel(myString tiles, int xPixel, int yPixel, char terrainChar) {

        int tSize = myWorld.get().tileSize * myWorld.get().tileSize;
        yPixel = 59 - yPixel;

        if(tiles.data.length() == tSize) tiles.replaceIndex(yPixel* myWorld.get().tileSize + xPixel, terrainChar);

        if( tiles.data.length() == 1) {
            char firstPixel = tiles.data.charAt(0);
            tiles.data = "";
            for(int x = 0; x<tSize; x++) { tiles.data += firstPixel; }
            tiles.replaceIndex(yPixel* myWorld.get().tileSize + xPixel, terrainChar);
        }

        if( tiles.data.length() == 0) {
            char emptyPixel = getCharFromType("empty");
            tiles.data = "";
            for(int x = 0; x<tSize; x++) { tiles.data += emptyPixel; }
            tiles.replaceIndex(yPixel* myWorld.get().tileSize + xPixel, terrainChar);
        }
    }

    public static Pixmap stringToImage(myString data) {

        int pixPerTile = myWorld.get().tileSize * myWorld.get().tileSize;
        Pixmap image = new Pixmap(myWorld.get().tileSize, myWorld.get().tileSize, Pixmap.Format.RGB888);


        //System.out.println(data.data.length());

        /*
        if(data.data.length() == 0) {
            int color = 16777215;
            for(int y = 0; y < myWorld.get().tileSize; y++) {
            for(int x = 0; x < myWorld.get().tileSize; x++) {
                image.drawPixel(x, y, color);
            }}
        }

         */

        if(data.data.length() == 1) {

            for(int y = 0; y < myWorld.get().tileSize; y++) {
            for(int x = 0; x < myWorld.get().tileSize; x++) {
                int color = charToColor(data.data.charAt(0), x, y);
                image.drawPixel(x, y, color);
            }}
        }

        else if(data.data.length() == pixPerTile) {
            for(int y = 0; y < myWorld.get().tileSize; y++) {
            for(int x = 0; x < myWorld.get().tileSize; x++) {
                int index = (y * myWorld.get().tileSize) + x;
                index = myMath.clamp(index, 0, pixPerTile);
                int color = charToColor( data.data.charAt(index), x, y);
                image.drawPixel(x, y, color);
            }}
        }

        return image;
    }

    public static int getHardnessByType(String type) {
        int retVal = 0;
        for(int x = 0; x < 256; x++) {
            if(colors[x].type.equals(type)) retVal = colors[x].hardness;
        }
        return retVal;
    }

    public static String getTypeFromChar(char c) {
        int index = (int)c;
        index = myMath.clamp(index, 0, 255);
        return colors[index].type;
    }
}

package gameCode.Utilities;

import com.badlogic.gdx.graphics.Pixmap;
import gameCode.Infastructure.World;
import gameCode.Utilities.StringUtils;

public class Pixel {

    public static int byteToColor(byte b) {
        int index = (int)b;
        if(index == 48) {
            System.out.println("we got here");
            return 16777215;
        }
        else return 10711935;
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

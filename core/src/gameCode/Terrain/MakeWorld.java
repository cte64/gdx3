package gameCode.Terrain;

import com.badlogic.gdx.math.Vector2;
import gameCode.Infastructure.FileSystem;
import gameCode.Infastructure.World;
import gameCode.Utilities.MathUtils;
import gameCode.Utilities.Pixel;
import gameCode.Utilities.StringUtils;

import java.security.KeyStore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MakeWorld {



    private int lowestPoint;
    private int layerC;
    private int stretch;
    private int scale;
    private boolean fillIt;
    private char terrainType;
    private int minDepth = 1000;
    private int worldRadius;
    private Map< String, ArrayList<Vector2> > rims;


    private void cleanUpRims() {

        byte emptyChar = Pixel.getCharFromType("empty");

        //ASSIGN EACH COORDINATE TO ITS APPROPRIATE CHUNKGRID INDEX ============
        for (Map.Entry<String, ArrayList<Vector2>> rim: rims.entrySet()) {

            ArrayList< ArrayList<Vector2> > chunkGrid = new ArrayList< ArrayList<Vector2> >();

            for (int x = 0; x < World.getNumChunks() * World.getNumChunks(); x++)
                chunkGrid.add(   new ArrayList<Vector2>()   );

            for (Vector2 coord : rim.getValue()) {

                int xChunk = (int)((coord.x * World.getNumChunks()) / World.getNumPixels());
                int yChunk = (int)((coord.y * World.getNumChunks()) / World.getNumPixels());

                int index = (yChunk * World.getNumChunks()) + xChunk;
                index = MathUtils.clamp(index, 0, chunkGrid.size() - 1);
                chunkGrid.get(index).add(coord);
            }

            //GET RID OF RIM COORDINATES THAT REPRESENT EMPTY SPACE ========================================
            for (int yChunk = 0; yChunk < World.getNumChunks(); yChunk++) {
            for (int xChunk = 0; xChunk < World.getNumChunks(); xChunk++) {

                StringUtils data = new StringUtils("");
                String fileName = "[type: terrain][xChunk: ][yChunk: ]";
                StringUtils.setField(fileName, "xChunk", StringUtils.toString(xChunk));
                StringUtils.setField(fileName, "yChunk", StringUtils.toString(yChunk));
                FileSystem.getFile(new StringUtils(fileName), data);
                String[] tiles = data.data.split("\n");

                int index = (yChunk * World.getNumChunks()) + xChunk;
                index = MathUtils.clamp(index, 0, chunkGrid.size() - 1);

                for(Vector2 i: chunkGrid.get(index)) {

                    int xTile = (int)((i.x / World.tileSize) % World.tilesPerChunk);
                    int yTile = (int)((i.y / World.tileSize) % World.tilesPerChunk);

                    int tileIndex = (yTile * World.tilesPerChunk) + xTile;
                    tileIndex = MathUtils.clamp(tileIndex, 0, tiles.length - 1);

                    if (tiles[tileIndex].length() == 1 && tiles[tileIndex].charAt(0) != emptyChar) continue;

                    int pixelX = (int)(i.x % World.tileSize);
                    int pixelY = (int)(i.y % World.tileSize);

                    int pixIndex = (pixelY * World.tileSize) + pixelX;
                    pixIndex = MathUtils.clamp(pixIndex, 0, tiles[tileIndex].length() - 1);

                    if (rim.getValue().size() == 0) break;
                    if (tiles[tileIndex].charAt(pixIndex) == emptyChar) {
                        rim.getValue().remove(i);
                    }
                }
            }}
        }
    }

    private void addPixelsToGame(ArrayList<Vector2> coords, String type) {

        byte terrainChar = Pixel.getCharFromType(type);
        byte emptyChar = Pixel.getCharFromType("empty");

        //CREATE A CHUNKGRID ===================================================
        ArrayList< ArrayList<Vector2> > chunkGrid = new ArrayList< ArrayList<Vector2> >();
        for (int x = 0; x < World.getNumChunks() * World.getNumChunks(); x++)
            chunkGrid.add( new ArrayList<Vector2>() );

        //ASSIGN EACH COORDINATE TO ITS APPROPRIATE CHUNKGRID INDEX ============
        for(Vector2 coord: coords) {

            int xPos = (int)coord.x;
            int yPos = (int)coord.y;

            int xChunk = (xPos * World.getNumChunks()) / World.getNumPixels();
            int yChunk = (yPos * World.getNumChunks()) / World.getNumPixels();

            int index = yChunk * World.getNumChunks() + xChunk;
            index = MathUtils.clamp(index, 0, chunkGrid.size() - 1);

            chunkGrid.get(index).add(coord);
        }

        //PLACE TERRAIN INTO GAMECHUNKS ========================================
        for (int yChunk = 0; yChunk < World.getNumChunks(); yChunk++) {
        for (int xChunk = 0; xChunk < World.getNumChunks(); xChunk++) {

            StringUtils data = new StringUtils("");
            StringUtils fileName = new StringUtils("[type: terrain][xChunk: ][yChunk: ]");
            StringUtils.setField(fileName, "xChunk", StringUtils.toString(xChunk));
            StringUtils.setField(fileName, "yChunk", StringUtils.toString(yChunk));

            FileSystem.getFile(fileName, data);

            String[] tiles = data.data.split("\n");

            int index = yChunk * World.getNumChunks() + xChunk;
            index = MathUtils.clamp(index, 0, chunkGrid.size() - 1);

            for (int z = 0; z < chunkGrid.get(index).size(); z++) {

                int xPosPixel = (int)chunkGrid.get(index).get(z).x;
                int yPosPixel = (int)chunkGrid.get(index).get(z).y;

                int xTile = (xPosPixel / World.tileSize) % World.tilesPerChunk;
                int yTile = (yPosPixel / World.tileSize) % World.tilesPerChunk;

                int xPixel = xPosPixel % World.tileSize;
                int yPixel = yPosPixel % World.tileSize;

                int tileIndex = (yTile * World.tilesPerChunk) + xTile;
                tileIndex = MathUtils.clamp(tileIndex, 0, tiles.length - 1);

                if (tiles[tileIndex].length() > 0) {

                    int pixIndex = yPixel * World.tileSize + xPixel;

        /*
                    if ((tiles[tileIndex].length() == 1 && tiles[tileIndex].charAt(0) != emptyChar))
                        Pixel.insertPixel(tiles[tileIndex], xPixel, yPixel, terrainChar);



                    else if (pixIndex < tiles[tileIndex].size())
                        pix.insertPixel(tiles[tileIndex], xPixel, yPixel, terrainChar);
         */
                }

            }


            /*
            std::string updatedChunk = "";
            for (int x = 0; x < tiles.size(); x++) updatedChunk += tiles[x] + "\n";

            std::string name = "[type: chunk][xChunk: " + st.toString(xChunk) + "][yChunk: " + st.toString(yChunk) + "]";
            world.fileThing.setFile(name, updatedChunk);
         */
        }}

    }

    public MakeWorld() {
        rims = new HashMap< String, ArrayList<Vector2> >();
    }


    void fillTerrain(ArrayList<Float> newTotal, String rimName){}
    void makeLayer(int newLowest, int newStretch, int newOctaves, String newType, boolean newFillIt, String rimName){}
    void makeMap(String directory, int numChunks, int tRadius, StringUtils message, StringUtils loadingBar) {}
    int getMinDepth() { return minDepth; }
}

package gameCode.Terrain;

import com.badlogic.gdx.math.Vector2;
import gameCode.Infastructure.FileSystem;
import gameCode.Infastructure.World;
import gameCode.Utilities.MathUtils;
import gameCode.Utilities.Pixel;
import gameCode.Utilities.StringUtils;

import java.util.ArrayList;

public class ScatterTerrain {


    private class layer {
        float seedsPerChunk;
        int lowerBound;
        int upperBound;
        float meanPix;
        float stdDevPix;
        float meanGran;
        float stdDevGran;
        float mDiv;
        float sdDiv;

        layer(int newLB, int newUB, float newS, float newMPix, float newSDPix, float newMGran, float newSDGran, float newMDiv, float newSDDiv) {
            lowerBound = newLB;
            upperBound = newUB;
            seedsPerChunk = newS;
            meanPix = newMPix;
            stdDevPix = newSDPix;
            meanGran = newMGran;
            stdDevGran = newSDGran;
            mDiv = newMDiv;
            sdDiv = newSDDiv;
        }
    };

    private class seed {
        int currentX;
        int currentY;
        int numPixels;
        float meanGran;
        float stdDevGran;
        float mDiv;
        float sdDiv;
    };

    ArrayList<layer> layers;
    ArrayList<seed> seeds;
    ArrayList < ArrayList <Vector2> > chunkGrid;
    char terrainChar;

    private void placeSeeds() {

        // TURN THE SEEDS INTO ACTUAL COORDINATES================
        for (layer lay: layers) {

            int outerArea = (int)(lay.upperBound * lay.upperBound * MathUtils.PI);
            int lowerArea = (int)(lay.lowerBound * lay.lowerBound * MathUtils.PI);
            int area = outerArea - lowerArea;
            int chunkArea = (World.tilesPerChunk * World.tilesPerChunk) * (World.tileSize * World.tileSize);
            int numSeeds = (int)((area / chunkArea) * lay.seedsPerChunk);

            for (int z = 0; z < numSeeds; z++) {

                int radius = (int)(lay.lowerBound + (Math.random() * (lay.upperBound - lay.lowerBound)));
                float angle = (float)((Math.random() * 3650) / 10.0);

                int xPos = (int)((World.getNumPixels() / 2) + radius * Math.cos(MathUtils.toRad(angle)));
                int yPos = (int)((World.getNumPixels() / 2) + radius * Math.sin(MathUtils.toRad(angle)));
                int newNum = (int)MathUtils.getGauss(lay.meanPix, lay.stdDevPix);
                newNum = MathUtils.clamp(newNum, 1, 500);

                seed s = new seed();
                s.currentX = xPos;
                s.currentY = yPos;
                s.meanGran = lay.meanGran;
                s.stdDevGran = lay.stdDevGran;
                s.numPixels = newNum;
                s.mDiv = lay.mDiv;
                s.sdDiv = lay.sdDiv;
                seeds.add(s);
            }
        }
    }
    private void growSeeds() {

        for (seed s : seeds) {

            for (int z = 0; z < s.numPixels; z++) {

                float div = (float)MathUtils.getGauss(s.mDiv, s.sdDiv);
                int gran = (int)MathUtils.getGauss(s.meanGran, s.stdDevGran);

                div = MathUtils.clamp(div, 0.7f, 2.5f);
                gran = MathUtils.clamp(gran, 10, 1000);

                float angle = MathUtils.toRad((float)(Math.random() * 2000));
                int newX = s.currentX + (int)((gran / div) * Math.cos(angle));
                int newY = s.currentY + (int)((gran / div) * Math.sin(angle));

                newX = MathUtils.clamp(newX, 0, World.getNumPixels() - 1);
                newY = MathUtils.clamp(newY, 0, World.getNumPixels() - 1);

                s.currentX = newX;
                s.currentY = newY;

                ArrayList<Float> a = new ArrayList<Float>();
                Perlin perlin = new Perlin((gran / 4), (int)(gran * MathUtils.PI), 6, (gran / 4), a);

                int centerX = gran / 2;
                int centerY = gran / 2;

                for (int y = 0; y < gran; y++) {
                for (int x = 0; x < gran; x++) {

                    float cellAngle = MathUtils.angleBetweenCells(x, y, centerX, centerY);

                    int index = (int)((cellAngle / 365.0) * a.size());
                    index = MathUtils.clamp(index, 0, a.size() - 1);
                    int m = (int)MathUtils.mag(x, y, centerX, centerY);

                    int xPos = newX + x;
                    int yPos = newY + y;
                    xPos = MathUtils.clamp(xPos, 0, World.getNumPixels() - 1);
                    yPos = MathUtils.clamp(yPos, 0, World.getNumPixels() - 1);

                    if (m <= a.get(index)) {

                        int xIndex = (xPos * World.getNumChunks()) / World.getNumPixels();
                        int yIndex = (yPos * World.getNumChunks()) / World.getNumPixels();

                        int innerIndex = (yIndex * World.getNumChunks()) + xIndex;
                        innerIndex = MathUtils.clamp(innerIndex, 0, chunkGrid.size() - 1);

                        chunkGrid.get(innerIndex).add( new Vector2(xPos, yPos) );
                    }
                }}
            }
        }
    }
    private void addToGame() {

        char emptyChar = Pixel.getCharFromType("empty");

        //PLACE TERRAIN INTO GAMECHUNKS ===========================
        for (int yChunk = 0; yChunk < World.getNumChunks(); yChunk++) {
        for (int xChunk = 0; xChunk < World.getNumChunks(); xChunk++) {

            StringUtils data = new StringUtils("");
            StringUtils fileName = new StringUtils("[type: chunk][xChunk: ][yChunk: ]");
            fileName.setField(fileName, "xChunk", StringUtils.toString(xChunk));
            fileName.setField(fileName, "yChunk", StringUtils.toString(yChunk));
            FileSystem.getFile(fileName, data);

            ArrayList<StringUtils> tiles = StringUtils.getBeforeChar(data.data, '\n');

            int index = (yChunk * World.getNumChunks()) + xChunk;
            index = MathUtils.clamp(index, 0, chunkGrid.size() - 1);

            for (int z = 0; z < chunkGrid.get(index).size(); z++) {

                int xPosPixel = (int)chunkGrid.get(index).get(z).x;
                int yPosPixel = (int)chunkGrid.get(index).get(z).y;

                int xTile = (xPosPixel / World.tileSize) % World.tilesPerChunk;
                int yTile = (yPosPixel / World.tileSize) % World.tilesPerChunk;

                int xPixel = xPosPixel % World.tileSize;
                int yPixel = yPosPixel % World.tileSize;

                int tileIndex = (yTile * World.tilesPerChunk) + xTile;
                tileIndex = MathUtils.clamp(tileIndex, 0, tiles.size() - 1);

                if(tiles.get(tileIndex).data.length() > 0) {

                    int pixIndex = (yPixel * World.tileSize) + xPixel;

                    if ((tiles.get(tileIndex).data.length() == 1 && tiles.get(tileIndex).data.charAt(0) != emptyChar))
                        Pixel.insertPixel(tiles.get(tileIndex), xPixel, yPixel, terrainChar);

                    else if (pixIndex < tiles.get(tileIndex).data.length() && tiles.get(tileIndex).data.charAt(pixIndex) != emptyChar)
                        Pixel.insertPixel(tiles.get(tileIndex), xPixel, yPixel, terrainChar);
                }
            }

            StringUtils updatedChunk = new StringUtils("");
            for (int x = 0; x < tiles.size(); x++) { updatedChunk.data += tiles.get(x).data; updatedChunk.data += "\n"; }
            StringUtils name = new StringUtils("[type: chunk][xChunk: " + StringUtils.toString(xChunk) + "][yChunk: " + StringUtils.toString(yChunk) + "]");
            FileSystem.setFile(name, updatedChunk);
        }}
    }
    public ScatterTerrain(String type) {
        terrainChar = Pixel.getCharFromType(type);
        layers = new ArrayList<layer>();
        seeds = new ArrayList<seed>();
        chunkGrid = new ArrayList< ArrayList<Vector2> >();
        for(int x = 0; x < World.getNumChunks() * World.getNumChunks(); x++) { chunkGrid.add( new ArrayList<Vector2>() ); }
    }
    public void addLayer(int newLB, int newUB, int newS, float mSeeds, float sdSeeds, float mGran, float sdGran, float mDiv, float sdDiv) {
        layer l = new layer(newLB, newUB, newS, mSeeds, sdSeeds, mGran, sdGran, mDiv, sdDiv);
        layers.add(l);
    }
    public void make() {
        placeSeeds();
        growSeeds();
        addToGame();
    }
}

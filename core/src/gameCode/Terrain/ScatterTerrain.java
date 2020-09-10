package gameCode.Terrain;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Engine;
import gameCode.Infrastructure.World;
import gameCode.Utilities.myMath;
import gameCode.Utilities.Pixel;
import gameCode.Utilities.myString;

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

            int outerArea = (int)(lay.upperBound * lay.upperBound * myMath.PI);
            int lowerArea = (int)(lay.lowerBound * lay.lowerBound * myMath.PI);
            int area = outerArea - lowerArea;
            int chunkArea = (World.get().tilesPerChunk * World.get().tilesPerChunk) * (World.get().tileSize * World.get().tileSize);
            int numSeeds = (int)((area / chunkArea) * lay.seedsPerChunk);

            for (int z = 0; z < numSeeds; z++) {

                int radius = (int)(lay.lowerBound + (Math.random() * (lay.upperBound - lay.lowerBound)));
                float angle = (float)((Math.random() * 3650) / 10.0);

                int xPos = (int)((World.get().getNumPixels() / 2) + radius * Math.cos(myMath.toRad(angle)));
                int yPos = (int)((World.get().getNumPixels() / 2) + radius * Math.sin(myMath.toRad(angle)));
                int newNum = (int) myMath.getGauss(lay.meanPix, lay.stdDevPix);
                newNum = myMath.clamp(newNum, 1, 500);

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

                float div = (float) myMath.getGauss(s.mDiv, s.sdDiv);
                int gran = (int) myMath.getGauss(s.meanGran, s.stdDevGran);

                div = myMath.clamp(div, 0.7f, 2.5f);
                gran = myMath.clamp(gran, 10, 1000);

                float angle = myMath.toRad((float)(Math.random() * 2000));
                int newX = s.currentX + (int)((gran / div) * Math.cos(angle));
                int newY = s.currentY + (int)((gran / div) * Math.sin(angle));

                newX = myMath.clamp(newX, 0, World.get().getNumPixels() - 1);
                newY = myMath.clamp(newY, 0, World.get().getNumPixels() - 1);

                s.currentX = newX;
                s.currentY = newY;

                ArrayList<Float> a = new ArrayList<Float>();
                Perlin perlin = new Perlin((gran / 4), (int)(gran * myMath.PI), 6, (gran / 4), a);

                int centerX = gran / 2;
                int centerY = gran / 2;

                for (int y = 0; y < gran; y++) {
                for (int x = 0; x < gran; x++) {

                    float cellAngle = myMath.angleBetweenCells(x, y, centerX, centerY);

                    int index = (int)((cellAngle / 365.0) * a.size());
                    index = myMath.clamp(index, 0, a.size() - 1);
                    int m = (int) myMath.mag(x, y, centerX, centerY);

                    int xPos = newX + x;
                    int yPos = newY + y;
                    xPos = myMath.clamp(xPos, 0, World.get().getNumPixels() - 1);
                    yPos = myMath.clamp(yPos, 0, World.get().getNumPixels() - 1);

                    if (m <= a.get(index)) {

                        int xIndex = (xPos * World.get().getNumChunks()) / World.get().getNumPixels();
                        int yIndex = (yPos * World.get().getNumChunks()) / World.get().getNumPixels();

                        int innerIndex = (yIndex * World.get().getNumChunks()) + xIndex;
                        innerIndex = myMath.clamp(innerIndex, 0, chunkGrid.size() - 1);

                        chunkGrid.get(innerIndex).add( new Vector2(xPos, yPos) );
                    }
                }}
            }
        }
    }
    private void addToGame() {

        char emptyChar = Pixel.getCharFromType("empty");

        //PLACE TERRAIN INTO GAMECHUNKS ===========================
        for (int yChunk = 0; yChunk < World.get().getNumChunks(); yChunk++) {
        for (int xChunk = 0; xChunk < World.get().getNumChunks(); xChunk++) {

            myString data = new myString("");
            myString fileName = new myString("[type: chunk][xChunk: ][yChunk: ]");
            fileName.setField("xChunk", myString.toString(xChunk));
            fileName.setField("yChunk", myString.toString(yChunk));
            Engine.get().getFileSystem().getFile(fileName, data);

            ArrayList<myString> tiles = myString.getBeforeChar(data.data, '\n');

            int index = (yChunk * World.get().getNumChunks()) + xChunk;
            index = myMath.clamp(index, 0, chunkGrid.size() - 1);

            for (int z = 0; z < chunkGrid.get(index).size(); z++) {

                int xPosPixel = (int)chunkGrid.get(index).get(z).x;
                int yPosPixel = (int)chunkGrid.get(index).get(z).y;

                int xTile = (xPosPixel / World.get().tileSize) % World.get().tilesPerChunk;
                int yTile = (yPosPixel / World.get().tileSize) % World.get().tilesPerChunk;

                int xPixel = xPosPixel % World.get().tileSize;
                int yPixel = yPosPixel % World.get().tileSize;

                int tileIndex = (yTile * World.get().tilesPerChunk) + xTile;
                tileIndex = myMath.clamp(tileIndex, 0, tiles.size() - 1);

                if(tiles.get(tileIndex).data.length() > 0) {

                    int pixIndex = (yPixel * World.get().tileSize) + xPixel;

                    if ((tiles.get(tileIndex).data.length() == 1 && tiles.get(tileIndex).data.charAt(0) != emptyChar))
                        Pixel.insertPixel(tiles.get(tileIndex), xPixel, yPixel, terrainChar);

                    else if (pixIndex < tiles.get(tileIndex).data.length() && tiles.get(tileIndex).data.charAt(pixIndex) != emptyChar)
                        Pixel.insertPixel(tiles.get(tileIndex), xPixel, yPixel, terrainChar);
                }
            }

            myString updatedChunk = new myString("");
            for (int x = 0; x < tiles.size(); x++) { updatedChunk.data += tiles.get(x).data; updatedChunk.data += "\n"; }
            myString name = new myString("[type: chunk][xChunk: " + myString.toString(xChunk) + "][yChunk: " + myString.toString(yChunk) + "]");
            Engine.get().getFileSystem().setFile(name, updatedChunk);
        }}
    }
    public ScatterTerrain(String type) {
        terrainChar = Pixel.getCharFromType(type);
        layers = new ArrayList<layer>();
        seeds = new ArrayList<seed>();
        chunkGrid = new ArrayList< ArrayList<Vector2> >();
        for(int x = 0; x < World.get().getNumChunks() * World.get().getNumChunks(); x++) { chunkGrid.add( new ArrayList<Vector2>() ); }
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

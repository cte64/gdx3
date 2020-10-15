package gameCode.Terrain;

import com.badlogic.gdx.graphics.Pixmap;
import com.mygdx.game.Engine;
import gameCode.Infrastructure.Chunk;
import gameCode.Infrastructure.Entity;
import gameCode.Infrastructure.myWorld;
import gameCode.Utilities.myMath;
import gameCode.Utilities.myPair;
import gameCode.Utilities.myString;

import java.util.ArrayList;
import java.util.HashMap;

public class ModifyTerrain {


    public static void addPixels(ArrayList<PixelT> pixels) {
        HashMap<Entity, Pixmap> ents = new HashMap<Entity, Pixmap>();
        for(PixelT coord: pixels) {
            //update the chunk map =============================================================
            myPair<Integer, Integer> key = Chunk.makeKeyFromPixel(coord.x, coord.y);
            Chunk chunkPtr = myWorld.get().getChunk(key);
            if(chunkPtr == null) continue;

            chunkPtr.setPixel(coord.x, coord.y, coord.type);
            int tileX = (coord.x / myWorld.get().tileSize) % myWorld.get().tilesPerChunk;
            int tileY = (coord.y / myWorld.get().tileSize) % myWorld.get().tilesPerChunk;
            Pixmap image = chunkPtr.getImage(tileX, tileY);

            //update the entities if there are any =============================================
            for(Entity ent: myWorld.get().getEntByLocation(coord.x, coord.y)) {
                ents.put(ent, image);
            }
        }

        for(Entity ent: ents.keySet()) {
            if(!myString.getField(ent.entityName, "type").equals("tile")) continue;
            Engine.get().getAssets().updateSprite(ent.spriteName, ents.get(ent));
            Engine.get().getPhysics().resetGrid(ent);
        }
    }

    public static PixelT getTypeAt(int x, int y) {


        PixelT retVal = new PixelT(0, 0, "");
        myPair<Integer, Integer> key = Chunk.makeKeyFromPixel(x, y);
        Chunk chunkPtr = myWorld.get().getChunk(key);
        if(chunkPtr == null) return retVal;

        String name = chunkPtr.getPixel(x, y);
        String newName = "[type: terrainTexture][subType: " + name + "]";
        retVal.x = x;
        retVal.y = y;
        retVal.type = newName;

        return retVal;
    }

    public static HashMap<String, Integer> getPixelTypeAndCount(ArrayList<PixelT> pixels) {

        HashMap<String, Integer> retVal = new HashMap<>();

        for(PixelT pixel: pixels) {
            String subType = myString.getField(pixel.type, "subType");
            if(!subType.equals("empty")) {
                if(!retVal.containsKey(pixel.type)) retVal.put(pixel.type, 1);
                else retVal.put(pixel.type, retVal.get(pixel.type) + 1);
            }
        }

        return retVal;
    }

    private static void circlePixels(float cX, float cY, float radius, ArrayList<PixelT> coords) {
        float width = radius + 2;
        for (float y = -width; y < width; y++) {
            for (float x = -width; x < width; x++) {
                float mag = myMath.mag(0.0f, 0.0f, x, y);
                if (mag < radius) {
                    PixelT coord = getTypeAt((int)(x + cX), (int)(y + cY));
                    coords.add(coord);
                }
            }
        }
    }

    public static void filterPixels(ArrayList<PixelT> coords, String filterType, ArrayList<String> filterList) {

        ArrayList<PixelT> toRemove = new ArrayList<>();
        for(PixelT coord: coords) {

            String subType = myString.getField(coord.type, "subType");

            if(filterType.equals("include") && !filterList.contains(subType))
                toRemove.add(coord);

            if(filterType.equals("exclude") && filterList.contains(subType))
                toRemove.add(coord);
        }

        coords.removeAll(toRemove);
    }

    public static HashMap<String, Integer> deleteCircle(float cX, float cY, float radius, String filterType, ArrayList<String> filterList) {

        ArrayList<PixelT> coords = new ArrayList<>();
        circlePixels(cX, cY, radius, coords);


        filterPixels(coords, filterType, filterList);
        addPixels(coords);

        HashMap<String, Integer> retVal = getPixelTypeAndCount(coords);
        return retVal;
    }
}

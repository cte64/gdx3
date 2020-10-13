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

    public static HashMap<String, Integer> getPixels(ArrayList<PixelT> pixels) {

        HashMap<String, Integer> retVal = new HashMap<>();
        for(PixelT coord: pixels) {

            //update the chunk map =============================================================
            myPair<Integer, Integer> key = Chunk.makeKeyFromPixel(coord.x, coord.y);
            Chunk chunkPtr = myWorld.get().getChunk(key);
            if(chunkPtr == null) continue;

            String type = chunkPtr.getPixel(coord.x, coord.y);
            if(!retVal.containsKey(type)) retVal.put(type, 1);
            else retVal.put( type, retVal.get(type) + 1);
        }

        return retVal;
    }

    public static HashMap<String, Integer> addCircle(float centerX, float centerY, float radius, String type) {

        ArrayList<PixelT> pixels = new ArrayList<>();

        float width = radius + 2;
        for (float y = -width; y < width; y++) {
            for (float x = -width; x < width; x++) {
                float mag = myMath.mag(0.0f, 0.0f, x, y);
                if (mag < radius) {
                    pixels.add(new PixelT((int)(x + centerX), (int)(y + centerY), type));
                }
            }
        }

        HashMap<String, Integer> retVal = getPixels(pixels);
        addPixels(pixels);
        return retVal;
    }




}

package gameCode.Terrain;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.Vector2;
import gameCode.Infastructure.Chunk;
import gameCode.Infastructure.Entity;
import gameCode.Infastructure.Graphics;
import gameCode.Infastructure.World;
import gameCode.Utilities.myPair;
import jdk.internal.net.http.common.Pair;

import java.util.ArrayList;
import java.util.HashMap;

public class ModifyTerrain {


    public static void addPixels(ArrayList<myPair<Integer, Integer>> pixels) {

        /* to make this more efficient we will keep track of all the entities that
           get update and update them all at once that way we don't call the "updateSprite"
           function as many times
         */

        HashMap<Entity, Pixmap> ents = new HashMap<Entity, Pixmap>();
        for(myPair<Integer, Integer> coord: pixels) {

            //update the chunk map =============================================================
            myPair<Integer, Integer> key = Chunk.makeKeyFromPixel(coord.first, coord.second);

            Chunk chunkPtr = World.getChunk(key);
            if(chunkPtr == null) continue;

            chunkPtr.setPixel(coord.first, coord.second, "coal");
            int tileX = (coord.first / World.tileSize) % World.tilesPerChunk;
            int tileY = (coord.second / World.tileSize) % World.tilesPerChunk;
            Pixmap image = chunkPtr.getImage(tileX, tileY);

            //update the entities if there are any =============================================
            for(Entity ent: World.getEntByLocation(coord.first, coord.second)) {
                ents.put(ent, image);
            }
        }

        for(Entity ent: ents.keySet()) {
            Graphics.updateSprite(ent.spriteName, ents.get(ent));
        }
    }
}

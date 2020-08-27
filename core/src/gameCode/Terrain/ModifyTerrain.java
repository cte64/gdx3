package gameCode.Terrain;

import com.badlogic.gdx.graphics.Pixmap;
import gameCode.Infrastructure.Chunk;
import gameCode.Infrastructure.Entity;
import gameCode.Infrastructure.Graphics;
import gameCode.Infrastructure.World;
import gameCode.Utilities.myPair;

import java.util.ArrayList;
import java.util.HashMap;

public class ModifyTerrain {


    public static void addPixels(ArrayList<myPair<Integer, Integer>> pixels) {

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

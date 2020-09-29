package gameCode.Terrain;

import com.badlogic.gdx.graphics.Pixmap;
import com.mygdx.game.Engine;
import gameCode.Infrastructure.Chunk;
import gameCode.Infrastructure.Entity;
import gameCode.Infrastructure.myWorld;
import gameCode.Utilities.myPair;
import gameCode.Utilities.myString;

import java.util.ArrayList;
import java.util.HashMap;

public class ModifyTerrain {


    public static void addPixels(ArrayList<myPair<Integer, Integer>> pixels) {

        HashMap<Entity, Pixmap> ents = new HashMap<Entity, Pixmap>();
        for(myPair<Integer, Integer> coord: pixels) {

            //update the chunk map =============================================================
            myPair<Integer, Integer> key = Chunk.makeKeyFromPixel(coord.first, coord.second);
            Chunk chunkPtr = myWorld.get().getChunk(key);
            if(chunkPtr == null) continue;

            chunkPtr.setPixel(coord.first, coord.second, "coal");
            int tileX = (coord.first / myWorld.get().tileSize) % myWorld.get().tilesPerChunk;
            int tileY = (coord.second / myWorld.get().tileSize) % myWorld.get().tilesPerChunk;
            Pixmap image = chunkPtr.getImage(tileX, tileY);

            //update the entities if there are any =============================================
            for(Entity ent: myWorld.get().getEntByLocation(coord.first, coord.second)) {
                ents.put(ent, image);
            }
        }


        for(Entity ent: ents.keySet()) {
            if(!myString.getField(ent.entityName, "type").equals("tile")) continue;
            Engine.get().getAssets().updateSprite(ent.spriteName, ents.get(ent));
            Engine.get().getPhysics().resetGrid(ent);
        }
    }
}

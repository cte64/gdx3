package gameCode.Living;

import gameCode.Infrastructure.*;
import gameCode.Terrain.ModifyTerrain;
import gameCode.Utilities.MathUtils;
import gameCode.Utilities.Timer;
import gameCode.Utilities.myPair;

import java.util.ArrayList;

public class PlaceTerrain extends Component {


    Timer timer;

    public PlaceTerrain() {
        type = "logic";
        timer = new Timer();
        timer.addTimer("place");
    }

    public void update(Entity entity) {


        if(InputAL.isMousePressed("mouse left")) {
            timer.update(World.getDeltaTime());
            if(timer.getTime("place") > 0.6f) {
                timer.resetTimer("place");

                //delete a square
                int width = 20;
                myPair<Integer, Integer> val = InputAL.getMouseAbs();
                ArrayList<myPair<Integer, Integer>> pixels = new ArrayList<myPair<Integer, Integer>>();






                /*

                for(int y = -width; y < width; y++) {
                for(int x = -width; x < width; x++) {
                    float mag = MathUtils.mag(x, y, 0, 0);
                    if(mag < width) {
                        int xPos = val.first + x;
                        int yPos = val.second + y;
                        pixels.add(new myPair(xPos, yPos));
                    }
                }}

                ModifyTerrain.addPixels(pixels);

                 */
            }
        }

        else {
            timer.resetTimer("place");
        }

    }
}

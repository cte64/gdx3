package gameCode.Living;

import com.badlogic.gdx.math.MathUtils;
import com.mygdx.game.Engine;
import gameCode.Infrastructure.*;
import gameCode.Terrain.ModifyTerrain;
import gameCode.Utilities.Timer;
import gameCode.Utilities.myMath;
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


        if(Engine.get().getInput().isMousePressed("mouse left")) {
            timer.update(myWorld.get().getDeltaTime());
            if(timer.getTime("place") > 0.6f) {
                timer.resetTimer("place");

                //delete a square
                int width = 20;
                myPair<Integer, Integer> val = Engine.get().getInput().getMouseAbs();
                ArrayList<myPair<Integer, Integer>> pixels = new ArrayList<myPair<Integer, Integer>>();



                for(int y = -width; y < width; y++) {
                for(int x = -width; x < width; x++) {
                    float mag = myMath.mag(x, y, 0, 0);
                    if(mag < width) {
                        int xPos = val.first + x;
                        int yPos = val.second + y;
                        pixels.add(new myPair(xPos, yPos));
                    }
                }}

                ModifyTerrain.addPixels(pixels);


            }
        }

        else {
            timer.resetTimer("place");
        }

    }
}

package gameCode.Living;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector3;
import com.sun.org.apache.xpath.internal.operations.Mod;
import gameCode.Infastructure.*;
import gameCode.Terrain.ModifyTerrain;
import gameCode.Utilities.Timer;
import gameCode.Utilities.myPair;
import jdk.internal.net.http.common.Pair;

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
            if(timer.getTime("place") > 1.0f) {
                timer.resetTimer("place");


                //delete a square

                int width = 10;
                myPair<Integer, Integer> val = InputAL.getMouseAbs();
                ArrayList<myPair<Integer, Integer>> pixels = new ArrayList<myPair<Integer, Integer>>();


                for(int y = -width; y < width; y++) {
                for(int x = -width; x < width; x++) {
                    int xPos = val.first + x;
                    int yPos = val.second + y;
                    pixels.add(new myPair(xPos, yPos));
                }}

                ModifyTerrain.addPixels(pixels);
            }

            /*
             */



            //System.out.println("yee: " + val.first);
        }

        else {
            timer.resetTimer("place");
        }




        //ModifyTerrain.addPixels();


    }
}

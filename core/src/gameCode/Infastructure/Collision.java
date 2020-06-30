package gameCode.Infastructure;

import java.util.ArrayList;

public class Collision {

    private static int numMoveTicks = 1;

    private static void setTick() {



        /*
        ArrayList<Float> largest = new ArrayList<Float>();

        for(Entity ent: World.getEntList()) {
            if(ent.moveable) {
                largest.add(java.lang.Math.abs(ent.getXVelocity()));
                largest.add(java.lang.Math.abs(ent.getYVelocity()));
            }
        }

        if(largest.size() > 0) numMoveTicks = ceil(*std::max_element(std::begin(largest), std::end(largest))/world.maxFps);
        else numMoveTicks = 1;

        if(numMoveTicks < 1) numMoveTicks = 1;

         */
    }

    public static void update() {



        setTick();
        for(int t = 0; t<numMoveTicks; t++) {


            FileSystem.update();

            /*
            world.setEdge();
            world.cleanUp();
            world.loadEntities();
            world.update();

            if (world.currentState == "paused") continue;

            createVec();
            moveVec();
            changeAngle();
            checkCollision2();

             */
        }


    }


}

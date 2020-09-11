package gameCode.Infrastructure;

import com.mygdx.game.Engine;

public class Collision {

    private static int numMoveTicks = 1;

    private static void setTick() {



        /*
        ArrayList<Float> largest = new ArrayList<Float>();

        for(Entity ent: World.get().getEntList()) {
            if(ent.moveable) {
                largest.add(java.lang.Math.abs(ent.getXVelocity()));
                largest.add(java.lang.Math.abs(ent.getYVelocity()));
            }
        }

        if(largest.size() > 0) numMoveTicks = ceil(*std::max_element(std::begin(largest), std::end(largest))/World.get().maxFps);
        else numMoveTicks = 1;

        if(numMoveTicks < 1) numMoveTicks = 1;

         */
    }

    public static void update() {



        setTick();
        for(int t = 0; t<numMoveTicks; t++) {


            Engine.get().getFileSystem().update();
            //System.out.println("file thing");

            myWorld.get().setEdge();
            //System.out.println("edge");

            myWorld.get().cleanUp();
            //System.out.println("clean up");

            myWorld.get().loadEntities();
            //System.out.println("loaded up");

            myWorld.get().update();
            //System.out.println("updated");
            /*

            if (World.get().currentState == "paused") continue;

            createVec();
            moveVec();
            changeAngle();
            checkCollision2();

             */
        }


    }


}

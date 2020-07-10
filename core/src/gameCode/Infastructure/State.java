package gameCode.Infastructure;

import com.badlogic.gdx.graphics.Pixmap;
import gameCode.Living.HeroInput;
import gameCode.Terrain.MakeWorld;

public class State {

    private static void roundEarth() {}
    private static void loadGame() {}
    private static void paused() {}
    private static void play() {}
    private static void mainScreen() {}
    private static void newGame() {}
    private static void loading() {}


    private static void testGame() {



        //MakeWorld.testCube();


        //Set the viewport size and update the screen ===================
        Entity ent = new Entity();
        ent.x_pos = 100;
        ent.y_pos = 100;
        ent.spriteName = "tileId: 0.0";
        ent.entityName = "hero";
        ent.addComponent(new HeroInput());
        World.entitiesToBeAdded.add(ent);
        World.setCamera(ent);
        World.addSiftingFrame(ent, 0, 0);


        Pixmap pix = new Pixmap(60, 60, Pixmap.Format.RGB888);
        for(int j = 0; j < 60; j++) {
            for(int i = 0; i < 60; i++) {

                if(i == j)
                    pix.drawPixel(j, i, 16711935);
                else
                    pix.drawPixel(j, i, 11711935);
            }
        }




        Graphics.updateSprite("tileId: 0.0", pix);
    }

    private State() {}
    public void deleteMenuItems() {}
    private static String state;
    public static String getState() { return state; }
    public static void setState(String newState) { state = newState; }

    public static void loadState() {
        if(World.getCurrentState() == "testGame") testGame();
    }
}

package gameCode.Infrastructure;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector3;
import gameCode.Utilities.MathUtils;
import gameCode.Utilities.myPair;

import java.util.ArrayList;
import java.util.HashMap;

public class InputAL {

    private static HashMap<String, Integer> keys;
    public static ArrayList<Character>  charsQueue = new ArrayList<Character>();
    public static ArrayList<Integer> scrollQueue = new ArrayList<Integer>();

    public static void init() {

        keys = new HashMap<String, Integer>();

        //Add all the alphabetical keys here =============
        keys.put("w", Input.Keys.W);
        keys.put("i", Input.Keys.I);

        //Add all the numerical keys here ================

        //Add all the special keys here ==================
        keys.put("left", Input.Keys.LEFT);
        keys.put("right", Input.Keys.RIGHT);
        keys.put("up", Input.Keys.UP);
        keys.put("down", Input.Keys.DOWN);
        keys.put("backspace", Input.Keys.BACKSPACE);
        keys.put("esc", Input.Keys.ESCAPE);

        //Add all the mouse buttons here =================
        keys.put("mouse left", Input.Buttons.LEFT);
        keys.put("mouse right", Input.Buttons.RIGHT);
    }

    public static int getMouseX() {
        int mouseX = Gdx.input.getX();
        mouseX = MathUtils.clamp(mouseX, 0, World.getViewPortWidth() - 1);
        return mouseX;
    }

    public static int getMouseY() {
        int mouseY = Gdx.input.getY();
        mouseY = World.getViewPortHeight() - mouseY;
        mouseY = MathUtils.clamp(mouseY, 0, World.getViewPortHeight() - 1);
        return mouseY;
    }

    public static myPair<Integer, Integer> getMouseAbs() {
        Vector3 coord = Graphics.getMouse();
        int x = (int)coord.x;
        int y = (int)coord.y;
        myPair<Integer, Integer> retVal = new myPair(x, y);
        return retVal;
    }

    public static boolean isKeyPressed(String val) {
        if(!keys.containsKey(val)) return false;
        if(Gdx.input.isKeyPressed(keys.get(val)) ) return true;
        else return false;
    }

    public static boolean isMousePressed(String val) {
        if(!keys.containsKey(val)) return false;
        if(Gdx.input.isButtonPressed(keys.get(val)) ) return true;
        else return false;
    }

    public static void reset() {
        charsQueue.clear();
        scrollQueue.clear();
    }
}

package gameCode.Infastructure;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import gameCode.Utilities.MathUtils;

import java.util.HashMap;

public class InputAL {

    private static HashMap<String, Integer> keys;

    public static void init() {

        keys = new HashMap<String, Integer>();


        //Add all the alphabetical keys here =============
        keys.put("w", Input.Keys.W);

        //Add all the numerical keys here ================

        //Add all the special keys here ==================
        keys.put("left", Input.Keys.LEFT);
        keys.put("right", Input.Keys.RIGHT);
        keys.put("up", Input.Keys.UP);
        keys.put("down", Input.Keys.DOWN);

        //Add all the mouse buttons here =================
        keys.put("mouse left", Input.Buttons.LEFT);
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

    public static boolean isKeyPressed(String val) {
        if(!keys.containsKey(val)) return false;
        if(Gdx.input.isKeyPressed(keys.get(val)) ) return true;
        else return false;
    }

}

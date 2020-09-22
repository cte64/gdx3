package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector3;
import gameCode.Infrastructure.myWorld;
import gameCode.Utilities.myMath;
import gameCode.Utilities.myPair;

import java.util.ArrayList;
import java.util.HashMap;

public class InputAL {

    private HashMap<String, Integer> keys;
    public ArrayList<Character>  charsQueue = new ArrayList<Character>();
    public ArrayList<Integer> scrollQueue = new ArrayList<Integer>();

    public void init() {

        //set the input processor ======================
        InputProcessor ip = new InputProcessor() {
            @Override
            public boolean keyDown(int keycode) {
                return false;
            }

            @Override
            public boolean keyUp(int keycode) {
                return false;
            }

            @Override
            public boolean keyTyped(char character) {
                Engine.get().getInput().charsQueue.add(character);
                return false;
            }

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                return false;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                return false;
            }

            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                return false;
            }

            @Override
            public boolean mouseMoved(int screenX, int screenY) {
                return false;
            }

            @Override
            public boolean scrolled(int amount) {
                Engine.get().getInput().scrollQueue.add(amount);
                return false;
            }
        };
        Gdx.input.setInputProcessor(ip);


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
        keys.put("plus", Input.Keys.PLUS);
        keys.put("minus", Input.Keys.MINUS);

        //Add all the mouse buttons here =================
        keys.put("mouse left", Input.Buttons.LEFT);
        keys.put("mouse right", Input.Buttons.RIGHT);
    }

    public int getMouseX() {
        int mouseX = Gdx.input.getX();
        mouseX = myMath.clamp(mouseX, 0, Engine.get().getGraphics().getVPWidth() - 1);
        return mouseX;
    }

    public int getMouseY() {
        int mouseY = Gdx.input.getY();
        mouseY = Engine.get().getGraphics().getVPHeight() - mouseY;
        mouseY = myMath.clamp(mouseY, 0, Engine.get().getGraphics().getVPHeight() - 1);
        return mouseY;
    }

    public myPair<Integer, Integer> getMouseAbs() {
        Vector3 coord = Engine.get().getGraphics().getMouse();
        int x = (int)coord.x;
        int y = (int)coord.y;
        myPair<Integer, Integer> retVal = new myPair(x, y);
        return retVal;
    }

    public boolean isKeyPressed(String val) {
        if(!keys.containsKey(val)) return false;
        if(Gdx.input.isKeyPressed(keys.get(val)) ) return true;
        else return false;
    }

    public boolean isMousePressed(String val) {
        if(!keys.containsKey(val)) return false;
        if(Gdx.input.isButtonPressed(keys.get(val)) ) return true;
        else return false;
    }

    public void reset() {
        charsQueue.clear();
        scrollQueue.clear();
    }

    public InputAL() {
        init();
    }
}

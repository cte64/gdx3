package gameCode.Tools;

import gameCode.Infrastructure.Component;
import gameCode.Infrastructure.Entity;
import gameCode.Infrastructure.myWorld;
import gameCode.Menus.Inventory.AddEntity;
import gameCode.Utilities.Timer;
import gameCode.Utilities.myMath;

import java.util.ArrayList;

public class AnimationSequence {

    private class Frame {
        int x, y;
        float angle, time;
    }

    private ArrayList<Frame> frames;
    private int currentStep;
    private Timer timer;
    private boolean mirrorX;

    public AnimationSequence() {
        frames = new ArrayList<>();
        timer = new Timer();
        timer.addTimer("timer");
        currentStep = 0;
        mirrorX = false;
    }

    public void setMirrorX(boolean newX) { mirrorX = newX; }

    public void addFrame(int x, int y, float angle, float time) {
        Frame newFrame = new Frame();
        newFrame.x = x;
        newFrame.y = y;
        newFrame.angle = angle;
        newFrame.time = time;
        frames.add(newFrame);
    }

    public int getIndex(boolean flip) {
        int retVal = currentStep;
        if(flip) retVal = frames.size() - 1 - currentStep;
        return retVal;
    }

    public void update() {
        timer.update("timer", myWorld.get().getDeltaTime());
        if(timer.getTime("timer") > frames.get(currentStep).time) {
            currentStep++;
            timer.resetTimer("timer");
            if(currentStep >= frames.size()) currentStep = 0;
        }
    }

    public int getX() {
        int retVal = frames.get(currentStep).x;
       // if(mirrorX) retVal *= -1;
        return retVal;
    }

    public int getY() {
        return frames.get(currentStep).y;
    }

    public float getAngle() {
        float retVal = frames.get(currentStep).angle;
        if(mirrorX) retVal = 180 - retVal;
        return retVal;
    }

    public void reset(boolean flip) {
        timer.resetTimer("timer");
        if(flip) currentStep = frames.size() - 1;
        else currentStep = 0;
    }
}

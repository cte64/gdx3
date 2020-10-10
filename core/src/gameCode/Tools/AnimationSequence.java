package gameCode.Tools;

import gameCode.Infrastructure.Component;
import gameCode.Infrastructure.Entity;
import gameCode.Infrastructure.myWorld;
import gameCode.Menus.Inventory.AddEntity;
import gameCode.Utilities.Timer;

import java.util.ArrayList;

public class AnimationSequence {

    private class Frame {
        int x, y;
        float angle, time;
    }

    ArrayList<Frame> frames;
    int currentStep;
    Timer timer;

    public AnimationSequence() {
        frames = new ArrayList<>();
        timer = new Timer();
        timer.addTimer("timer");
        currentStep = 0;
    }

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

    public int getX(boolean flip) {
        return frames.get(getIndex(flip)).x;
    }

    public int getY(boolean flip) {
        return frames.get(getIndex(flip)).y;
    }

    public float getAngle(boolean flip) {
        return frames.get(getIndex(flip)).angle;
    }

    public void reset(boolean flip) {
        timer.resetTimer("timer");
        if(flip) currentStep = frames.size() - 1;
        else currentStep = 0;
    }
}

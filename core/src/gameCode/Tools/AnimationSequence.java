package gameCode.Tools;

import gameCode.Infrastructure.Component;
import gameCode.Infrastructure.Entity;
import gameCode.Infrastructure.myWorld;
import gameCode.Menus.Inventory.AddEntity;
import gameCode.Utilities.Coordinates;
import gameCode.Utilities.Timer;
import gameCode.Utilities.myMath;
import gameCode.Utilities.myPair;

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
    private float angle;
    private float offsetAngle;


    public AnimationSequence() {
        frames = new ArrayList<>();
        timer = new Timer();
        timer.addTimer("timer");
        currentStep = 0;
        mirrorX = false;
        angle = 0.0f;
    }

    public void setOffsetAngle(float newAngle) { offsetAngle = newAngle; }

    public void addFrame(int x, int y, float angle, float time) {
        Frame newFrame = new Frame();
        newFrame.x = x;
        newFrame.y = y;
        newFrame.angle = angle;
        newFrame.time = time;
        frames.add(newFrame);
    }

    public void update(Entity parent, Entity child) {

        if(parent == null || child == null) return;
        this.angle = parent.angle;

        if(parent.flipX) {
            mirrorX = true;
            child.flipX = true;
        }
        else {
            mirrorX = false;
            child.flipX = false;
        }

        timer.update("timer", myWorld.get().getDeltaTime());
        if(timer.getTime("timer") > frames.get(currentStep).time) {
            currentStep++;
            timer.resetTimer("timer");
            if(currentStep >= frames.size()) currentStep = 0;
        }

        child.angle = parent.angle + myMath.toRad( getAngle() );
        child.x_pos = parent.x_pos + parent.origin.first - child.origin.first + getX();
        child.y_pos = parent.y_pos + parent.origin.second - child.origin.second + getY();
    }

    public float getX() {
        float x = frames.get(currentStep).x;
        if(mirrorX) x = -x;
        myPair<Float, Float> coord = Coordinates.rotatePoint3(x, frames.get(currentStep).y, 0, 0, angle);
        return coord.first;
    }

    public float getY() {
        float x = frames.get(currentStep).x;
        if(mirrorX) x = -x;
        myPair<Float, Float> coord = Coordinates.rotatePoint3(x, frames.get(currentStep).y, 0, 0, angle);
        return coord.second;
    }

    public float getAngle() {

        float newOffset = offsetAngle;
        float retVal = frames.get(currentStep).angle;
        if(mirrorX) {
            retVal = -retVal;
            newOffset = -offsetAngle;
        }
        return newOffset + retVal;
    }

    public void reset(boolean flip) {
        timer.resetTimer("timer");
        currentStep = 0;
    }

    public int getIndex() { return currentStep; };
}

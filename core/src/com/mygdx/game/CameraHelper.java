package com.mygdx.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import gameCode.Infrastructure.Entity;
import gameCode.Utilities.myMath;

public class CameraHelper {
    private static final String TAG = CameraHelper.class.getName();
    private final float FOLLOW_SPEED = 4.0f;
    private final float MAX_ZOOM_IN = 0.25f;
    private final float MAX_ZOOM_OUT = 7.0f;
    private Vector2 position;
    private float zoom;
    private Entity target;
    float angleBefore, amount;

    public CameraHelper() {
        position = new Vector2();
        zoom = 1.0f;
        angleBefore = 0.0f;
        amount = 0.0f;
    }

    public void update() {
        if (!hasTarget()) return;

        position.x = target.x_pos + target.origin.first;
        position.y = target.y_pos + target.origin.second;

        if(angleBefore != target.angle) {
            amount = target.angle - angleBefore;
            angleBefore = target.angle;
        }
        else {
            amount = 0;
        }
    }

    public void setPosition(float x, float y) {
        this.position.set(x, y);
    }

    public Vector2 getPosition() {
        return position;
    }

    public void addZoom(float amount) {
        setZoom(zoom + amount);
    }

    public float getZoom() {
        return zoom;
    }

    public void setZoom(float zoom) {
        this.zoom = MathUtils.clamp(zoom, MAX_ZOOM_IN, MAX_ZOOM_OUT);
    }

    public Entity getTarget() {
        return target;
    }

    public void setTarget(Entity target) {
        this.target = target;
    }

    public boolean hasTarget() {
        return target != null;
    }

    public boolean hasTarget(Entity target) {
        return hasTarget() && this.target.equals(target);
    }

    public void applyTo(OrthographicCamera camera) {
        camera.position.x = position.x;
        camera.position.y = position.y;


        camera.rotate(-myMath.toDeg(amount));
        camera.zoom = zoom;
        camera.update();
    }
}
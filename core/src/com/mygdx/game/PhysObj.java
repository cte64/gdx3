package com.mygdx.game;

import box2dLight.PointLight;
import com.badlogic.gdx.physics.box2d.Body;

import java.util.ArrayList;

public class PhysObj {

    public boolean setGrid;
    public boolean gridActive;

    public ArrayList<Body> bodies;
    public ArrayList<PointLight> lights;

    public PhysObj() {
        bodies = new ArrayList<Body>();
        lights = new ArrayList<PointLight>();
        setGrid = false;
        gridActive = false;
    }
}

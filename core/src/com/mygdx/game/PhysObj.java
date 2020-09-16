package com.mygdx.game;

import com.badlogic.gdx.physics.box2d.Body;

import java.util.ArrayList;

public class PhysObj {

    public boolean setGrid;
    public boolean gridActive;

    public ArrayList<Body> bodies;

    public PhysObj() {
        bodies = new ArrayList<Body>();
        setGrid = false;
        gridActive = false;
    }
}

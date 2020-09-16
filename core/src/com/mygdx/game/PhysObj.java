package com.mygdx.game;

import com.badlogic.gdx.physics.box2d.Body;

import java.util.ArrayList;

public class PhysObj {


    private class bodyStruct {
        boolean tile;
    }
    public ArrayList<Body> bodies;

    public PhysObj() {
        bodies = new ArrayList<Body>();
    }
}

package com.mygdx.game;

import com.badlogic.gdx.physics.box2d.*;
import com.sun.tools.javac.util.StringUtils;
import gameCode.Infrastructure.Entity;
import gameCode.Utilities.myString;

public class Collision1 implements ContactListener {

    @Override
    public void beginContact(Contact contact) {



        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        if(fa == null || fb == null) return;
        if(fa.getUserData() == null || fb.getUserData() == null) return;

        if(isTutorialContact(fa, fb)) {

            System.out.println("Begin:");
            Entity ent = (Entity)fb.getUserData();


            if(myString.getField(ent.entityName, "type").equals("tile")) {
                Engine.get().getPhysics().toBeAdded.put(ent, ent);
            }
        }
    }

    @Override
    public void endContact(Contact contact) {


        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        if(fa == null || fb == null) return;
        if(fa.getUserData() == null || fb.getUserData() == null) return;

        if(isTutorialContact(fa, fb)) {

            System.out.println("End:");
            Entity ent = (Entity)fb.getUserData();
            if(myString.getField(ent.entityName, "type").equals("tile")) {
                Engine.get().getPhysics().toBeDeleted.put(ent, ent);
            }
        }
        /*

         */

        /*
         */
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
    }

    private boolean isTutorialContact(Fixture a, Fixture b) {
        return (a.getUserData() instanceof Entity && b.getUserData() instanceof Entity);
    }
}

package com.mygdx.game;

import com.badlogic.gdx.physics.box2d.*;
import com.sun.tools.javac.util.StringUtils;
import gameCode.Infrastructure.Entity;
import gameCode.Utilities.myString;

public class Collision1 implements ContactListener {


    static int counter = 0;

    @Override
    public void beginContact(Contact contact) {

        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();


        if(fa == null || fb == null) return;
        if(fa.getUserData() == null || fb.getUserData() == null) return;

        if(isTutorialContact(fa, fb)) {
            Entity ent = (Entity)fb.getUserData();
            if(fb.getFilterData().categoryBits == 1 &&
            myString.getField(ent.entityName, "type").equals("tile")) {
                Engine.get().getPhysics().setGridFlag(ent, true);
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
            Entity ent = (Entity)fb.getUserData();
            if(fb.getFilterData().categoryBits == 1 &&
            myString.getField(ent.entityName, "type").equals("tile")) {
                Engine.get().getPhysics().setGridFlag(ent, false);
            }
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {


        /*
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        if(fa == null || fb == null) return;
        if(fa.getUserData() == null || fb.getUserData() == null) return;

        if(isTutorialContact(fa, fb)) {
            Entity ent = (Entity)fb.getUserData();
            if(ent.entityName.equals("hero")) {
                System.out.println(ent.entityName);
            }
        }

         */






    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {



        if(!contact.isTouching()) {
            System.out.println("for some reason");
        }

    }

    private boolean isTutorialContact(Fixture a, Fixture b) {
        return (a.getUserData() instanceof Entity && b.getUserData() instanceof Entity);
    }
}

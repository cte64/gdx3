package com.mygdx.game;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import gameCode.Infrastructure.Entity;
import gameCode.Infrastructure.myWorld;

import java.util.ArrayList;

public class Physics {

    private World b2world;

    public Physics() {
        if (b2world != null) b2world.dispose();
        b2world = new World(new Vector2(0, 0), true);
        b2world.setContactListener(new Collision1());
    }


    /*

    public void addBody(Entity ent, int x, int y, float w, float h, String type, boolean active) {
        if(ent == null) return;
        w /= 2.0f;
        h /= 2.0f;

        BodyDef bodyDef = new BodyDef();
        if(type.equals("static")) bodyDef.type = BodyDef.BodyType.StaticBody;
        else if(type.equals("dynamic")) bodyDef.type = BodyDef.BodyType.DynamicBody;

        bodyDef.position.set(ent.x_pos + x, ent.y_pos + y);
        Body body = b2world.createBody(bodyDef);

        PolygonShape polygonShape = new PolygonShape();
        Vector2 origin = new Vector2();
        origin.x = w;//w / 2.0f;
        origin.y = h;//h / 2.0f;
        polygonShape.setAsBox(w, h, origin, 0);



        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = polygonShape;
        fixtureDef.density = 1.0f;
        fixtureDef.friction = 0.3f;

        body.createFixture(fixtureDef).setUserData(ent);

        if(!active) {
            for(Fixture f: body.getFixtureList()) {
                f.setSensor(true);
            }
        }


        polygonShape.dispose();
        ent.bodies.add(body);
    }

    public void addGrid(Entity ent) {
        int width = 10;
        for(int y = 0; y < myWorld.get().tileSize; y += width) {
        for(int x = 0; x < myWorld.get().tileSize; x += width) {
            addBody(ent, x, y, width, width, "static", false);
        }}
    }

    public void subtractGrid(Entity ent) {


        for(int x = 0; x < ent.bodies.size(); x++) {
            b2world.destroyBody(ent.bodies.get(x));
        }

        addBody(ent, 0, 0, 60, 60, "static", false);
    }

    public void subtractBody(Entity ent) {
        if(ent == null) return;
        for(Body body: ent.bodies) {
            b2world.destroyBody(body);
        }
      //  b2world.destroyBody(ent.body);
    }

    private void setRotation() {
        for(Entity ent: myWorld.get().getEntByZIndex()) {

        }
    }

    public void update() {

        b2world.step(myWorld.get().getDeltaTime(), 100, 3);
    }

    public World getb2World() { return b2world; }

     */
}

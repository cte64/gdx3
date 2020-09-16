package com.mygdx.game;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import gameCode.Infrastructure.Entity;
import gameCode.Infrastructure.myWorld;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Physics {

    private World b2world;
    HashMap<Entity, PhysObj> entities;
    //public ArrayList<Entity> toBeAdded;
    public Map<Entity, Entity> toBeAdded;
    //public ArrayList<Entity> toBeDeleted;
    public Map<Entity, Entity> toBeDeleted;

    public void addEntity(Entity ent) {
        entities.put(ent, new PhysObj());
    }

    public Physics() {
        if (b2world != null) b2world.dispose();
        b2world = new World(new Vector2(0, 0), true);
        b2world.setContactListener(new Collision1());
        entities = new HashMap<Entity, PhysObj>();
        //toBeAdded = new ArrayList<Entity>();
        toBeAdded = new ConcurrentHashMap<>();
        //toBeDeleted = new ArrayList<Entity>();
        toBeDeleted = new ConcurrentHashMap<>();
    }

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
        origin.x = w;
        origin.y = h;
        polygonShape.setAsBox(w, h, origin, 0);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = polygonShape;
        fixtureDef.density = 1.0f;
        fixtureDef.friction = 0.3f;
        body.createFixture(fixtureDef).setUserData(ent);

        if(!active) {
            for(Fixture f: body.getFixtureList()) { f.setSensor(true); }
        }
        polygonShape.dispose();

        if(!entities.containsKey(ent)) addEntity(ent);
        entities.get(ent).bodies.add(body);
    }

    public void addGrid(Entity ent) {
        int width = 20;
        for(int y = 0; y < myWorld.get().tileSize; y += width) {
        for(int x = 0; x < myWorld.get().tileSize; x += width) {
            addBody(ent, x, y, width, width, "static", true);
        }}

        System.out.println( entities.get(ent).bodies.size());
    }

    public void subtractGrid(Entity ent) {
        if(!entities.containsKey(ent)) return;
        for(int x = 0; x < entities.get(ent).bodies.size(); x++) {
            b2world.destroyBody( entities.get(ent).bodies.get(x) );
        }
        entities.get(ent).bodies.clear();
        addBody(ent, 0, 0, myWorld.get().tileSize, myWorld.get().tileSize, "static", false);
    }

    public void update() {

        for(Entity ent: myWorld.get().getEntList()) {
            if(!entities.containsKey(ent)) continue;

            for(Body body: entities.get(ent).bodies) {
                body.setLinearVelocity(ent.getXVelocity(), ent.getYVelocity());
                body.setAngularVelocity(ent.angle);

                if(ent.entityName.equals("hero")) {
                    ent.x_pos = body.getPosition().x;
                    ent.y_pos = body.getPosition().y;
                }
            }
        }

        b2world.step(myWorld.get().getDeltaTime(), 8, 3);




    }

    public World getb2World() { return b2world; }

    public void funny() {

        for(Entity ent: toBeDeleted.values()) {
            if(ent.stuff) {
                subtractGrid(ent);
                ent.stuff = false;
            }
        }
        toBeDeleted.clear();

        for(Entity ent: toBeAdded.values()) {
            if(!ent.stuff) {
                addGrid(ent);
                ent.stuff = true;
            }
        }
        toBeAdded.clear();

    }


}

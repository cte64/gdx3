package com.mygdx.game;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import gameCode.Infrastructure.Chunk;
import gameCode.Infrastructure.Entity;
import gameCode.Infrastructure.myWorld;
import gameCode.Utilities.myPair;
import gameCode.Utilities.myString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Physics {

    public World b2world = null;
    HashMap<Entity, PhysObj> entities;

    public RayHandler rayHandler;


    public Physics() {
        if (b2world != null) b2world.dispose();
        b2world = new World(new Vector2(0, 0), true);
        b2world.setContactListener(new Collision1());
        entities = new HashMap<Entity, PhysObj>();

        //rayHandler = new RayHandler(b2world);
        //rayHandler.setAmbientLight(0.4f);
    }

    ArrayList<Entity> reSpring = new ArrayList<Entity>();

    private void addEntity(Entity ent) {
        entities.put(ent, new PhysObj());
    }

    public boolean pollFixture(Entity ent) {
        if (!entities.containsKey(ent)) return false;

        for(Body body: entities.get(ent).bodies) {

            for(Contact con: body.getWorld().getContactList()) {

                Fixture fa = con.getFixtureA();
                Fixture fb = con.getFixtureB();

                if(fa == null || fb == null) return false;
                if(fa.getUserData() == null || fb.getUserData() == null) return false;

                if(fa.getUserData() instanceof Entity && fb.getUserData() instanceof Entity) {
                    Entity entity = (Entity)fb.getUserData();
                    if(fb.getFilterData().categoryBits == 2 &&
                    myString.getField(entity.entityName, "type").equals("tile") &&
                    con.isTouching() ) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public void addBody2(Entity ent, int x, int y, float w, float h, String type, boolean active, int filter) {

        if(ent == null) return;

        BodyDef bodyDef = new BodyDef();
        if(type.equals("static")) bodyDef.type = BodyDef.BodyType.StaticBody;
        else if(type.equals("dynamic")) bodyDef.type = BodyDef.BodyType.DynamicBody;

        bodyDef.position.set(w/2 + ent.x_pos + x, h/2 + ent.y_pos + y);
        Body body = b2world.createBody(bodyDef);

        PolygonShape polygonShape = new PolygonShape();
        Vector2 origin = new Vector2();
        origin.x = 0;
        origin.y = 0;

        int stepX = 9;
        int stepY = 10;

        Vector2[] vert = new Vector2[6];
        vert[0] = new Vector2(w/2, h/2);
        vert[1] = new Vector2(-w/2, h/2);
        vert[2] = new Vector2(-w/2, -h/2 + stepY);
        vert[3] = new Vector2(-w/2 + stepX, -h/2);
        vert[4] = new Vector2(w/2 - stepX, -h/2);
        vert[5] = new Vector2(w/2, -h/2 + stepY);
        polygonShape.set(vert);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = polygonShape;
        fixtureDef.filter.categoryBits = (short)filter;
        body.createFixture(fixtureDef).setUserData(ent);
        if(!active) { for(Fixture f: body.getFixtureList()) { f.setSensor(true); } }
        polygonShape.dispose();

        if(!entities.containsKey(ent)) addEntity(ent);
        entities.get(ent).bodies.add(body);




        /*
        PointLight light = new PointLight(rayHandler, 100, Color.WHITE, 1000, 0, 0);
        light.attachToBody( body );
        entities.get(ent).lights.add(light);

         */


    }

    public void addBody(Entity ent, int x, int y, float w, float h, String type, boolean active, int filter) {

        if(ent == null) return;

        BodyDef bodyDef = new BodyDef();
        if(type.equals("static")) bodyDef.type = BodyDef.BodyType.StaticBody;
        else if(type.equals("dynamic")) bodyDef.type = BodyDef.BodyType.DynamicBody;

        bodyDef.position.set(w/2 + ent.x_pos + x, h/2 + ent.y_pos + y);
        Body body = b2world.createBody(bodyDef);

        PolygonShape polygonShape = new PolygonShape();
        Vector2 origin = new Vector2();
        origin.x = 0;
        origin.y = 0;

        polygonShape.setAsBox(w/2, h/2, origin, 0);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = polygonShape;
        fixtureDef.friction = 0.3f;
        fixtureDef.filter.categoryBits = (short)filter;
        body.createFixture(fixtureDef).setUserData(ent);
        if(!active) { for(Fixture f: body.getFixtureList()) { f.setSensor(true); } }
        polygonShape.dispose();

        if(!entities.containsKey(ent)) addEntity(ent);
        entities.get(ent).bodies.add(body);
    }

    private void addGrid(Entity ent) {
        int width = 3;
        for(int y = 0; y < myWorld.get().tileSize; y += width) {
        for(int x = 0; x < myWorld.get().tileSize; x += width) {

            myPair<Integer, Integer> key = Chunk.makeKeyFromPixel((int)ent.x_pos, (int)ent.y_pos);
            int xTile = (int)((ent.x_pos / myWorld.get().tileSize) % myWorld.get().tilesPerChunk);
            int yTile = (int)((ent.y_pos / myWorld.get().tileSize) % myWorld.get().tilesPerChunk);

            if(!myWorld.get().getChunk(key).isRegionEmpty(xTile, yTile, x, y, width, width)) {
                addBody(ent, x, y, width, width, "static", true, 2);
            }
        }}
    }

    private void subtractGrid(Entity ent) {
        if(!entities.containsKey(ent)) return;
        for(int x = 0; x < entities.get(ent).bodies.size(); x++) {
            b2world.destroyBody( entities.get(ent).bodies.get(x) );
        }
        entities.get(ent).bodies.clear();
        addBody(ent, -1, -1, myWorld.get().tileSize + 2, myWorld.get().tileSize + 2, "static", false, 1);
    }

    public void update() {

        for(Entity ent: myWorld.get().getEntList()) {
            if(!entities.containsKey(ent)) continue;
            for(Body body: entities.get(ent).bodies) {
                if(body.getType().equals(BodyDef.BodyType.StaticBody)) continue;
                body.setLinearVelocity(body.getLinearVelocity().x + ent.getXVelocity(), body.getLinearVelocity().y + ent.getYVelocity());

                ent.accelerate(-1.0f, 0);

                ent.x_pos = body.getPosition().x - ent.getWidth()/2;
                ent.y_pos = body.getPosition().y - ent.getHeight()/2;
                body.setTransform(body.getPosition(), ent.angle);

            }
        }
        for(Entity ent: entities.keySet()) {
            PhysObj obj = entities.get(ent);
            if(obj.setGrid == obj.gridActive) continue;
            if(obj.setGrid) {
                obj.gridActive = true;
                addGrid(ent);
            }
            else {
                obj.gridActive = false;
                subtractGrid(ent);
            }
        }


        for(Entity ent: reSpring) {
            //subtractGrid(ent);
            //addGrid(ent);
            PhysObj obj = entities.get(ent);

            subtractGrid(ent);
            addGrid(ent);
            obj.gridActive = true;
            obj.setGrid = true;
        }

        reSpring.clear();

        b2world.step(myWorld.get().getDeltaTime(), 8, 3);
    }

    public World getb2World() {
        return b2world;
    }

    public void setGridFlag(Entity ent, boolean flag) {
        if(!entities.containsKey(ent)) return;
        if(!entities.get(ent).setGrid && flag) entities.get(ent).setGrid = true;
        else if(entities.get(ent).setGrid && !flag) entities.get(ent).setGrid = false;
    }

    public void resetGrid(Entity ent) {
        if(!entities.containsKey(ent)) return;
        if(entities.get(ent).setGrid) {
            subtractGrid(ent);
        }
    }

    public void deleteEnt(Entity ent) {
        if(!entities.containsKey(ent)) return;

        for(Body body: entities.get(ent).bodies) {
            b2world.destroyBody(body);
        }

        entities.remove(ent);
    }

}


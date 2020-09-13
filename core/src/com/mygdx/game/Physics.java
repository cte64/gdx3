package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import gameCode.Infrastructure.Entity;
import gameCode.Infrastructure.myWorld;

public class Physics {

    private World b2world;


    public Physics() {
        if (b2world != null) b2world.dispose();
        b2world = new World(new Vector2(0, 0), true);
    }

    public void addBody(Entity ent) {

        BodyDef bodyDef = new BodyDef();

        if(ent.entityName.equals("hero"))
            bodyDef.type = BodyDef.BodyType.DynamicBody;
        else {
            bodyDef.type = BodyDef.BodyType.StaticBody;

        }

        bodyDef.position.set(ent.x_pos, ent.y_pos);


        Body body = b2world.createBody(bodyDef);


        ent.body = body;

        PolygonShape polygonShape = new PolygonShape();

        Vector2 origin = new Vector2();
        origin.x = ent.getWidth() / 2.0f;
        origin.y = ent.getHeight() / 2.0f;
        polygonShape.setAsBox(ent.getWidth() / 2.0f, ent.getHeight() / 2.0f, origin, 0);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = polygonShape;
        fixtureDef.density = 1.0f;
        fixtureDef.friction = 0.3f;

        body.createFixture(fixtureDef);
        polygonShape.dispose();
    }

    public void subtractBody(Entity ent) {
        if(ent == null || ent.body == null) return;
        b2world.destroyBody(ent.body);
    }


    private void setRotation() {
        for(Entity ent: myWorld.get().getEntByZIndex()) {

        }
    }

    public void update() {

        b2world.step(myWorld.get().getDeltaTime(), 100, 3);
    }

    public World getb2World() { return b2world; }


}

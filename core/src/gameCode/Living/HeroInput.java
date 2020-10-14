package gameCode.Living;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
import com.mygdx.game.Engine;
import gameCode.Factory.EntityFactory;
import gameCode.Infrastructure.*;
import gameCode.Tools.Animation;
import gameCode.Utilities.Timer;
import gameCode.Utilities.myMath;
import gameCode.Utilities.myPair;

import java.util.ArrayList;

public class HeroInput extends Component implements Animation {


    float deltaX;
    float wkSpeed;
    int walkFrame;
    int legFrame, armFrame, bodyFrame, headFrame;


    private final int frameStart = 6;
    private final int frameEnd = 19;
    private final int frameDefault = 19;
    ArrayList<myPair<String, Integer>> events;

    Timer timer;
    Entity head;
    Entity hair;
    Entity legs;
    Entity body;
    Entity hands;

    public HeroInput() {

        type = "input";
        timer = new Timer();
        timer.addTimer("walk");
        deltaX = 10.0f;
        wkSpeed = 0.05f;
        walkFrame = 0;
        events = new ArrayList<>();

        headFrame = frameStart;
        legFrame = frameStart;
        armFrame = frameStart;
        bodyFrame = frameStart;

        head = EntityFactory.createEntity("[type: actor][subType: bodyPart][id: head]");
        head.spriteName = "Player_Head";
        myWorld.get().entitiesToBeAdded.add(head);

        hair = EntityFactory.createEntity("[type: actor][subType: bodyPart][id: hair]");
        hair.spriteName = "Player_Hair";
        hair.z_pos = head.z_pos + 1;
        myWorld.get().entitiesToBeAdded.add(hair);

        body = EntityFactory.createEntity("[type: actor][subType: bodyPart][id: body]");
        body.spriteName = "Skin_Body";
        body.z_pos = head.z_pos;
        myWorld.get().entitiesToBeAdded.add(body);

        legs = EntityFactory.createEntity("[type: actor][subType: bodyPart][id: legs]");
        legs.spriteName = "Skin_Legs";
        legs.z_pos = head.z_pos;
        myWorld.get().entitiesToBeAdded.add(legs);

        hands = EntityFactory.createEntity("[type: actor][subType: bodyPart][id: hands]");
        hands.spriteName = "Player_Hands";
        hands.z_pos = 22;
        myWorld.get().entitiesToBeAdded.add(hands);
    }

    private void updateControls(Entity entity) {

        float center = myWorld.get().getNumPixels() / 2.0f;
        float angle = myMath.angleBetweenCells(center, center, entity.x_pos, entity.y_pos);

        entity.angle = -myMath.toRad(angle - 90.0f );
        entity.accelerate(deltaX/3.0f, 270.0f);

        boolean grounded = Engine.get().getPhysics().pollFixture(entity);
        float walkSpeed = deltaX;
        if(!grounded) walkSpeed /= 2.0f;


        boolean dirBefore = entity.flipX;
        boolean isWalk = false;

        if(Engine.get().getInput().isKeyPressed("a") && !Engine.get().getInput().isKeyPressed("d")) {
            entity.flipX = true;
            entity.accelerate(deltaX, 180.0f);
            isWalk = true;
        }

        if(Engine.get().getInput().isKeyPressed("d") && !Engine.get().getInput().isKeyPressed("a")) {
            entity.flipX = false;
            entity.accelerate(deltaX, 0.0f);
            isWalk = true;
        }

        //if( Engine.get().getInput().isKeyPressed("w")) entity.accelerate(deltaX, 90.0f);
        //if( Engine.get().getInput().isKeyPressed("s")) entity.accelerate(deltaX, 270.0f);

        if(dirBefore == entity.flipX && isWalk) {
             timer.update("walk", myWorld.get().getDeltaTime());
             if(timer.getTime("walk") > wkSpeed) {
                 timer.resetTimer("walk");
                 walkFrame++;
                 if(walkFrame > frameEnd) walkFrame = frameStart;
             }
        }
        else {
            timer.resetTimer("walk");
            walkFrame = frameDefault;
        }

        if(grounded) {
            if(Engine.get().getInput().isKeyPressed("space")) { entity.accelerate(100*deltaX, 90.0f); }
        }

        if(Engine.get().getInput().isKeyPressed("p")) { entity.accelerate(-1.0f, 0); }
        if(Engine.get().getInput().isKeyPressed("[")) { Engine.get().getGraphics().getCameraHelper().addZoom(0.01f); }
        if(Engine.get().getInput().isKeyPressed("]")) { Engine.get().getGraphics().getCameraHelper().addZoom(-0.01f); }
        if(Engine.get().getInput().isKeyPressed("m")) entity.angle += 0.01f;

        legFrame = walkFrame;
        bodyFrame = walkFrame;
        headFrame = walkFrame;
        armFrame = walkFrame;
    }

    public void Animate(String id, int index) {
        events.add(new myPair(id, index));

    }

    private void updateBodyPieces(Entity entity) {

        head.x_pos = entity.x_pos;
        head.y_pos = entity.y_pos;
        head.angle = entity.angle;
        head.spriteOffsetY = headFrame * 56;
        head.flipX = entity.flipX;

        hair.x_pos = entity.x_pos;
        hair.y_pos = entity.y_pos;
        hair.angle = entity.angle;
        hair.flipX = entity.flipX;
        hair.spriteOffsetY = (headFrame - frameStart) * 56;

        legs.x_pos = entity.x_pos;
        legs.y_pos = entity.y_pos;
        legs.angle = entity.angle;
        legs.spriteOffsetY = legFrame * 56;
        legs.flipX = entity.flipX;

        body.x_pos = entity.x_pos;
        body.y_pos = entity.y_pos;
        body.angle = entity.angle;
        body.spriteOffsetY = bodyFrame * 56;
        body.flipX = entity.flipX;

        hands.x_pos = entity.x_pos;
        hands.y_pos = entity.y_pos;
        hands.angle = entity.angle;
        hands.spriteOffsetY = bodyFrame * 56;
        hands.flipX = entity.flipX;
    }

    private void updateAnimation() {


        for(myPair<String, Integer> event: events) {
            if(event.first.equals("pickaxe")) bodyFrame = event.second + 1;
        }
        events.clear();
    }


    private void testMode(Entity entity) {

        if(Engine.get().getInput().isKeyPressed("w")) entity.accelerate(deltaX, 90.0f);
        if(Engine.get().getInput().isKeyPressed("a")) entity.accelerate(deltaX, 180.0f);
        if(Engine.get().getInput().isKeyPressed("s")) entity.accelerate(deltaX, 270.0f);
        if(Engine.get().getInput().isKeyPressed("d")) entity.accelerate(deltaX, 0.0f);

      //  System.out.println("X. " + entity.x_pos + " Y." + entity.y_pos);

    }

    public void update(Entity entity) {

        if(myWorld.get().testMode)  testMode(entity);

        else {
            updateControls(entity);
            updateAnimation();
        }


        updateBodyPieces(entity);
    }
}

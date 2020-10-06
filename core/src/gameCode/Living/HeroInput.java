package gameCode.Living;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
import com.mygdx.game.Engine;
import gameCode.Factory.EntityFactory;
import gameCode.Infrastructure.*;
import gameCode.Utilities.Timer;
import gameCode.Utilities.myMath;

public class HeroInput extends Component {


    float deltaX;
    float wkSpeed;
    int walkFrame;
    Timer timer;


    Entity head;
    Entity hair;
    Entity legs;
    Entity body;


    public HeroInput() {


        type = "input";
        timer = new Timer();
        timer.addTimer("walk");
        deltaX = 10.0f;
        wkSpeed = 0.1f;
        walkFrame = 0;




        /*

        hair = new Entity();
        hair.spriteName = "Player_Hair";
        hair.width = Engine.get().getAssets().getSpriteDimensions(hair.spriteName).first;
        hair.height = Engine.get().getAssets().getSpriteDimensions(hair.spriteName).second;
        hair.z_pos = 21;
        hair.deleteRange = -2;
        hair.drawMode = "normal";
        hair.entityName = "hair";
        myWorld.get().entitiesToBeAdded.add(hair);

        legs = new Entity();
        legs.spriteName = "Skin_Legs";
        legs.width = Engine.get().getAssets().getSpriteDimensions(legs.spriteName).first;
        legs.height = Engine.get().getAssets().getSpriteDimensions(legs.spriteName).second;
        legs.z_pos = 21;
        legs.deleteRange = -2;
        legs.drawMode = "normal";
        legs.entityName = "legs";
        myWorld.get().entitiesToBeAdded.add(legs);

        body = new Entity();
        body.spriteName = "Skin_Body";
        body.width = Engine.get().getAssets().getSpriteDimensions(body.spriteName).first;
        body.height = Engine.get().getAssets().getSpriteDimensions(body.spriteName).second;
        body.z_pos = 21;
        body.deleteRange = -2;
        body.drawMode = "normal";
        body.entityName = "body";
        myWorld.get().entitiesToBeAdded.add(body);

         */

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
        
        



    }



    private void updateControls(Entity entity) {


        float center = myWorld.get().getNumPixels() / 2.0f;
        float angle = myMath.angleBetweenCells(center, center, entity.x_pos, entity.y_pos);

        entity.angle = -myMath.toRad(angle - 90.0f );

        boolean grounded = Engine.get().getPhysics().pollFixture(entity);
        float walkSpeed = deltaX;
        if(!grounded) walkSpeed /= 2.0f;


        boolean dirBefore = entity.flipX;
        boolean isWalk = false;

        if(Engine.get().getInput().isKeyPressed("a") && !Engine.get().getInput().isKeyPressed("d")) {
            entity.flipX = false;
            entity.accelerate(deltaX, 180.0f);
            isWalk = true;
        }

        if(Engine.get().getInput().isKeyPressed("d") && !Engine.get().getInput().isKeyPressed("a")) {
            entity.flipX = true;
            entity.accelerate(deltaX, 0.0f);
            isWalk = true;
        }

        if( Engine.get().getInput().isKeyPressed("w")) entity.accelerate(deltaX, 90.0f);
        if( Engine.get().getInput().isKeyPressed("s")) entity.accelerate(deltaX, 270.0f);

        if(dirBefore == entity.flipX && isWalk) {
             timer.update("walk", myWorld.get().getDeltaTime());
             if(timer.getTime("walk") > wkSpeed) {
                 timer.resetTimer("walk");
                 walkFrame++;
                 if(walkFrame > 15) walkFrame = 0;
             }
        }
        else {
            timer.resetTimer("walk");
            walkFrame = 0;
        }


       //entity.spriteOffsetY = walkFrame * 56;




        if(grounded) {
            if(Engine.get().getInput().isKeyPressed("space")) { entity.accelerate(100*deltaX, 90.0f); }
        }


        if(Engine.get().getInput().isKeyPressed("p")) { entity.accelerate(-1.0f, 0); }
        if(Engine.get().getInput().isKeyPressed("[")) { Engine.get().getGraphics().getCameraHelper().addZoom(0.01f); }
        if(Engine.get().getInput().isKeyPressed("]")) { Engine.get().getGraphics().getCameraHelper().addZoom(-0.01f); }


    }

    public void update(Entity entity) {


        updateControls(entity);



        head.x_pos = entity.x_pos;
        head.y_pos = entity.y_pos;
        head.angle = entity.angle;

        hair.x_pos = entity.x_pos;
        hair.y_pos = entity.y_pos;
        hair.angle = entity.angle;

        legs.x_pos = entity.x_pos;
        legs.y_pos = entity.y_pos;
        legs.angle = entity.angle;

        body.x_pos = entity.x_pos;
        body.y_pos = entity.y_pos;
        body.angle = entity.angle;



    }


}

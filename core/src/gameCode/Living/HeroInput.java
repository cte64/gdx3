package gameCode.Living;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
import com.mygdx.game.Engine;
import gameCode.Infrastructure.*;
import gameCode.Utilities.Timer;
import gameCode.Utilities.myMath;

public class HeroInput extends Component {


    float deltaX;
    float wkSpeed;
    int walkFrame;
    Timer timer;

    public HeroInput() {
        type = "input";
        timer = new Timer();
        timer.addTimer("walk");
        deltaX = 10.0f;
        wkSpeed = 0.1f;
        walkFrame = 0;
    }

    public void update(Entity entity) {

        float center = myWorld.get().getNumPixels() / 2.0f;
        float angle = myMath.angleBetweenCells(center, center, entity.x_pos, entity.y_pos);

        entity.angle = -myMath.toRad(angle - 90.0f );
        entity.accelerate(deltaX/2.0f, 270.0f);


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


        entity.spriteOffsetY = walkFrame * 56;




        if(grounded) {
            if(Engine.get().getInput().isKeyPressed("space")) { entity.accelerate(100*deltaX, 90.0f); }
        }


        if(Engine.get().getInput().isKeyPressed("p")) { entity.accelerate(-1.0f, 0); }
        if(Engine.get().getInput().isKeyPressed("[")) { Engine.get().getGraphics().getCameraHelper().addZoom(0.01f); }
        if(Engine.get().getInput().isKeyPressed("]")) { Engine.get().getGraphics().getCameraHelper().addZoom(-0.01f); }

    }

}

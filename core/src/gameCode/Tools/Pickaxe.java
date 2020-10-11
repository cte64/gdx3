package gameCode.Tools;

import com.mygdx.game.Engine;
import gameCode.Infrastructure.Component;
import gameCode.Infrastructure.Entity;
import gameCode.Infrastructure.myWorld;
import gameCode.Menus.Inventory.AddEntity;
import gameCode.Utilities.Timer;
import gameCode.Utilities.myMath;

public class Pickaxe extends Component implements AddEntity {

    private Entity parent;
    AnimationSequence animate;
    AnimationSequence backward;

    public void addEntity(Entity entity) {
        parent = entity;
    }

    public Pickaxe() {
        type = "logic";
        parent = null;
        animate = new AnimationSequence();
        backward = new AnimationSequence();

        int offsetAngle = 0;

        float time = 0.7f;
        animate.addFrame(-10, 14, 60 + offsetAngle, time);
        //animate.addFrame(-6, 2, 8 + offsetAngle, time);
        //animate.addFrame(-2, -10, -40 + offsetAngle, time);
       // animate.addFrame(0, -10, -60 + offsetAngle, time);
    }


    public void update(Entity entity) {

        if(parent == null) return;

        if(Engine.get().getInput().isMousePressed("mouse left")) {
            entity.drawMode = "normal";


            /*
            if(parent.flipX) {
                animate.setMirrorX(true);
                entity.flipX = true;
            }
            else {
                animate.setMirrorX(false);
                entity.flipX = false;
            }

             */

            animate.update();
            entity.angle = parent.angle + myMath.toRad( animate.getAngle() );
            entity.x_pos = parent.x_pos + parent.origin.first - entity.origin.first + animate.getX();
            entity.y_pos = parent.y_pos + parent.origin.second - entity.origin.second + animate.getY();

            for(Component comp: parent.components) {
                if(comp instanceof Animation)
                    ((Animation) comp).Animate("pickaxe", animate.getIndex(parent.flipX));
            }
        }
        else {
            entity.drawMode = "hidden";
            animate.reset(parent.flipX);
        }
    }
}

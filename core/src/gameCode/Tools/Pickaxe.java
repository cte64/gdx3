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

        float time = 0.15f;
        animate.addFrame(5, 0, 60 + offsetAngle, time);
        animate.addFrame(5, 0, 8 + offsetAngle, time);
        animate.addFrame(2, -1, -40 + offsetAngle, time);
        animate.addFrame(0, -2, -60 + offsetAngle, time);

        backward.addFrame(5, 0, 60 + offsetAngle, time);
        backward.addFrame(5, 0, 8 + offsetAngle, time);
        backward.addFrame(2, -1, -40 + offsetAngle, time);
        backward.addFrame(0, -2, -60 + offsetAngle, time);
    }


    public void update(Entity entity) {

        if(parent == null) return;

        if(Engine.get().getInput().isMousePressed("mouse left")) {
            entity.drawMode = "normal";
            animate.update();
            entity.x_pos = parent.x_pos + parent.width/2.0f + animate.getX(parent.flipX);
            entity.y_pos = parent.y_pos + parent.height/2.0f + animate.getY(parent.flipX);
            entity.angle = parent.angle + myMath.toRad( animate.getAngle(parent.flipX) );

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

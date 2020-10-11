package gameCode.Tools;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Engine;
import gameCode.Infrastructure.Component;
import gameCode.Infrastructure.Entity;
import gameCode.Infrastructure.myWorld;
import gameCode.Menus.Inventory.AddEntity;
import gameCode.Utilities.Coordinates;
import gameCode.Utilities.Timer;
import gameCode.Utilities.myMath;
import gameCode.Utilities.myPair;

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
        animate.setOffsetAngle(45.0f);


        int offsetAngle = 0;

        float time = 0.1f;
        animate.addFrame(-12, 24, 13, time);
        animate.addFrame(17, 18, -39, time);
        animate.addFrame(25, -6, -85 + offsetAngle, time);
        animate.addFrame(23, -13, -90 + offsetAngle, time);
    }


    public void update(Entity entity) {

        if(parent == null) return;

        if(Engine.get().getInput().isMousePressed("mouse left")) {
            entity.drawMode = "normal";

            animate.update(parent, entity);


            for(Component comp: parent.components) {
                if(comp instanceof Animation)
                    ((Animation) comp).Animate("pickaxe", animate.getIndex());
            }
        }
        else {
            entity.drawMode = "hidden";
            animate.reset(parent.flipX);
        }
    }
}

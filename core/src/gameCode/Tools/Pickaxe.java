package gameCode.Tools;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Engine;
import gameCode.Infrastructure.Component;
import gameCode.Infrastructure.Entity;
import gameCode.Infrastructure.myWorld;
import gameCode.Menus.Inventory.AddEntity;
import gameCode.Menus.Inventory.ModifyInventory;
import gameCode.Terrain.ModifyTerrain;
import gameCode.Terrain.PixelT;
import gameCode.Utilities.Coordinates;
import gameCode.Utilities.Timer;
import gameCode.Utilities.myMath;
import gameCode.Utilities.myPair;

import java.util.ArrayList;
import java.util.HashMap;

public class Pickaxe extends Component implements AddEntity {

    private Entity parent;
    AnimationSequence animate;
    private int digRadius;
    private int digRange;


    public void addEntity(Entity entity) {
        parent = entity;
    }

    public Pickaxe() {
        type = "logic";
        parent = null;
        digRadius = 6;
        digRange = 150;
        animate = new AnimationSequence();
        animate.setOffsetAngle(45.0f);

        float time = 0.1f;
        animate.addFrame(-12, 24, 13, time);
        animate.addFrame(17, 18, -39, time);
        animate.addFrame(25, -6, -85, time);
        animate.addFrame(23, -13, -90, time);
    }

    public void setDigRadius(int digRadius) { this.digRadius = digRadius; }

    public void update(Entity entity) {

        if(parent == null) return;

        if(Engine.get().getInput().isMousePressed("mouse left")) {
            entity.drawMode = "normal";
            animate.update(parent, entity);

            if(animate.getIndex() == animate.getNumFrames() - 1 && animate.getFirstTime()) {

                float xPos = Engine.get().getInput().getMouseAbs().first;
                float yPos = Engine.get().getInput().getMouseAbs().second;

                float parentCenterX = parent.x_pos + parent.origin.first;
                float parentCenterY = parent.y_pos + parent.origin.second;
                float distance = myMath.mag(xPos, yPos, parentCenterX, parentCenterY);

                if(distance < digRange) {
                    HashMap<String, Integer> pixels = ModifyTerrain.addCircle(xPos, yPos, 10, "empty");
                    for(String key: pixels.keySet()) {
                    for(Component comp: parent.components) {
                        if(comp instanceof ModifyInventory)
                            ((ModifyInventory) comp).addItem(key, "current", -1, pixels.get(key));
                    }}
                }
            }

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

package gameCode.Tools;

import com.mygdx.game.Engine;
import gameCode.Infrastructure.Component;
import gameCode.Infrastructure.Entity;
import gameCode.Infrastructure.myWorld;
import gameCode.Menus.Inventory.AddEntity;
import gameCode.Utilities.Timer;

public class Pickaxe extends Component implements AddEntity {

    private Entity parent;

    private Timer timer;
    private float tickTime = 0.1f;
    private float tickIncrement = 0.1f;



    public void addEntity(Entity entity) {
        parent = entity;
    }



    public Pickaxe() {
        timer = new Timer();
        timer.addTimer("pick");
        type = "logic";
        parent = null;
    }


    public void update(Entity entity) {

        if(parent == null) return;

        entity.x_pos = parent.x_pos;
        entity.y_pos = parent.y_pos;


        if(Engine.get().getInput().isMousePressed("mouse left")) {


            entity.drawMode = "normal";
            timer.update(myWorld.get().getDeltaTime());
            if(timer.getTime("pick") > tickTime) {
                timer.resetTimer("pick");

                if(parent.flipX) entity.angle += tickIncrement;
                else entity.angle -= tickIncrement;
            }
        }

        else {
            timer.resetTimer("pick");
            entity.drawMode = "hidden";
            entity.angle = parent.angle;
        }
    }
}

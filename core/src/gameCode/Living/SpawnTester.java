package gameCode.Living;

import com.mygdx.game.Engine;
import gameCode.Factory.EntityFactory;
import gameCode.Infrastructure.Component;
import gameCode.Infrastructure.Entity;
import gameCode.Infrastructure.myWorld;
import gameCode.Utilities.myString;

public class SpawnTester extends Component {

    public SpawnTester() {
        type = "logic";
    }

    private boolean once = false;

    public void update(Entity entity) {

        if( Engine.get().getInput().isMousePressed("mouse left") && !once) {

            once = true;
            int x = Engine.get().getInput().getMouseAbs().first.intValue();
            int y = Engine.get().getInput().getMouseAbs().second.intValue();

            myString treeStr = new myString("[type: plant][subType: tree][xPos: ][yPos: ]");
            treeStr.setField("xPos", myString.toString(x));
            treeStr.setField("yPos", myString.toString(y));

            Entity newTree = EntityFactory.createEntity(treeStr.data);
            myWorld.get().entitiesToBeAdded.add(newTree);
        }

        if( !Engine.get().getInput().isMousePressed("mouse left") && once) once = false;


    }
}

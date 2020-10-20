package gameCode.Living.Plants;
import gameCode.Infrastructure.Component;
import gameCode.Infrastructure.Entity;

import java.util.ArrayList;

public class Tree extends Component {

    String treeType;
    int treeHeight;
    ArrayList<Entity> trunks;
    public Tree(String treeType, int treeHeight) {
        type = "input";
        this.treeType = treeType;
        this.treeHeight = treeHeight;
        trunks = new ArrayList<Entity>();
        System.out.println("we got here and we on top");
    }


    public void update(Entity entity) {
        System.out.println("we got here and we on top");
    }
}

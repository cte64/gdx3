package gameCode.Tools;

import gameCode.Infrastructure.Component;
import gameCode.Infrastructure.Entity;
import gameCode.Menus.Inventory.AddEntity;

public class Pickaxe extends Component implements AddEntity {

    private Entity parent;

    public void addEntity(Entity entity) {
        parent = entity;
    }

    public Pickaxe() {
        type = "logic";
        parent = null;
    }


    public void update(Entity entity) {


        System.out.println("dirty");

    }


}

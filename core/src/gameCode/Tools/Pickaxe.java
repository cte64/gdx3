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

        if(parent != null) {
            entity.x_pos = parent.x_pos + parent.origin.first;
            entity.y_pos = parent.y_pos + parent.origin.second;
            entity.z_pos = parent.z_pos + 10;
            entity.angle = parent.angle;
        }


    }


}

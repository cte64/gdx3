package gameCode.Infastructure;
import java.util.ArrayList;

public class Entity {

    public float x_pos, y_pos;
    public String spriteName;

    ArrayList<Component> components;

    public Entity() {
        components = new ArrayList<Component>();

    }

    public void addComponent(Component newComponent) { components.add(newComponent); }
    public void updateType(String type) {
        for(Component comp: components) {
            if(comp.type == type)
                comp.update(this);
        }
    }

    public void getComponentByName() {

    }


}

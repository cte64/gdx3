package gameCode.Infastructure;
import java.util.ArrayList;

public class Entity {

    float x_pos, y_pos;
    ArrayList<Component> components;

    void addComponent(Component newComponent) { components.add(newComponent); }
    void updateType(String type) {
        for(Component comp: components) {
            if(comp.type == type)
                comp.update(this);
        }
    }

    void getComponentByName() {

    }


}

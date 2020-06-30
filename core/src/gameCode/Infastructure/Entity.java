package gameCode.Infastructure;
import java.util.ArrayList;

public class Entity {

    public float x_pos, y_pos, angle, scaleW, scaleH, velMag, velAng, lastGoodX, lastGoodY, lastGoodAngle;
    int bitMapX, bitMapY, z_pos, deleteRange, spriteOffsetX, spriteOffsetY;// -2 = false; -1 = delete at edge, > 0 =  delete out of range
    public boolean moveable, drawable, markForDeletion, flip, cameraBound;
    public String spriteName, drawMode, entityName;
    ArrayList<Component> components;


    public Entity() {

        x_pos = 0.0f;
        y_pos = 0.0f;


        /*
        public float x_pos, y_pos, angle, scaleW, scaleH, velMag, velAng, lastGoodX, lastGoodY, lastGoodAngle;
        int bitMapX, bitMapY, z_pos, deleteRange, spriteOffsetX, spriteOffsetY;// -2 = false; -1 = delete at edge, > 0 =  delete out of range
        public boolean moveable, drawable, markForDeletion, flip, cameraBound;
        public String spriteName, drawMode, entityName;
        ArrayList<Component> components;*/






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

    public int getWidth() {
        return 10;
    }

    public int getHeight() {
        return 10;
    }

    public float getXVelocity() { return 0.0f; }
    public float getYVelocity() { return 0.0f; }

}

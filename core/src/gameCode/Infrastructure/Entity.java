package gameCode.Infrastructure;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import gameCode.Utilities.myMath;
import gameCode.Utilities.myPair;

import java.util.ArrayList;


public class Entity {

    public float x_pos, y_pos;
    public int z_pos;
    public float angle;
    public myPair<Float, Float> scale;
    public myPair<Float, Float> origin;
    public boolean flipX, flipY;
    public float width;
    public float height;
    public int spriteOffsetX, spriteOffsetY;


    private float velMag, velAng;
    public int deleteRange;// -2 = false; -1 = delete at edge, > 0 =  delete out of range
    public String spriteName, drawMode, entityName;
    ArrayList<Component> components;

    public Entity() {
        scale = new myPair(1.0f, 1.0f);
        x_pos = 0.0f;
        y_pos = 0.0f;
        angle = 0.0f;
        velMag = 0.0f;
        velAng = 0.0f;
        z_pos = 0;
        deleteRange = 0;
        spriteOffsetX = 0;
        spriteOffsetY = 0;
        flipX = false;
        flipY = false;
        spriteName = "";
        drawMode = "normal";
        entityName = "";
        components = new ArrayList<Component>();
        origin = new myPair(0.0f, 0.0f);
    }

    public void addComponent(Component newComponent) { components.add(newComponent); }

    public void updateType(String type) {
        for(Component comp: components) {
            if(comp.type == type)
                comp.update(this);
        }
    }

    public Component getComponent(String type) {
        for(Component comp: components) {
            if(comp.type == type)
                return comp;
        }
        return null;
    }

    public ArrayList<Component> getComponents(String type) {
        ArrayList<Component> retVal = new ArrayList<Component>();
        for(Component comp: components) {
            if(comp.type == type)
                retVal.add(comp);
        }
        return retVal;
    }

    public float getWidth() {
        return (width * scale.first);
    }

    public float getHeight() {
        return (height * scale.second);
    }

    public void deleteComponents() { components.clear(); }

    public void deleteComponent(Component component) {
        components.remove(component);
    }

    public float getXVelocity() {
        return (float)(velMag * Math.cos(velAng));
    }

    public float getYVelocity() {
        return (float)(velMag * Math.sin(velAng));
    }

    public void accelerate(float mag, float ang) {

        if(mag == -1.0f) {
            velMag = 0;
            velAng = 0;
            return;
        }

        ang = myMath.toRad(ang);

        float currentX = getXVelocity();
        float currentY = getYVelocity();

        float addX = (float)(mag*Math.cos(ang + angle));
        float addY = (float)(mag*Math.sin(ang + angle));

        float newX = currentX + addX;
        float newY = currentY + addY;

        float updateMag = (float)Math.sqrt(newX*newX + newY*newY);
        float updateAng = myMath.toRad( myMath.angle1(newX, newY) );

        velMag = myMath.clamp(updateMag, 0.0f, 500.0f);
        velAng = updateAng;
    }
}

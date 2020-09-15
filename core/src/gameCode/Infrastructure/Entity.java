package gameCode.Infrastructure;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.game.PhysObj;
import gameCode.Utilities.myPair;

import java.util.ArrayList;


public class Entity {

    public float x_pos, y_pos, angle, velMag, velAng;
    public int bitMapX, bitMapY, z_pos, deleteRange;// -2 = false; -1 = delete at edge, > 0 =  delete out of range
    public boolean moveable, markForDeletion, flip;
    public String spriteName, drawMode, entityName;
    public myPair<Float, Float> scale;
    public ArrayList<Body> bodies;
    public myPair<Float, Float> origin;
    ArrayList<Component> components;
    public float width;
    public float height;
    public boolean markBody;
    public boolean markBody1;


    PhysObj physics;

    public void updateBody() {
        if(bodies.size() == 0) {
            x_pos += velAng;
            y_pos += velMag;
        }
        else if(bodies.size() == 1) {
            x_pos = bodies.get(0).getPosition().x;
            y_pos = bodies.get(0).getPosition().y;
        }
    }

    public Entity() {

        physics = new PhysObj();
        markBody1 = false;
        bodies = new ArrayList<Body>();
        scale = new myPair(1.0f, 1.0f);
        x_pos = 0.0f;
        y_pos = 0.0f;
        angle = 0.0f;
        velMag = 0.0f;
        velAng = 0.0f;
        bitMapX = 0;
        bitMapY = 0;
        z_pos = 0;
        deleteRange = 0;
        moveable = false;
        markForDeletion = false;
        flip = false;
        spriteName = "";
        drawMode = "normal";
        entityName = "";
        components = new ArrayList<Component>();
        origin = new myPair(0.0f, 0.0f);
        markBody = false;
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



    /*
    public float getXVelocity() {
        return velMag * Math.cos( toRad(velAng) );
    }

    public float getYVelocity() {
        return -velMag * sin( toRad(velAng) );
    }

     */


    /*
        int getWidth();
    int getHeight();

    Entity();
    Entity(bool new_moveable, float new_x_pos, float new_y_pos, int new_z_pos, float new_x_velo, float new_y_velo);

    template<typename C> void addComponent(C* c){ components[&typeid(*c)] = c;}
    template<typename B> void deleteComponent(B* c){ delete(components[&typeid(*c)]);components.erase(&typeid(*c)); }
    template <typename A> A* get_component() {
        for(auto iter = components.begin(); iter != components.end(); iter++) if(iter->first == &typeid(A)) return (A*)iter->second;
        return nullptr;
    }
    void update_logic(std::string update_what);
    void interactWith(Entity& entity, std::string message);

    void accelerate(float mag, float ang);
    float getXVelocity();
    float getYVelocity();
    void setXVelocity(float newX);
    void setYVelocity(float newY);

    void collider();

    std::string getSerializedEntity();


    struct by_z_pos {
        bool operator()(Entity *const& a, Entity *const& b) {
            return (a->z_pos < b->z_pos);
        }
    };

    Entity& operator = (const Entity &a);
     */


}

package gameCode.Infastructure;
import java.util.ArrayList;


public class Entity {

    public float x_pos, y_pos, angle, scaleW, scaleH, velMag, velAng, lastGoodX, lastGoodY, lastGoodAngle;
    public int bitMapX, bitMapY, z_pos, deleteRange, spriteOffsetX, spriteOffsetY;// -2 = false; -1 = delete at edge, > 0 =  delete out of range
    public boolean moveable, drawable, markForDeletion, flip, cameraBound;
    public String spriteName, drawMode, entityName;

    ArrayList<Component> components;

    public float width;
    public float height;

    public Entity() {
        x_pos = 0.0f;
        y_pos = 0.0f;
        angle = 0.0f;
        scaleW = 0.0f;
        scaleH = 0.0f;
        velMag = 0.0f;
        velAng = 0.0f;
        lastGoodX = 0.0f;
        lastGoodY = 0.0f;
        lastGoodAngle = 0.0f;
        bitMapX = 0;
        bitMapY = 0;
        z_pos = 0;
        deleteRange = 0;
        spriteOffsetX = 0;
        spriteOffsetY = 0;
        moveable = false;
        drawable = false;
        markForDeletion = false;
        flip = false;
        cameraBound = false;
        spriteName = "";
        drawMode = "normal";
        entityName = "";
        components = new ArrayList<Component>();
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
        return width;
    }

    public float getHeight() {
        return height;
    }

    public void deleteComponents() { components.clear(); }


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

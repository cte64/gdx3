package gameCode.Infrastructure;

abstract public class Component {
    public boolean pausable;
    public String type;
    public String componentName;
    public Component() { type = ""; pausable = false; componentName = ""; }
    public abstract void update(Entity entity);
    public void serializeComponent(Entity entity) {}
    public void interactWith(Entity entity, Entity interactEnt, String message) {}
}

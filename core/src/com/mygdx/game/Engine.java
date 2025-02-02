package com.mygdx.game;

public class Engine {

    //singleton instance variable ================================================================
    private static Engine engineInstance = null;

    private Engine() {
        physics = new Physics();
        input = new InputAL();
        assets = new Assets();
        fileSystem = new FileSystem();
        graphics = new Graphics();
    }

    public static Engine get() {
        if(engineInstance == null)
            engineInstance = new Engine();
        return engineInstance;
    }

    //All the components this engine is made out of =============================================
    Physics physics;
    Graphics graphics;
    InputAL input;
    Assets assets;
    FileSystem fileSystem;

    //getters ====================================================================================
    public Graphics getGraphics() { return graphics; }
    public InputAL getInput() { return input; }
    public Assets getAssets() { return assets; }
    public FileSystem getFileSystem() { return fileSystem; }
    public Physics getPhysics() { return physics; }
}

package gameCode.Infastructure;

public class State {

    private static void roundEarth() {}
    private static void loadGame() {}
    private static void paused() {}
    private static void play() {}
    private static void mainScreen() {}
    private static void newGame() {}
    private static void loading() {}

    private State() {}
    public static void loadState() {}
    public void deleteMenuItems() {}

    public String state;
}

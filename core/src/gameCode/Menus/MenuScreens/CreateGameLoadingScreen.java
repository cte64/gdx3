package gameCode.Menus.MenuScreens;

import gameCode.Infrastructure.*;
import gameCode.Menus.MenuManager;
import gameCode.Menus.TextComponent;
import gameCode.Terrain.MakeWorld;
import gameCode.Utilities.myString;

import java.util.ArrayList;

public class CreateGameLoadingScreen extends Component {




    private String background;
    MakeWorld makeWorld;

    ArrayList<myString> loadingMessages;
    private ArrayList<TextComponent> textComponents;

    int maxListSize;
    int topOffset = -30;
    int itemHeight = 18;

    Thread t;
    String directory;

    MenuManager menu;


    public class MyRunnable implements Runnable {
        private MakeWorld makeWorld;
        public MyRunnable(MakeWorld makeWorld) {
            this.makeWorld = makeWorld;
        }

        public void run() {
            makeWorld.start();
        }
    }

    public CreateGameLoadingScreen() {

        type = "logic";

        menu = new MenuManager();

        background = "[type: menu][name: background]";


        menu.registerItem(background, "mainMenuBack", null, "[vertical: center][horizontal: center]", 0, 0, 0);
        menu.addText(background, new TextComponent("Creating New World", 10, "[vertical: top][horizontal: center]", 0, 0));

        String name = myString.getField(State.getState(), "name");
        String numChunks = myString.getField(State.getState(), "numChunks");
        int numChunksInt = myString.stringToInt(numChunks);

        directory = name;
        textComponents = new ArrayList<TextComponent>();
        maxListSize = 7;

        loadingMessages = new ArrayList<myString>();
        int radius = (int)((numChunksInt * World.get().tilesPerChunk * World.get().tileSize) * 0.3f);
        makeWorld = new MakeWorld(name, numChunksInt, radius, loadingMessages);

        t = new Thread(new MyRunnable(makeWorld));
        t.start();
    }

    public void update(Entity entity) {

        if(textComponents.size() != loadingMessages.size()) {
            for(int x = textComponents.size(); x < loadingMessages.size(); x++) {
                int yPos = topOffset - x*itemHeight;
                TextComponent newComp = new TextComponent(loadingMessages.get(x).data, 10, "[vertical: top][horizontal: center]", 0, yPos);
                textComponents.add(newComp);
                menu.addText(background, newComp);
            }

            //position only the last "maxListSize" items
            if(textComponents.size() > maxListSize) {
                menu.getEnt(background).deleteComponent(textComponents.get(0));
                textComponents.remove(0);
                loadingMessages.remove(0);
                for(int x = 0; x < textComponents.size(); x++) {
                    int yPos = topOffset - x*itemHeight;
                    textComponents.get(x).yOffset = yPos;
                }
            }
        }

        if(!t.isAlive()) {
            myString newState = new myString("[action: play][directory: ]");
            newState.setField("directory", directory);
            State.play(newState.data);
        }
    }

}

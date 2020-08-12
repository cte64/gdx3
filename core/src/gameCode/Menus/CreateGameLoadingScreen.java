package gameCode.Menus;

import gameCode.Infastructure.Component;
import gameCode.Infastructure.Entity;
import gameCode.Infastructure.TextComponent;
import gameCode.Infastructure.World;
import gameCode.Terrain.MakeWorld;
import gameCode.Utilities.MathUtils;
import gameCode.Utilities.StringUtils;

import java.util.ArrayList;

public class CreateGameLoadingScreen extends Component {

    private MenuItem background;
    MakeWorld makeWorld;


    ArrayList<StringUtils> loadingMessages;
    int sizeBefore;
    int textIndex;
    int maxListSize;

    private ArrayList<TextComponent> textComponents;

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

        background = new MenuItem("[type: menu][name: background]", "loadGameBack", null, "[vertical: center][horizontal: center]", 0, 0, 0, 479, 600);
        background.addText(new TextComponent("Creating New World", 10, "[vertical: top][horizontal: center]", 0, 0));

        String name = StringUtils.getField(World.getCurrentState(), "name");
        String numChunks = StringUtils.getField(World.getCurrentState(), "numChunks");
        int numChunksInt = StringUtils.stringToInt(numChunks);


        textComponents = new ArrayList<TextComponent>();

        sizeBefore = 0;
        textIndex = 0;
        maxListSize = 10;

        loadingMessages = new ArrayList<StringUtils>();
        makeWorld = new MakeWorld(name, numChunksInt, 800, loadingMessages);

        Thread t = new Thread(new MyRunnable(makeWorld));
        t.start();
    }

    private void postionTextComponents() {

        if(textComponents.size() > maxListSize) {
            textIndex = textComponents.size() - maxListSize;
            textIndex = MathUtils.clamp(textIndex, 0, textComponents.size() - 1);
        }

        for(int y = textIndex; y < textComponents.size(); y++) {

            int yPos = 30 * y;
            

        }


    }

    public void update(Entity entity) {


        if(sizeBefore != loadingMessages.size()) {

            int numComponents = loadingMessages.size() - sizeBefore;
            int startIndex = textComponents.size();
            startIndex = MathUtils.clamp(startIndex, 0, loadingMessages.size() - 1);

            for(int x = 0; x < numComponents; x++) {
                int index = startIndex + x;
                index = MathUtils.clamp(index, 0, loadingMessages.size() - 1);

                TextComponent newComp = new TextComponent("stuff", 10, "[vertical: top][horizontal: center]", 0, -100);
                textComponents.add(newComp);
                background.addText(newComp);
            }
            sizeBefore += numComponents;
            postionTextComponents();
        }


    }

}

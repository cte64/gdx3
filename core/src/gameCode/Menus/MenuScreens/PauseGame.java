package gameCode.Menus.MenuScreens;

import gameCode.Infrastructure.*;
import gameCode.Menus.MenuManager;
import gameCode.Menus.TextComponent;
import gameCode.Utilities.StringUtils;

public class PauseGame extends Component {

    private boolean escToggle;
    MenuManager menu;

    private String background;
    private String backButton;
    private String optionsButton;
    private String saveAndQuit;

    int start = 6;
    int height = 43;

    public PauseGame() {

        type = "logic";

        menu = new MenuManager();
        escToggle = false;

        background = "[type: menu][subType: paused][name: background]";
        backButton = "[type: menu][subType: paused][name: backButton]";
        optionsButton = "[type: menu][subType: paused][name: optionsButton]";
        saveAndQuit ="[type: menu][subType: paused][name: saveAndQuit]";
        
        //Background
        menu.registerItem(background, "mainMenuBack", null, "[vertical: center][horizontal: center]", 0, 0, 4);
        menu.addText(background, new TextComponent("Paused", 10, "[vertical: top][horizontal: center]", 0, 0));

        //Save and Quit button
        menu.registerItem(saveAndQuit, "menuItem", background, "[vertical: top][horizontal: center]", 0, start  + 1*height, 4);
        menu.addText(saveAndQuit, new TextComponent("Save And Quit", 10, "[vertical: center][horizontal: center]", 0, 0));

        //Options buttons
        menu.registerItem(optionsButton, "menuItem", background, "[vertical: top][horizontal: center]", 0, start  +2*height, 4);
        menu.addText(optionsButton, new TextComponent("Options", 10, "[vertical: center][horizontal: center]", 0, 0));

        //bottom buttons
        menu.registerItem(backButton, "menuItem", background, "[vertical: top][horizontal: center]", 0, start  + 3*height, 4);
        menu.addText(backButton, new TextComponent("Back", 10, "[vertical: center][horizontal: center]", 0, 0));

        //escToggle everything to hidden for now
        menu.updateDrawMode(background, "hidden");
    }

    public void update(Entity entity) {


        Entity ent = menu.getEnt(background);
        if(ent == null) return;
        String currentMode = ent.drawMode;

        String worldState = StringUtils.getField(State.getState(), "action");
        if(worldState.equals("inventory")) return;

        if(InputAL.isKeyPressed("esc") && !escToggle) escToggle = true;
        if(!InputAL.isKeyPressed("esc") && escToggle) {
            if(currentMode.equals("hud")) {
                menu.updateDrawMode(background, "hidden");
                State.setState("[action: play]");
            }
            if(currentMode.equals("hidden")) {
                menu.updateDrawMode(background, "hud");
                State.setState("[action: paused]");
            }
            escToggle = false;
        }

        if(currentMode.equals("hud")) {
            if(menu.isLeftClicked(backButton)) {
                menu.updateDrawMode(background, "hidden");
                State.setState("[action: play]");
            }
            if(menu.isLeftClicked(saveAndQuit)) {
                FileSystem.saveCurrentChunks();
                State.mainMenu();
            }
        }
    }
}

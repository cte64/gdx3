package gameCode.Menus;

import gameCode.Infastructure.*;

public class PauseGame extends Component {


    private boolean toggle;

    private class PauseMenu {

      public MenuItem background;
      public MenuItem backButton;
      public MenuItem optionsButton;
      public MenuItem saveAndQuit;
      int start = 6;
      int height = 43;

      public PauseMenu() {
          background = new MenuItem("[type: menu][subType: paused][name: background]", "mainMenuBack", null, "[vertical: center][horizontal: center]", 0, 0, 4, 360, 180);
          background.addText( new TextComponent("Paused", 10, "[vertical: top][horizontal: center]", 0, 0));

          //Save and Quit button
          saveAndQuit = new MenuItem("[type: menu][subType: paused][name: saveAndQuit]", "menuItem", background.treeNode, "[vertical: top][horizontal: center]", 0, start  + 1*height, 4, 350, 40);
          saveAndQuit.addText( new TextComponent("Save And Quit", 10, "[vertical: center][horizontal: center]", 0, 0));

          //Options buttons
          optionsButton = new MenuItem("[type: menu][subType: paused][name: optionsButton]", "menuItem", background.treeNode, "[vertical: top][horizontal: center]", 0, start  +2*height, 4, 350, 40);
          optionsButton.addText( new TextComponent("Options", 10, "[vertical: center][horizontal: center]", 0, 0));

          //bottom buttons
          backButton = new MenuItem("[type: menu][subType: paused][name: backButton]", "menuItem", background.treeNode, "[vertical: top][horizontal: center]", 0, start  + 3*height, 4, 350, 40);
          backButton.addText( new TextComponent("Back", 10, "[vertical: center][horizontal: center]", 0, 0));
      }
      public void delete() { background.delete(); }
    };

    private PauseMenu pausemenu;

    public PauseGame() {
        type = "logic";
        toggle = false;
        pausemenu = null;
    }
    
    private void togglePause(boolean state) {

        if(state) {
            pausemenu = new PauseMenu();
        }

        else {
            pausemenu.delete();
            pausemenu = null;
        }
        
    }

    public void update(Entity entity) {

        //back button update =====================================================================
        if(pausemenu != null && pausemenu.backButton.isLeftClicked()) togglePause(false);
        if(InputAL.isKeyPressed("esc") && !toggle) toggle = true;
        if(!InputAL.isKeyPressed("esc") && toggle) {
            toggle = false;
            if(pausemenu == null) togglePause(true);
            else togglePause(false);
        }

        //Save and Quit ===========================================================================
        if(pausemenu != null && pausemenu.saveAndQuit.isLeftClicked()) {
            FileSystem.saveCurrentChunks();
            State.mainMenu();
        }
    }
}

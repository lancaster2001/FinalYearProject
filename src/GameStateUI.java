import java.awt.*;

public class GameStateUI {
    //singleton-------------------------------------------------------------------------
    private static GameStateUI instance = new GameStateUI();

    private GameStateUI() {
    }

    public static GameStateUI getInstance() {
        if (instance == null) {
            instance = new GameStateUI();
        }
        return instance;
    }

    //----------------------------------------------------------------------------------------
    private final GameUIBuildMenu GameUIBuildMenuInstance = GameUIBuildMenu.getInstance();
    private final GameUIResourceMenu gameUIResourceMenuInstance = GameUIResourceMenu.getInstance();

    public void drawUI(Graphics g) {
        gameUIResourceMenuInstance.drawResourcesMenu(g);
        GameUIBuildMenuInstance.drawBuildMenu(g);
    }

    public boolean takeInput(Point p) {
        boolean interacted = false;
        if (GameUIBuildMenuInstance.takeInput(p)) {
            interacted = true;
        } else if (gameUIResourceMenuInstance.takeInput(p)) {
            interacted = true;
        }
        return interacted;
    }


}

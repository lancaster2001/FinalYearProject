import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public final class MainPanel extends JPanel {
    //singleton-------------------------------------------------------------------------
    private static MainPanel instance = new MainPanel();

    private MainPanel() {
        this.setSize(gameConstants.screenSize.width, gameConstants.screenSize.height - 500);
        this.setMinimumSize(new Dimension(600, 600));
        this.setVisible(true);
        this.setOpaque(false);
    }

    public static MainPanel getInstance() {
        if (instance == null) {
            instance = new MainPanel();
        }
        return instance;
    }

    //----------------------------------------------------------------------------------------
    public ArrayList<MapSlot> mapArray = new ArrayList<MapSlot>();
    private final Camera cameraInstance = Camera.getInstance();
    private final GameStateDrawer gameStateDrawerInstance = GameStateDrawer.getInstance();
    private final StateManager StateManagerInstance = StateManager.getInstance();


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (StateManagerInstance.getCurrentState()!=null) {
            if (StateManagerInstance.getCurrentState().equals(gameConstants.STATE.GAME)) {
                gameStateDrawerInstance.draw(g);
            } else if (StateManagerInstance.getCurrentState().equals(gameConstants.STATE.STARTMENU)) {
                MenuState.getInstance().draw(g);
            }
        }
    }

}

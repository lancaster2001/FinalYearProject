import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public final class MainPanel extends JPanel{
    //singleton-------------------------------------------------------------------------
    private static MainPanel instance;
    private MainPanel(){
        this.setSize(gameConstants.screenSize.width, gameConstants.screenSize.height-200);
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
    private final GameStateUI gameStateUIInstance = GameStateUI.getInstance();
    private gameConstants.STATE state;

    @Override
    protected void paintComponent(Graphics g) {
        int numOslotsWide = cameraInstance.getNumOslotsWide();
        int numOslotsTall = cameraInstance.getNumOslotsTall();
        int widthOfSlot = cameraInstance.getwidthOfSlot();
        int heightOfslot = cameraInstance.getheightOfslot();
        super.paintComponent(g);
        GameStateDrawer.getInstance().drawMap(g,cameraInstance.getViewableMap(),numOslotsWide,numOslotsTall,widthOfSlot,heightOfslot);
        gameStateUIInstance.drawUI(g);
    }

}

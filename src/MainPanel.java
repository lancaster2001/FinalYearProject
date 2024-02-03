import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public final class MainPanel extends JPanel{
    //singleton-------------------------------------------------------------------------
    private static MainPanel instance = new MainPanel();
    private MainPanel(){
        this.setSize(gameConstants.screenSize.width, gameConstants.screenSize.height-500);
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
    private final  GameStateDrawer gameStateDrawerInstance = GameStateDrawer.getInstance();
    private gameConstants.STATE state;

    @Override
    protected void paintComponent(Graphics g) {
        int numOslotsWide = cameraInstance.getNumOslotsWide();
        int numOslotsTall = cameraInstance.getNumOslotsTall();
        int widthOfSlot = cameraInstance.getwidthOfSlot();
        int heightOfslot = cameraInstance.getheightOfslot();
        super.paintComponent(g);
        gameStateDrawerInstance.draw(g,cameraInstance.getViewableMap(),numOslotsWide,numOslotsTall,widthOfSlot,heightOfslot);
        fpsCounter(g);
    }
    private void fpsCounter(Graphics g){
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString( String.valueOf(GameState.getInstance().fpsCounter),0,50);
    }

}

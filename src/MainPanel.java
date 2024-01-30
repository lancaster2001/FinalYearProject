import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public final class MainPanel extends JPanel{
    //singleton-------------------------------------------------------------------------
    private static MainPanel instance;
    private MainPanel(){
        this.setSize(gameConstants.screenSize.width, gameConstants.screenSize.height);
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
        drawMap(g,cameraInstance.getViewableMap(),numOslotsWide,numOslotsTall,widthOfSlot,heightOfslot);
        gameStateUIInstance.drawUI(g);
    }

    public void setMapArray(ArrayList<MapSlot> givenArray){
        mapArray = givenArray;
    }
    private void drawMap(Graphics g,ArrayList<MapSlot> map,int numOslotsWide, int numOslotsTall, int widthOfSlot, int heightOfslot){
        drawTiles(g,map,numOslotsWide,numOslotsTall,widthOfSlot,heightOfslot);
        drawTowers(g,map,numOslotsWide,numOslotsTall,widthOfSlot,heightOfslot);
    }

    private void drawTiles(Graphics g,ArrayList<MapSlot> map,int numOslotsWide, int numOslotsTall, int widthOfSlot, int heightOfslot){
        for (int yIndex = 0;yIndex < numOslotsTall; yIndex++){
            for(int xIndex = 0;xIndex < numOslotsWide; xIndex++){
                int index = yIndex*numOslotsWide+xIndex;
                int x = xIndex*widthOfSlot;
                int y = yIndex*heightOfslot;
                g.drawImage(map.get(index).getTile().getImage(), x, y, widthOfSlot, heightOfslot, null);
            }
        }
    }
    private void drawTowers(Graphics g,ArrayList<MapSlot> map,int numOslotsWide, int numOslotsTall, int widthOfSlot, int heightOfslot){
        for (int yIndex = 0;yIndex < numOslotsTall; yIndex++){
            for(int xIndex = 0;xIndex < numOslotsWide; xIndex++){
                int index = yIndex*numOslotsWide+xIndex;
                int x = xIndex*widthOfSlot;
                int y = yIndex*heightOfslot;
                g.drawImage(map.get(index).getTower().image, x, y, widthOfSlot, heightOfslot, null);
            }
        }
    }
}

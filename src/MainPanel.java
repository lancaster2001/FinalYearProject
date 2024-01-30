import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public final class MainPanel extends JPanel {
    //singleton-------------------------------------------------------------------------
    private static MainPanel instance;
    private MainPanel(){
        System.out.println("make sure you only print this once for mainPanel");
        this.setSize(gameConstants.screenSize.width, gameConstants.screenSize.height);
        this.setMinimumSize(new Dimension(600, 600));
        this.setVisible(true);
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
    private gameConstants.STATE state;

    @Override
    protected void paintComponent(Graphics g) {
        mapArray = cameraInstance.getViewableMap();
        int numOslotsWide = cameraInstance.getNumOslotsWide();
        int numOslotsTall = cameraInstance.getNumOslotsTall();
        int widthOfSlot = cameraInstance.getwidthOfSlot();
        int heightOfslot = cameraInstance.getheightOfslot();
        super.paintComponent(g);
        drawMap(g,mapArray,numOslotsWide,numOslotsTall,widthOfSlot,heightOfslot);
    }

    public void setMapArray(ArrayList<MapSlot> givenArray){
        mapArray = givenArray;
    }
    private void drawMap(Graphics g,ArrayList<MapSlot> map,int numOslotsWide, int numOslotsTall, int widthOfSlot, int heightOfslot){
        for (int yIndex = 0;yIndex <= numOslotsTall; yIndex++){
            for(int xIndex = 0;xIndex <= numOslotsWide; xIndex++){
                int index = yIndex*numOslotsWide+xIndex;
                BufferedImage image = map.get(index).tower1.image;
                int x = xIndex*widthOfSlot;
                int y = yIndex*heightOfslot;
                g.drawImage(image, x, y, widthOfSlot, heightOfslot, null);
            }
        }
    }
}

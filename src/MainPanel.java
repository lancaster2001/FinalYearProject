import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public final class MainPanel extends JPanel {
    public ArrayList<MapSlot> mapArray = new ArrayList<MapSlot>();
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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawMap(g,mapArray);
    }

    public void setMapArray(ArrayList<MapSlot> givenArray){
        mapArray = givenArray;
    }
    private void drawMap(Graphics g,ArrayList<MapSlot> map){
        for (MapSlot slot : map)
            g.drawImage(slot.tower1.image,0,0,300,300,null);
    }
}

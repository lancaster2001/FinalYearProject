import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Map {
    public ArrayList<MapSlot> mapArray = new ArrayList<MapSlot>();
    private MainFrame frameInstance = MainFrame.getInstance();
    public MainPanel panelInstance;
    private Map(){
        for(int slotNumber = 0; slotNumber < (gameConstants.mapSize); slotNumber++){
            mapArray.add(new MapSlot());
        }
        frameInstance = MainFrame.getInstance();
        panelInstance = frameInstance.getPanelInstance();

    }
    public static void main(String[] args) {
        Map themap = new Map();
        themap.thething();

    }
    public void thething(){
        Timer gameTimer = new Timer();

        this.panelInstance.repaint();
        TimerTask task = new TimerTask() {
            public static int i = 0;
            @Override
            public void run() {
                System.out.println("Timer ran " + ++i);
                thething();
            }

        };
        gameTimer.schedule(task,500 );
    }
}

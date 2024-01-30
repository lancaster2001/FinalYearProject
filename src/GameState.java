import java.util.Timer;
import java.util.TimerTask;

public class GameState {
    //singleton------------------------------------------------------------------------
    private static GameState instance;
    public static GameState getInstance(){
        if (instance == null) {
            instance = new GameState();
        }
        return instance;
    }
    private GameState(){
        setup();
    }
    //----------------------------------------------------------------------------------

    private MainFrame frameInstance;
    public MainPanel panelInstance;
    private Map mapInstance = Map.getInstance();
    private Camera cameraInstance = Camera.getInstance();
    private void setup(){
        frameInstance = MainFrame.getInstance();
        panelInstance = frameInstance.getPanelInstance();
    }
    void gameLoop(){
        Timer gameTimer = new Timer();
        mapInstance.makeAllTowersAct();
        panelInstance.repaint();
        TimerTask task = new TimerTask() {
            public static int i = 0;
            @Override
            public void run() {
                System.out.println("Timer ran " + ++i);
                gameLoop();
            }

        };
        gameTimer.schedule(task,500 );
    }
}

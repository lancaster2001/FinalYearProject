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
    private GameState(){}
    //----------------------------------------------------------------------------------

    private final MainFrame frameInstance = MainFrame.getInstance();
    public final MainPanel panelInstance = frameInstance.getPanelInstance();
    private final Map mapInstance = Map.getInstance();
    private final Camera cameraInstance = Camera.getInstance();
    private final ResourceManager resourceManagerInstance = ResourceManager.getInstance();
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

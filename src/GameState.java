import java.awt.event.KeyEvent;
import java.util.ArrayList;
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
        actloop();
        screenRefresher();
        tickLoop();

    }
    //----------------------------------------------------------------------------------
    private final MainFrame frameInstance = MainFrame.getInstance();
    public final MainPanel panelInstance = frameInstance.getPanelInstance();
    private final Map mapInstance = Map.getInstance();
    private final Camera cameraInstance = Camera.getInstance();
    private final ResourceManager resourceManagerInstance = ResourceManager.getInstance();
    private final ArrayList<BaseEnemy> enemyList = new ArrayList<>();
    void actloop(){
        Timer actTimer = new Timer();
        mapInstance.act();
        TimerTask task = new TimerTask() {
            public static int i = 0;
            @Override
            public void run() {
                System.out.println("Timer ran " + ++i);
                actloop();
            }

        };
        actTimer.schedule(task,1000 );
    }
    private void screenRefresher(){
        Timer screenRefreshTimer = new Timer();
        panelInstance.repaint();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                screenRefresher();
            }
        };
        screenRefreshTimer.schedule(task,1000/30 );
    }
    private void tickLoop(){
        Timer tickTimer = new Timer();
        for(BaseEnemy enemy: enemyList){
            enemy.act();
        }
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                tickLoop();
            }
        };
        tickTimer.schedule(task,1000/30 );
    }
    public void userInput(KeyEvent e){
        if(e.getKeyChar() == gameConstants.moveCameraUp){
            cameraInstance.move(gameConstants.DIRECTION.UP);
        }else if(e.getKeyChar() == gameConstants.moveCameraDown){
            cameraInstance.move(gameConstants.DIRECTION.DOWN);
        }else if(e.getKeyChar() == gameConstants.moveCameraLeft){
            cameraInstance.move(gameConstants.DIRECTION.LEFT);
        }else if(e.getKeyChar() == gameConstants.moveCameraRight){
            cameraInstance.move(gameConstants.DIRECTION.RIGHT);
        }
    }

}

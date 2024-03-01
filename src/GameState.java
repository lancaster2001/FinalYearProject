import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GameState {
    //singleton------------------------------------------------------------------------
    private static GameState instance = new GameState();

    public static GameState getInstance() {
        if (instance == null) {
            instance = new GameState();
        }
        return instance;
    }

    private GameState() {
        TowerManager.getInstance();
        resourceManagerInstance.queryInventory("Rock").add(50);
    }

    //----------------------------------------------------------------------------------
    private int targettickRate = gameSettings.getInstance().getGameTickRate();
    private int tickRate = targettickRate;
    private final MainFrame frameInstance = MainFrame.getInstance();
    public final MainPanel panelInstance = frameInstance.getPanelInstance();
    private final Camera cameraInstance = Camera.getInstance();
    private final EnemyManager enemyManagerInstance = EnemyManager.getInstance();
    private final ProjectileManager projectileManagerInstance = ProjectileManager.getInstance();
    private final ResourceManager resourceManagerInstance = ResourceManager.getInstance();
    private final SaveHandler saveHandlerInstance = SaveHandler.getInstance();
    private Map mapInstance;
    public boolean paused = false;
    int fps = 0;
    int fpsCounter = 0;
    int tickAccumulator = 0;

    private boolean start = true;

    public void startup() {
        try {
            try {
                mapInstance = SaveHandler.getInstance().getMapInstance();
            }catch(Exception e){
                MenuState.getInstance().showErrorMessage("error getting map instance in gamestate");
                StateManager.getInstance().setCurrentState(gameSettings.STATE.STARTMENU);
            }
            cameraInstance.setPosition(cameraInstance.getX(), cameraInstance.getY());
            tickLoop();
            actLoop();
            screenRefresher();
            if (gameSettings.getInstance().isAutosave()) {
                saveLoop();
            }
            cameraInstance.setPosition((mapInstance.getMapWidth()/2)-(cameraInstance.getZoom()/2), (mapInstance.getMapHeight()/2)-(cameraInstance.getZoom()/2));
        }catch(Exception e){
            MenuState.getInstance().showErrorMessage("error starting up gamestate");
            StateManager.getInstance().setCurrentState(gameSettings.STATE.STARTMENU);
        }
        if(mapInstance==null){
            MenuState.getInstance().showErrorMessage("error loading the map instance\n in gamestate startup");
            StateManager.getInstance().setCurrentState(gameSettings.STATE.STARTMENU);
        }
    }
    private final Runnable actTask = () -> {
        System.out.println("Timer ran");
        if (!start) {
            tickLoop();
            start = true;
        }
        fpsCounter = fps;
        System.out.println("fps:" + fps);
        System.out.println("ticks/s:" + tickAccumulator);

        fps = 0;
        tickAccumulator = 0;
    };

    private final Runnable saveTask = () -> {
        System.out.println("save made");
        saveHandlerInstance.saveGame();
    };

    private final Runnable tickTask = () -> {
        if(!paused) {
            double tickMultiplier = tickRate / 1000.0;
            enemyManagerInstance.tick(tickMultiplier);
            projectileManagerInstance.tick(tickMultiplier);
            mapInstance.tick(tickMultiplier);
        }
            tickAccumulator++;

    };

    private final Runnable screenRefreshTask = () -> {
        panelInstance.repaint();
        fps += 1;
    };

    // ScheduledExecutorService for scheduling tasks
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(4);

    // Other code remains unchanged...

    private void actLoop() {
        // Schedule actTask to run every 1 second
        scheduler.scheduleAtFixedRate(actTask, 0, 1, TimeUnit.SECONDS);
    }

    private void saveLoop() {
        // Schedule saveTask to run every 30 seconds
        scheduler.scheduleAtFixedRate(saveTask, 0, 30, TimeUnit.SECONDS);
    }

    private void tickLoop() {
        // Schedule tickTask to run every tickRate milliseconds
        scheduler.scheduleAtFixedRate(tickTask, 0, tickRate, TimeUnit.MILLISECONDS);
    }

    private void screenRefresher() {
        scheduler.scheduleAtFixedRate(screenRefreshTask, 0, 1000 / 60, TimeUnit.MILLISECONDS);
    }

    public boolean userInput(KeyEvent e) {
        boolean check = true;
        if (e.getKeyChar() == gameSettings.getInstance().moveCameraUp) {
            cameraInstance.move(gameSettings.DIRECTION.UP);
        } else if (e.getKeyChar() == gameSettings.getInstance().moveCameraDown) {
            cameraInstance.move(gameSettings.DIRECTION.DOWN);
        } else if (e.getKeyChar() == gameSettings.getInstance().moveCameraLeft) {
            cameraInstance.move(gameSettings.DIRECTION.LEFT);
        } else if (e.getKeyChar() == gameSettings.getInstance().moveCameraRight) {
            cameraInstance.move(gameSettings.DIRECTION.RIGHT);
        } else {
            check = false;
        }
        return check;
    }

    public Rectangle2D.Double outOfBoundsCheck(Rectangle2D.Double section) {
        return mapInstance.sectionOutOfBoundsCheck(section, false);
    }

    public Map getMapInstance() {
        if (mapInstance != null) {
            return mapInstance;
        }
        return null;
    }

}

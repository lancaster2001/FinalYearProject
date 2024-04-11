import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public final class GameState {
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
    private int targetTickRate = GameSettings.getInstance().getGameTickRate();
    private int tickRate = targetTickRate;
    private final MainFrame frameInstance = MainFrame.getInstance();
    public final MainPanel panelInstance = frameInstance.getPanelInstance();
    private final Camera cameraInstance = Camera.getInstance();
    private final EnemyManager enemyManagerInstance = EnemyManager.getInstance();
    private final ProjectileManager projectileManagerInstance = ProjectileManager.getInstance();
    private final ResourceManager resourceManagerInstance = ResourceManager.getInstance();
    private final SaveHandler saveHandlerInstance = SaveHandler.getInstance();
    private Map mapInstance;
    //scheduler used to improve performance by allocating a pool of core (1 for each task)
    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(6);
    private boolean paused = false;
    private int fps = 0;
    private int tickAccumulator = 0;
    private boolean start = true;

    public void startup() {
        try {
            try {
                mapInstance = SaveHandler.getInstance().getMapInstance();
            }catch(Exception e){
                MenuState.getInstance().showErrorMessage("error getting map instance in gamestate");
                StateManager.getInstance().setCurrentState(GameSettings.STATE.STARTMENU);
            }
            cameraInstance.setPosition(cameraInstance.getX(), cameraInstance.getY());
            tickLoop();
            actLoop();
            screenRefresher();
            if (GameSettings.getInstance().isAutosave()) {
                saveLoop();
            }
            cameraInstance.setPosition((mapInstance.getMapWidth()/2)-(cameraInstance.getZoom()/2), (mapInstance.getMapHeight()/2)-(cameraInstance.getZoom()/2));
        }catch(Exception e){
            MenuState.getInstance().showErrorMessage("error starting up gamestate");
            StateManager.getInstance().setCurrentState(GameSettings.STATE.STARTMENU);
        }
        if(mapInstance==null){
            MenuState.getInstance().showErrorMessage("error loading the map instance\n in gamestate startup");
            StateManager.getInstance().setCurrentState(GameSettings.STATE.STARTMENU);
        }
    }

    //tasks for each timer that is setups
    //E. Paraschiv, “Java - Timer | Baeldung,” www.baeldung.com, Nov. 02, 2014. https://www.baeldung.com/java-timer-and-timertask
    private Runnable actTask = () -> {
        System.out.println("Timer ran");
        if (!start) {
            tickLoop();
            start = true;
        }
        System.out.println("fps:" + fps);
        System.out.println("ticks/s:" + tickAccumulator);

        fps = 0;
        tickAccumulator = 0;
    };

    private Runnable saveTask = () -> {
        System.out.println("save made");
        saveHandlerInstance.saveGame();
    };

    private Runnable tickTask = () -> {
        if(!paused) {
            double tickMultiplier = tickRate / 1000.0;
            enemyManagerInstance.tick(tickMultiplier);
            projectileManagerInstance.tick(tickMultiplier);
            mapInstance.tick(tickMultiplier);
        }
            tickAccumulator++;

    };

    private Runnable screenRefreshTask = () -> {
        panelInstance.repaint();
        fps += 1;
    };

    //timers
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


    //todo: move this to camera class
    public boolean userInput(KeyEvent e) {
        boolean check = true;
        if (e.getKeyChar() == GameSettings.getInstance().moveCameraUp) {
            cameraInstance.move(GameSettings.DIRECTION.UP);
        } else if (e.getKeyChar() == GameSettings.getInstance().moveCameraDown) {
            cameraInstance.move(GameSettings.DIRECTION.DOWN);
        } else if (e.getKeyChar() == GameSettings.getInstance().moveCameraLeft) {
            cameraInstance.move(GameSettings.DIRECTION.LEFT);
        } else if (e.getKeyChar() == GameSettings.getInstance().moveCameraRight) {
            cameraInstance.move(GameSettings.DIRECTION.RIGHT);
        } else {
            check = false;
        }
        return check;
    }

    public Map getMapInstance() {
        if (mapInstance != null) {
            return mapInstance;
        }
        return null;
    }
    public void togglePaused(){
        paused = !paused;
    }
}

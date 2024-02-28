import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.util.Timer;
import java.util.TimerTask;

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

    private void actLoop() {
        Timer actTimer = new Timer();
        TimerTask task = new TimerTask() {
            public static int i = 0;

            @Override
            public void run() {
                System.out.println("Timer ran " + ++i);
                if (!start) {
                    tickLoop();
                    start = true;
                }
                fpsCounter = fps;
                System.out.println("fps:"+fps);
                System.out.println("ticks/s:"+tickAccumulator);

                fps = 0;
                tickAccumulator = 0;
                actLoop();
            }
        };
        actTimer.schedule(task, 1000);
    }
    private void saveLoop() {
        Timer actTimer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                System.out.println("save made");
                saveHandlerInstance.saveGame();
                saveLoop();
            }
        };
        actTimer.schedule(task, 30000);
    }

    private void screenRefresher() {
        Timer screenRefreshTimer = new Timer();
        panelInstance.repaint();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                fps += 1;
                screenRefresher();
            }
        };
        screenRefreshTimer.schedule(task, 1000 / 30);
    }

    private void tickLoop() {
        Timer tickTimer = new Timer();
        if (!paused) {
            double tickMultiplier = tickRate / 1000.0;
            enemyManagerInstance.tick(tickMultiplier);
            projectileManagerInstance.tick(tickMultiplier);
            mapInstance.tick(tickMultiplier);
        }
        tickAccumulator+=1;
        TimerTask task = new TimerTask() {
            public static double i = 0;

            @Override
            public void run() {
                tickLoop();
                i += gameSettings.getInstance().getGameTickRate() / 1000.0;
            }
        };
        tickTimer.schedule(task, tickRate);
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

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
        BaseTower playerTower = BaseBaseTower.getInstance();
        mapInstance = getMapInstance();
        //mapInstance.setTower(playerTower,(int)playerTower.getPose().getX(),(int)playerTower.getPose().getY());
        resourceManagerInstance.queryInventory("Rock").add(20);
        getMapInstance();
    }

    //----------------------------------------------------------------------------------
    private int tickRate = gameConstants.gameTickRate;
    private final MainFrame frameInstance = MainFrame.getInstance();
    public final MainPanel panelInstance = frameInstance.getPanelInstance();
    private final MapGenerator mapGeneratorInstance = MapGenerator.getInstance();
    private final Camera cameraInstance = Camera.getInstance();
    private final EnemyManager enemyManagerInstance = EnemyManager.getInstance();
    private final ProjectileManager projectileManagerInstance = ProjectileManager.getInstance();
    private final ResourceManager resourceManagerInstance = ResourceManager.getInstance();
    private Map mapInstance = getMapInstance();
    public boolean paused = false;

    int fps = 0;
    int fpsCounter = 0;

    private boolean start = true;

    public void startup() {
        cameraInstance.setPosition(cameraInstance.getX(), cameraInstance.getY());
        tickLoop();
        actLoop();
        screenRefresher();
    }

    private void actLoop() {
        Timer actTimer = new Timer();
        //mapInstance.act();
        //enemyManagerInstance.randomEnemyChance();
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
                System.out.println(fps);
                fps = 0;
                actLoop();
            }
        };
        actTimer.schedule(task, 1000);
    }

    private void screenRefresher() {
        Timer screenRefreshTimer = new Timer();
        panelInstance.repaint();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                screenRefresher();
                fps += 1;
            }
        };
        screenRefreshTimer.schedule(task, 1000 / 60);
    }

    private void tickLoop() {
        Timer tickTimer = new Timer();
        if (!paused) {
            double tickMultiplier = tickRate / 1000.0;
            mapInstance.tick(tickMultiplier);
            enemyManagerInstance.tick(tickMultiplier);
            projectileManagerInstance.tick(tickMultiplier);
        }
        TimerTask task = new TimerTask() {
            public static double i = 0;

            @Override
            public void run() {
                tickLoop();
                i += gameConstants.gameTickRate / 1000.0;
            }
        };
        tickTimer.schedule(task, tickRate);
    }

    public boolean userInput(KeyEvent e) {
        boolean check = true;
        if (e.getKeyChar() == gameConstants.moveCameraUp) {
            cameraInstance.move(gameConstants.DIRECTION.UP);
        } else if (e.getKeyChar() == gameConstants.moveCameraDown) {
            cameraInstance.move(gameConstants.DIRECTION.DOWN);
        } else if (e.getKeyChar() == gameConstants.moveCameraLeft) {
            cameraInstance.move(gameConstants.DIRECTION.LEFT);
        } else if (e.getKeyChar() == gameConstants.moveCameraRight) {
            cameraInstance.move(gameConstants.DIRECTION.RIGHT);
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
        } else {
            mapInstance = mapGeneratorInstance.loadMap("src/Saves/Maps/map.json");
            if (mapInstance == null) {
                mapInstance = mapGeneratorInstance.createNewMap(gameConstants.mapWidth, gameConstants.mapHeight);
            }
        }
        return mapInstance;
    }
}

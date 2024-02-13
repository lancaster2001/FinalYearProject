import java.awt.*;

public class GameStateDrawer {
    //singleton-------------------------------------------------------------------------
    private static GameStateDrawer instance = new GameStateDrawer();

    private GameStateDrawer() {
    }

    public static GameStateDrawer getInstance() {
        if (instance == null) {
            instance = new GameStateDrawer();
        }
        return instance;
    }

    //----------------------------------------------------------------------------------------
    private final AssetManager assetManagerInstance = AssetManager.getInstance();
    private final Camera cameraInstance = Camera.getInstance();
    private final GameStateUI gameStateUIInstance = GameStateUI.getInstance();
    private final EnemyManager enemyManagerInstance = EnemyManager.getInstance();
    private final ProjectileManager projectileManagerInstance = ProjectileManager.getInstance();

    //private final Map mapInstance = GameState.getInstance().getMapInstance();
    public void draw(Graphics g) {
        cameraInstance.draw(g);
        ResourceManager.getInstance().draw(g, AssetManager.getInstance());
        enemyManagerInstance.drawEnemies(g);
        projectileManagerInstance.draw(g, cameraInstance, assetManagerInstance);
        gameStateUIInstance.drawUI(g);

    }
}

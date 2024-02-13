import java.awt.*;
import java.util.ArrayList;

public class ProjectileManager {
    //singleton------------------------------------------------------------------------
    private static ProjectileManager instance = new ProjectileManager();

    public static ProjectileManager getInstance() {
        if (instance == null) {
            instance = new ProjectileManager();
        }
        return instance;
    }

    private ProjectileManager() {

    }

    //----------------------------------------------------------------------------------
    private final AssetManager assetManagerInstance = AssetManager.getInstance();
    protected ArrayList<Bullet> bullets = new ArrayList<>();

    public void addBullet(double x, double y, double theta, String type, double range, BulletTemplate bulletTemplate) {
        bullets.add(new Bullet(x, y, theta, type, range, bulletTemplate));
    }

    public void tick(double tickMultiplier) {
        ArrayList<Integer> bulletsToRemove = new ArrayList<Integer>();
        for (Integer index = 0; index < bullets.size(); index++) {
            if (bullets.get(index).tick(tickMultiplier)) {
                bulletsToRemove.add(index);
            } else if (!bullets.get(index).getExists()) {
                bulletsToRemove.add(index);
            }
        }
        if (!bulletsToRemove.isEmpty()) {
            removeListOfIndexes(bulletsToRemove);
        }
    }

    private void removeListOfIndexes(ArrayList<Integer> indexesToRemove) {
        for (int toRemoveIndex = indexesToRemove.size() - 1; toRemoveIndex >= 0; toRemoveIndex--) {
            for (int bulletIndex = bullets.size() - 1; bulletIndex >= 0; bulletIndex--) {
                if (indexesToRemove.get(toRemoveIndex) == bulletIndex) {
                    bullets.remove(bulletIndex);
                    break;
                }
            }
        }
    }

    public void draw(Graphics g, Camera cameraInstance, AssetManager assetManagerInstance) {
        for (Bullet bullet : bullets) {
            if (bullet.onScreenCheck(cameraInstance)) {
                bullet.draw(g, cameraInstance, assetManagerInstance);
            }
        }
    }
}

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class BaseTurretTower extends BaseTower {
    private final boolean debugging = gameSettings.getInstance().isDebugging();
    private final EnemyManager enemyManagerInstance = EnemyManager.getInstance();
    private final ProjectileManager projectileManagerInstance = ProjectileManager.getInstance();
    protected double range;
    private double shootAccumulator = 0.0;
    private String BulletCostResource;
    private int BulletCostQuantity;
    private double cooldown;
    private BulletTemplate bullet;
    BaseEnemy previousEnemy;
    private double targetPreviousX = -1;
    private double targetPreviousY=-1;
    private Point2D.Double predictedEnemyPoint = new Point2D.Double(-1,-1);


    public BaseTurretTower(Pose pose, TowerTemplate template) {
        super(pose, template);
        this.range = template.getRange();
        this.cooldown = speed;
        this.bullet = template.getBulletTemplate();
        this.BulletCostQuantity = template.getBulletCostQuantity();
        this.BulletCostResource = template.getBulletCostResource();
        inventorySize = 10;
        for (double index = 0; index < 4; index += 1.0) {
            inputDirections.add(index * (Math.PI / 2));
        }
    }

    private boolean checkForEnemies(double tickMultiplier) {
         ArrayList<BaseEnemy> enemyList = enemyManagerInstance.getEnemyList();
        for (BaseEnemy enemy : enemyList) {
            if (range >= calculateDistantToTarget(enemy.getPose().getX(), enemy.getPose().getY())) {
                double x = enemy.getPose().getX() + (enemy.getWidth() / 2);
                double y = enemy.getPose().getY() + (enemy.getHeight() / 2);

                if ((targetPreviousY!=-1)&&(targetPreviousX!=-1)){
                    double enemySpeedx = enemy.getPose().getX()-targetPreviousX;
                    double enemySpeedy = enemy.getPose().getY()-targetPreviousY;
                    if((enemySpeedx!= 0)&&(enemySpeedy!=0)) {
                        double enemySpeedBase = Math.pow(Math.pow(enemySpeedx, 2) + Math.pow(enemySpeedy, 2), 0.5);
                        double distanceToTarget = calculateDistantToTarget(enemy.getPose().getX(), enemy.getPose().getY());
                        double predictedTimeBeforeImpact = distanceToTarget / (enemySpeedBase / tickMultiplier);
                        x = enemy.getPose().getX() + (enemySpeedx * predictedTimeBeforeImpact) + (enemy.getWidth() / 2);
                        y = enemy.getPose().getY() + (enemySpeedy * predictedTimeBeforeImpact) + (enemy.getHeight() / 2);
                        predictedEnemyPoint.x = x;
                        predictedEnemyPoint.y = y;
                    }
                }
                calculateDirectionToTarget(x, y);
                shoot();
                targetPreviousX = enemy.getPose().getX();
                targetPreviousY = enemy.getPose().getY();
                previousEnemy = enemy;
                return true;
            }
        }

        clearValues();
        return false;
    }

    private void calculateDirectionToTarget(double x, double y) {
        double atan2_x = x - pose.getX();
        double atan2_y = y - pose.getY();
        double rot1 = Math.atan2(atan2_y, atan2_x);
        pose.setTheta(rot1 + (Math.PI / 2));
    }

    private double calculateDistantToTarget(double x, double y) {
        return (Math.sqrt((Math.pow(pose.getX() - x, 2)) + (Math.pow(pose.getY() - y, 2))));
    }
    private void clearValues(){
        pose.setTheta(0);
        targetPreviousX = -1;
        targetPreviousY = -1;
    }

    public void tick(double tickMultiplier) {
        shootAccumulator += tickMultiplier;
        if (shootAccumulator >= cooldown) {
            shootAccumulator = cooldown;
        }
        if (checkForEnemies(tickMultiplier)) {
            shoot();
        }
    }

    public void shoot() {
        int counter = 0;
        for (Resource resource : inventory) {
            if (resource.getName().equalsIgnoreCase(BulletCostResource)) {
                counter += 1;
            }
        }
        if ((shootAccumulator == cooldown) && (counter >= BulletCostQuantity)) {
            shootAccumulator -= cooldown;
            int counter2 = BulletCostQuantity;
            for (int index = inventory.size() - 1; index >= 0; index--) {
                if ((inventory.get(index).getName().equalsIgnoreCase(BulletCostResource)) && (counter2 > 0)) {
                    counter2 -= 1;
                    inventory.remove(index);
                }
            }
            projectileManagerInstance.addBullet(pose.getX(), pose.getY(), pose.getTheta(), "player", range, bullet);
        }
    }

    public void draw(Graphics g, int x, int y, int slotWidth, int slotHeight, AssetManager assetManagerInstance) {
        super.draw(g,x,y,slotWidth,slotHeight,assetManagerInstance);
        if((predictedEnemyPoint.x != -1)&&(predictedEnemyPoint.y != -1)&&(debugging)) {
            g.setColor(Color.red);
            g.setFont(new Font("Arial", Font.BOLD, 10));
            g.drawString(predictedEnemyPoint.x + "", x, y + 20);
            g.drawString(predictedEnemyPoint.y + "", x, y + 30);
        }
    }
}

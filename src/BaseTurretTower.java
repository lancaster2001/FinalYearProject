import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class BaseTurretTower extends BaseTower {
    private final EnemyManager enemyManagerInstance = EnemyManager.getInstance();
    private final ProjectileManager projectileManagerInstance = ProjectileManager.getInstance();
    private double range;
    private double shootAccumulator = 0.0;
    private String BulletCostResource;
    private int BulletCostQuantity;
    private double cooldown;
    private int magsize;
    private int magazine;
    private BulletTemplate bullet;
    private double targetPreviousX = -1;
    private double targetPreviousY=-1;
    private Point2D.Double predictedEnemyPoint = new Point2D.Double(-1,-1);

    //constructor
    public BaseTurretTower(Pose pose, TowerTemplate template) {
        super(pose, template);
        this.range = template.getRange();
        this.cooldown = speed;
        this.bullet = template.getBulletTemplate();
        this.BulletCostQuantity = template.getBulletCostQuantity();
        this.BulletCostResource = template.getBulletCostResource();
        inventorySize = 5;
        for (double index = 0; index < 4; index += 1.0) {
            inputDirections.add(index * (Math.PI / 2));
        }
        magazine = 0;
        magsize = template.getMagSize();
    }

    //searches for enemies within range of the turret
    private boolean checkForEnemies(double tickMultiplier) {
        //this could be more efficient
        ArrayList<BaseEnemy> enemyList = enemyManagerInstance.getEnemyList();
        //check each enemy in the enemy manager
        for (BaseEnemy enemy : enemyList) {
            //if the enemy is within range of the turret, select that enemy at the target
            if (range >= calculateDistantToTarget(enemy.getPose().getX(), enemy.getPose().getY())) {
                double x = enemy.getPose().getX() + (enemy.getWidth() / 2);
                double y = enemy.getPose().getY() + (enemy.getHeight() / 2);
                //an attempt at getting the turret to predict where the enemy will be by the time the bullet reaches it
                //this doesn't fully work but was good enough without having to remake it which would have taken to much time
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
                //face target
                pose.setTheta(calculateDirectionToTarget(x, y));
                //shoot at target
                shoot();
                //set last know target location for the next enemy location prediction
                targetPreviousX = enemy.getPose().getX();
                targetPreviousY = enemy.getPose().getY();
                return true;
            }
        }
        //reset the target tracking data direction of the turret as no enemies are in range
        clearValues();
        return false;
    }

    //return the angle that a point is to something on the map
    private double calculateDirectionToTarget(double x, double y) {
        double atan2_x = x - pose.getX();
        double atan2_y = y - pose.getY();
        double rot1 = Math.atan2(atan2_y, atan2_x);
        return (rot1 + (Math.PI / 2));
    }

    //calculate distant from turret to a point
    private double calculateDistantToTarget(double x, double y) {
        return (Math.sqrt((Math.pow(pose.getX() - x, 2)) + (Math.pow(pose.getY() - y, 2))));
    }

    //reset the position and target tracking data of the turret
    private void clearValues(){
        pose.setTheta(0);
        targetPreviousX = -1;
        targetPreviousY = -1;
    }


    //method for what needs to be done each tick
    public void tick(double tickMultiplier) {
        shootAccumulator += tickMultiplier;
        if (shootAccumulator >= cooldown) {
            shootAccumulator = cooldown;
        }
        if (checkForEnemies(tickMultiplier)) {
            shoot();
        }
    }

    //checks that the conditions to shoot a bullet are met shoots if so
    public void shoot() {
        /*note: this would have been overhauled at some point as the base tower was intended to get a method
            for accepting resources into inventory based on what resources it was allowed to accept
            this would have ment that all resources in inventory would have been valid, however this was never implemented*/
        int counter = 0;
        if(magazine<=0){
            //check if there's any resources in inventory that can be used as ammo
            for (Resource resource : inventory) {
                if (resource.getName().equalsIgnoreCase(BulletCostResource)) {
                    counter += 1;
                }
            }
            //check that the cooldown is over and the magazine can be reloaded
            if ((shootAccumulator == cooldown) && (counter >= BulletCostQuantity)) {
                shootAccumulator -= cooldown;
                int counter2 = BulletCostQuantity;
                for (int index = inventory.size() - 1; index >= 0; index--) {
                    if ((inventory.get(index).getName().equalsIgnoreCase(BulletCostResource)) && (counter2 > 0)) {
                        counter2 -= 1;
                        inventory.remove(index);
                        magazine+=magsize;
                    }
                }
            }
        }
        //check the magazine has bullets and if so shoot
        if(magazine>0) {
            magazine -= 1;
            projectileManagerInstance.addBullet(pose.getX(), pose.getY(), pose.getTheta(), "player", range, bullet);
        }
    }

    //display turret on screen
    public void draw(Graphics g, Rectangle rectangle, AssetManager assetManagerInstance) {
        super.draw(g,rectangle,assetManagerInstance);
        if((predictedEnemyPoint.x != -1)&&(predictedEnemyPoint.y != -1)&&(GameSettings.getInstance().isDebugging())) {
            g.setColor(Color.red);
            g.setFont(new Font("Arial", Font.BOLD, 10));
            g.drawString(predictedEnemyPoint.x + "", rectangle.x, rectangle.y + 20);
            g.drawString(predictedEnemyPoint.y + "", rectangle.x, rectangle.y + 30);
        }
    }
}

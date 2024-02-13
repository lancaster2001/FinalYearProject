import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Bullet {
    private final EnemyManager enemyManagerInstance = EnemyManager.getInstance();
    private boolean exists = true;
    private String type;
    private Pose pose;
    private double moveSpeed;
    private double damage;
    private double width;
    private double height;
    private double totalDistance = 0;
    private double range;
    private String imageLink = "src/Towers/Tower1/irondrill.png";

    Bullet(double x, double y, double theta, String type, double range, BulletTemplate template) {
        pose = new Pose(x, y, theta);
        this.moveSpeed = template.moveSpeed;
        this.damage = template.damage;
        this.width = template.width;
        this.height = template.height;
        this.imageLink = template.imageLink;
        this.type = type;
    }

    protected boolean tick(double tickMultiplier) {
        makeMovement(tickMultiplier);
        if (type.equalsIgnoreCase("enemy")) {
            return checkForTowerCollision();
        } else {
            return checkForEnemyCollision();
        }
    }

    private void makeMovement(double tickMultiplier) {
        if (totalDistance >= range) {
            exists = false;
        } else {
            double distanceToTravel = moveSpeed * tickMultiplier;
            double targetX = (distanceToTravel * Math.cos(pose.getTheta() - (Math.PI / 2)));
            double targetY = (distanceToTravel * Math.sin(pose.getTheta() - (Math.PI / 2)));
            pose.setX(pose.getX() + targetX);
            pose.setY(pose.getY() + targetY);
            totalDistance += distanceToTravel;
        }
    }

    private boolean checkForEnemyCollision() {
        ArrayList<BaseEnemy> enemyList = enemyManagerInstance.getEnemyList();
        for (BaseEnemy enemy : enemyList) {
            int numberOfSigFigs = 5;
            int enemyX = (int) (enemy.getPose().getX() * 100000.0);
            int enemyY = (int) (enemy.getPose().getY() * 100000.0);
            int enemyWidth = (int) ((enemy.getWidth()) * 100000.0);
            int enemyHeight = (int) ((enemy.getHeight()) * 100000.0);
            Rectangle enemyHitBox = new Rectangle(enemyX, enemyY, enemyWidth, enemyHeight);
            int x = (int) (this.pose.getX() * 100000.0);
            int y = (int) (this.pose.getY() * 100000.0);
            int width = (int) (this.width * 100000.0);
            int height = (int) (this.height * 100000.0);
            if (!enemyHitBox.intersection(new Rectangle(x, y, width, height)).isEmpty()) {
                enemy.takeDamage(damage);
                return true;
            }
        }
        return false;
    }

    private boolean checkForTowerCollision() {
        Rectangle2D.Double slotToCheck = new Rectangle2D.Double(pose.getX(), pose.getY(), width, height);
        ArrayList<MapSlot> intersectedSlots = GameState.getInstance().getMapInstance().getMapSection(slotToCheck);
        for (MapSlot slot : intersectedSlots) {
            if (slot.getTower() != null) {
                slot.damageTower(damage);
                return true;
            }
        }
        return false;
    }

    public void draw(Graphics g, Camera cameraInstance, AssetManager assetManagerInstance) {
        int x = cameraInstance.getOnScreenX(pose.getX());
        int y = cameraInstance.getOnScreenY(pose.getY());
        int width = (int) (this.width * cameraInstance.getwidthOfslot(pose.getX()));
        int height = (int) (this.height * cameraInstance.getheightOfslot(pose.getY()));
        //g.drawImage(assetManagerInstance.getImage(imageLink),x, y, width, height,null);
        double rotationRequired = pose.getTheta();
        BufferedImage image = AssetManager.getInstance().getImage("Projectiles", imageLink);

        Graphics2D g2d = (Graphics2D) g;
        AffineTransform backup = g2d.getTransform();
        AffineTransform trans = new AffineTransform();
        trans.rotate(rotationRequired, (x + (width / 2)), (y + (height / 2))); // the points to rotate around (the center in my example, your left side for your problem)
        g2d.transform(trans);
        g2d.drawImage(image, x, y, width, height, null);  // the actual location of the sprite
        g2d.setTransform(backup);
    }

    public boolean onScreenCheck(Camera cameraInstance) {
        return cameraInstance.isOnCamera((int) pose.getX(), (int) pose.getY());
    }

    public String getImageLink() {
        return imageLink;
    }

    public boolean getExists() {
        return exists;
    }

}

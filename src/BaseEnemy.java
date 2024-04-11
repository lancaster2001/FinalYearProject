import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public final class BaseEnemy {
    private Camera cameraInstance = Camera.getInstance();
    private final ProjectileManager projectileManagerInstance = ProjectileManager.getInstance();
    private boolean alive = true;
    private Pose pose = new Pose();
    private double width;//relative to size of slot
    private double height;//relative to size of slot
    private double maxHealth;
    private double health;
    private double moveSpeed;
    private double range;
    private double shootAccumulator = 0.0;
    private double cooldown = 1.0;
    private BulletTemplate bullet;
    private String imageLink = "src/Assets/Units/enemy1.png";
    private Point2D.Double targetLocation = new Point2D.Double(50, 50);
    private final Point2D.Double playerBasePoint = new Point2D.Double(GameState.getInstance().getMapInstance().getBasePose().getX(),GameState.getInstance().getMapInstance().getBasePose().getX());

    public BaseEnemy(Pose pose, EnemyTemplate template) {
        this.pose = pose;
        this.width = template.getWidth();
        this.height = template.getHeight();
        this.maxHealth = template.getMaxHealth();
        this.health = maxHealth;
        this.moveSpeed = template.getMoveSpeed();
        this.imageLink = template.getImageLink();
        this.range = template.getRange();
        this.cooldown = template.getCooldown();
        this.bullet = template.getBullet();
    }

    public void draw(Graphics g) {
        int x = cameraInstance.getOnScreenX(pose.getX());
        int y = cameraInstance.getOnScreenY(pose.getY());
        int width = (int) (cameraInstance.getwidthOfslot(pose.getX()) * this.width);
        int height = (int) (cameraInstance.getheightOfslot(pose.getY()) * this.height);
        BufferedImage image = AssetManager.getInstance().getImage("Units", imageLink);
        double rotationRequired = pose.getTheta();
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform backup = g2d.getTransform();
        AffineTransform trans = new AffineTransform();
        trans.rotate(rotationRequired, (x + (width / 2)), (y + (height / 2))); // the points to rotate around (the center in my example, your left side for your problem)
        g2d.transform(trans);
        g2d.drawImage(image, x, y, width, height, null);  // the actual location of the sprite
        g2d.setTransform(backup);
        drawHealthBar(g, new Rectangle(x, y, width, height));
    }

    private void drawHealthBar(Graphics g, Rectangle hitbox) {
        if (maxHealth > health) {
            int height = hitbox.height / 10;
            g.setColor(Color.white);
            g.drawRect(hitbox.x, hitbox.y - height, hitbox.width, height);
            g.setColor(Color.red);
            g.drawRect(hitbox.x, hitbox.y - height, (int) ((health / maxHealth) * hitbox.width), height);
        }
    }

    public void tick(double tickMultiplier) {
        if (alive) {
            shootAccumulator += tickMultiplier;
            if (shootAccumulator >= cooldown) {
                shootAccumulator = cooldown;
            }
            calculateDirection();
            makeMovement(tickMultiplier);
        }
    }

    private void calculateDirection() {
        pose.setTheta(calculateDirectionToTarget());
    }
    //calculate the angle the enemy needs to be at to face its target
    private double calculateDirectionToTarget() {
        double atan2_x = (targetLocation.x) - pose.getX();
        double atan2_y = (targetLocation.y) - pose.getY();
        double rot1 = Math.atan2(atan2_y, atan2_x);
        return (Math.PI / 2) + rot1;
    }
    /*method to move the enemy to where it should be based on tick multiplier so that if the ticks per second is decreased
    the enemy's movements shouldn't be affected*/
    private void makeMovement(double tickMultiplier) {
        double directionToTarget = calculateDirectionToTarget();
        double distanceToTravel = moveSpeed * tickMultiplier;
        double targetX = (distanceToTravel * Math.sin(directionToTarget));
        double targetY = (distanceToTravel * Math.sin(directionToTarget));
        MapSlot check = checkForCollision(pose.getX() + targetX, pose.getY() + targetY);

        if (check == null) {
            targetLocation = playerBasePoint;
            pose.setX(pose.getX() + targetX);
            pose.setY(pose.getY() + targetY);
        } else {
            targetLocation = new Point2D.Double(check.getX() + 0.5, check.getY() + 0.5);
            calculateDirection();
            shoot();
        }
        onScreenCheck();
        outOfBoundsCheck();
    }
    //check for collision with any obstacles on the map
    private MapSlot checkForCollision(double x, double y) {
        Rectangle2D.Double slotToCheck = new Rectangle2D.Double(x, y, width, height);
        ArrayList<MapSlot> intersectedSlots = GameState.getInstance().getMapInstance().getMapSection(slotToCheck);
        for (MapSlot slot : intersectedSlots) {
            if (slot.getTower() != null) {
                return slot;
            }
        }
        return null;
    }

    private void shoot() {
        if (shootAccumulator == cooldown) {
            shootAccumulator -= cooldown;
            projectileManagerInstance.addBullet(pose.getX(), pose.getY(), pose.getTheta(), "enemy", range, bullet);
        }
    }
    //check the enemy is within the bounds of the map
    private void outOfBoundsCheck() {
        Rectangle2D.Double placement = GameState.getInstance().getMapInstance().sectionOutOfBoundsCheck(new Rectangle2D.Double(pose.getX(), pose.getY(), 1, 1), true);
        pose.setX(placement.x);
        pose.setY(placement.y);
        width = placement.width;
        height = placement.height;
    }
    //check if enemy should be on screen
    public boolean onScreenCheck() {
        return cameraInstance.isOnCamera((int) pose.getX(), (int) pose.getY());
    }

    public void takeDamage(double damage) {
        health -= damage;
        if (health <= 0) {
            alive = false;
        }
    }

    public Pose getPose() {
        return pose;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public boolean getAlive() {
        return alive;
    }
}

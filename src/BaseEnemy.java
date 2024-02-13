import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class BaseEnemy {
    private Camera cameraInstance = Camera.getInstance();
    private final ProjectileManager projectileManagerInstance = ProjectileManager.getInstance();
    private boolean alive = true;
    protected Pose pose = new Pose();
    protected double width;//relative to size of slot
    protected double height;//relative to size of slot
    protected double damage;
    protected double maxHealth;
    protected double health;
    protected double moveSpeed;
    private double range;
    private double shootAccumulator = 0.0;
    private double cooldown = 1.0;
    private BulletTemplate bullet;
    protected String imageLink = "src/Assets/Units/enemy1.png";
    private Point2D.Double targetLocation = new Point2D.Double(50, 50);
    private final Point2D.Double playerBasePoint = new Point2D.Double((int) BaseBaseTower.getInstance().getPose().getX(), (int) BaseBaseTower.getInstance().getPose().getY());

    public BaseEnemy(Pose pose, EnemyTemplate template) {
        this.pose = pose;
        width = template.getWidth();
        height = template.getHeight();
        damage = template.getDamage();
        maxHealth = template.getMaxHealth();
        health = maxHealth;
        moveSpeed = template.getMoveSpeed();
        imageLink = template.getImageLink();
        range = template.getRange();
        cooldown = template.getCooldown();
        bullet = template.getBullet();
    }

    protected void draw(Graphics g) {
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

        //g.drawImage(AssetManager.getInstance().getImage("Units",imageLink), x, y, width, height, null);
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

    protected void tick(double tickMultiplier) {
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

    private double calculateDirectionToTarget() {
        double atan2_x = (targetLocation.x) - pose.getX();
        double atan2_y = (targetLocation.y) - pose.getY();
        double rot1 = Math.atan2(atan2_y, atan2_x);
        return (Math.PI / 2) + rot1;
    }

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

    public void shoot() {
        if (shootAccumulator == cooldown) {
            shootAccumulator -= cooldown;
            projectileManagerInstance.addBullet(pose.getX(), pose.getY(), pose.getTheta(), "enemy", range, bullet);
        }
    }

    private void outOfBoundsCheck() {
        Rectangle2D.Double placement = GameState.getInstance().getMapInstance().sectionOutOfBoundsCheck(new Rectangle2D.Double(pose.getX(), pose.getY(), 1, 1), true);
        pose.setX(placement.x);
        pose.setY(placement.y);
        width = placement.width;
        height = placement.height;
    }

    public boolean onScreenCheck() {
        return cameraInstance.isOnCamera((int) pose.getX(), (int) pose.getY());
    }

    public void takeDamage(double damage) {
        health -= damage;
        if (health <= 0) {
            alive = false;
        }
    }

    public void setPosition(double x, double y) {
        pose.setX(x);
        pose.setY(y);

    }

    public Rectangle2D.Double getMapPlacement() {
        return new Rectangle2D.Double(pose.getX(), pose.getY(), width, height);
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

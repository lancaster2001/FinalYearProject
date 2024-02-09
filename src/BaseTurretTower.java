import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class BaseTurretTower extends BaseTower{
    private final EnemyManager enemyManagerInstance = EnemyManager.getInstance();
    private final ProjectileManager projectileManagerInstance = ProjectileManager.getInstance();
    protected double range = 0.0;
    private double shootAccumulator = 0.0;
    private double cooldown = 1.0;
    private BulletTemplate bullet;
    private double targetX;
    private double targetY;
    public BaseTurretTower(Pose pose,TowerTemplate template){
        super(pose,template);
        this.range = template.getRange();
        this.cooldown = template.getCooldown();
        this.bullet = template.getBulletTemplate();
    }

    private boolean checkForEnemies(){
        ArrayList<BaseEnemy> enemyList = enemyManagerInstance.getEnemyList();
        for(BaseEnemy enemy: enemyList){
            if (range>= calculateDistantToTarget(enemy.getPose().getX(),enemy.getPose().getY())){
                double x = enemy.getPose().getX()+(enemy.getWidth()/2);
                double y = enemy.getPose().getY()+(enemy.getHeight()/2);
                calculateDirectionToTarget(x,y);
                shoot();
                return true;
            }
        }
        pose.setTheta(0);
        return false;
    }
    private void calculateDirectionToTarget(double x, double y){
        double atan2_x =x-pose.getX();
        double atan2_y =y-pose.getY();
        double rot1 = Math.atan2(atan2_y,atan2_x);
        pose.setTheta(rot1+(Math.PI/2));
    }
    private double calculateDistantToTarget(double x, double y){
        return (Math.sqrt((Math.pow(pose.getX() - x,2))+(Math.pow(pose.getY()-y,2))));
    }
    public void tick(double tickMultiplier){
        shootAccumulator+=tickMultiplier;
        if (shootAccumulator >= cooldown) {
            shootAccumulator = cooldown;
        }
        if(checkForEnemies()) {
            shoot();
        }
    }
    public void setup(int x,int y){
        pose = new Pose(x,y,0.0);
    }
    public void shoot(){
        if(shootAccumulator== cooldown) {
            shootAccumulator -= cooldown;
            projectileManagerInstance.addBullet(pose.getX(), pose.getY(),pose.getTheta(), "player" ,bullet);
        }
    }
    public void draw(Graphics g,int x, int y, int slotWidth ,int slotHeight,AssetManager assetManagerInstance){
        int width = (int)(this.width * slotWidth);
        int height = (int)(this.height * slotHeight);
        Rectangle towerBox = new Rectangle(x, y, width, height);
        // Rotation information
        double rotationRequired = pose.getTheta();
        BufferedImage image = AssetManager.getInstance().getImage("Towers",imageLink);

        Graphics2D g2d = (Graphics2D) g;
        AffineTransform backup = g2d.getTransform();
        AffineTransform trans = new AffineTransform();
        trans.rotate( rotationRequired, (x+(width/2)), (y+(height/2)) ); // the points to rotate around (the center in my example, your left side for your problem)
        g2d.transform( trans );
        g2d.drawImage( image, x, y ,width,height,null);  // the actual location of the sprite
        g2d.setTransform( backup ); // restore previous transform
        drawHealthBar(g, towerBox);
    }
}

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Bullet {
    private final EnemyManager enemyManagerInstance = EnemyManager.getInstance();
    Pose pose;
    double moveSpeed;
    double damage;
    double width;
    double height;
    String imageLink = "src/Towers/Tower1/image.png";
    Bullet(double x, double y, double theta, BulletTemplate template) {
        pose = new Pose(x,y,theta);
        this.moveSpeed = template.moveSpeed;
        this.damage = template.damage;
        this.width = template.width;
        this.height = template.height;
        this.imageLink =template.imageLink;
    }
    protected boolean tick(double tickMultiplier){
        makeMovement(tickMultiplier);
        return checkForCollision();
    }
    private void makeMovement(double tickMultiplier){
        //int distanceToTarget = (int)Math.sqrt((((pose.getX()-targetLocation.x)^2)+((pose.getY()-targetLocation.y)^2)));
        double distanceToTravel = moveSpeed*tickMultiplier;
        double targetX = (distanceToTravel * Math.cos(pose.getTheta()-(Math.PI/2)));
        double targetY = (distanceToTravel * Math.sin(pose.getTheta()-(Math.PI/2)));
        pose.setX(pose.getX()+targetX);
        pose.setY(pose.getY()+targetY);
    }
    private boolean checkForCollision(){
        ArrayList<BaseEnemy> enemyList =enemyManagerInstance.getEnemyList();
        for(BaseEnemy enemy: enemyList){
            int numberOfSigFigs = 5;
            int enemyX = (int)(enemy.getPose().getX()* 100000.0);
            int enemyY = (int)(enemy.getPose().getY()* 100000.0);
            int enemyWidth = (int)((enemy.getWidth())* 100000.0);
            int enemyHeight = (int)((enemy.getHeight())* 100000.0);
            Rectangle enemyHitBox = new Rectangle(enemyX,enemyY,enemyWidth,enemyHeight);
            int x =(int)(this.pose.getX()* 100000.0);
            int y = (int)(this.pose.getY()* 100000.0);
            int width = (int)(this.width* 100000.0);
            int height = (int)(this.height* 100000.0);
            if(!enemyHitBox.intersection(new Rectangle(x,y,width,height)).isEmpty()){
                enemy.takeDamage(damage);
                return true;
            }

        }
        return false;
    }
    public void draw(Graphics g,Camera cameraInstance, AssetManager assetManagerInstance){
        int widthOfSlot = cameraInstance.getwidthOfSlot();
        int heightOfSlot = cameraInstance.getheightOfslot();
        int x = (int)((this.pose.getX()-cameraInstance.getX())*widthOfSlot);
        int y = (int)((this.pose.getY()-cameraInstance.getY())*heightOfSlot);
        int width = (int)(this.width*widthOfSlot);
        int height = (int)(this.height*heightOfSlot);
        //g.drawImage(assetManagerInstance.getImage(imageLink),x, y, width, height,null);
        double rotationRequired = pose.getTheta();
        BufferedImage image = AssetManager.getInstance().getImage(imageLink);

        Graphics2D g2d = (Graphics2D) g;
        AffineTransform backup = g2d.getTransform();
        AffineTransform trans = new AffineTransform();
        trans.rotate( rotationRequired, (x+(width/2)), (y+(height/2)) ); // the points to rotate around (the center in my example, your left side for your problem)
        g2d.transform( trans );
        g2d.drawImage( image, x, y ,width,height,null);  // the actual location of the sprite
        g2d.setTransform( backup );
    }
    public boolean onScreenCheck(Camera cameraInstance){
        return cameraInstance.isOnCamera((int)pose.getX(),(int)pose.getY());
    }
    public String getImageLink() {
        return imageLink;
    }
}

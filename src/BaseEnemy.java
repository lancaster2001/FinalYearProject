import java.awt.*;
import java.awt.geom.Rectangle2D;

public abstract class BaseEnemy {
    private Camera cameraInstance = Camera.getInstance();
    private boolean alive = true;
    protected Pose pose = new Pose();
    protected double width = 1;//relative to size of slot
    protected double height = 1;//relative to size of slot
    protected double damage = 1;
    protected double maxHealth = 5;
    protected double health = maxHealth;
    protected double moveSpeed = 2.5;
    protected String imageLink = "src/Enemies/Base/image.png";
    //private final Map mapInstance = Map.getInstance();
    private Point targetLocation = new Point(50,50);

    protected void draw(Graphics g){
        int widthOfSlot = cameraInstance.getwidthOfSlot();
        int heightOfSlot = cameraInstance.getheightOfslot();
        int x = (int)((pose.getX()-cameraInstance.getX())*widthOfSlot);
        int y =(int)((pose.getY()-cameraInstance.getY())*heightOfSlot);
        int width =(int)(cameraInstance.getwidthOfSlot()*this.width);
        int height = (int)(cameraInstance.getheightOfslot()*this.height);
        g.drawImage(AssetManager.getInstance().getImage(imageLink), x, y, width, height, null);
        drawHealthBar(g,new Rectangle(x,y,width,height));
    }
    private void drawHealthBar(Graphics g, Rectangle hitbox){
        if(maxHealth > health){
            int height = hitbox.height/10;
            g.setColor(Color.white);
            g.drawRect(hitbox.x,hitbox.y-height,hitbox.width,height);
            g.setColor(Color.red);
            g.drawRect(hitbox.x,hitbox.y-height,(int)((health/maxHealth)*hitbox.width),height);
        }
    }
    protected void tick(double tickMultiplier){
        if(alive) {
            searchForTarget();
            calculateDirection();
            makeMovement(tickMultiplier);
        }
    }
    private void calculateDirection(){
        pose.setTheta(calculateDirectionToTarget());
    }
    private double calculateDirectionToTarget(){
        double atan2_x =((double)targetLocation.x)-pose.getX();
        double atan2_y =((double)targetLocation.y)-pose.getY();
        double rot1 = Math.atan2(atan2_y,atan2_x);
        return rot1;
    }
    private void searchForTarget(){
        targetLocation = new Point(50,50);
    }
    private void makeMovement(double tickMultiplier){
        double distanceToTravel = moveSpeed*tickMultiplier;
        double directionToTarget = calculateDirectionToTarget();
        double targetX = (distanceToTravel * Math.sin(directionToTarget));
        double targetY = (distanceToTravel * Math.sin(directionToTarget));
        pose.setX(pose.getX()+targetX);
        pose.setY(pose.getY()+targetY);
        onScreenCheck();
        outOfBoundsCheck();
    }
    private void outOfBoundsCheck(){
            Rectangle2D.Double placement =GameState.getInstance().getMapInstance().sectionOutOfBoundsCheck(new Rectangle2D.Double(pose.getX(), pose.getY(), 1,1),true);
            pose.setX(placement.x);
            pose.setY(placement.y);
            width = placement.width;
            height = placement.height;
    }
    public boolean onScreenCheck(){
        return cameraInstance.isOnCamera((int)pose.getX(),(int)pose.getY());
    }
    public void takeDamage(double damage){
        health-=damage;
        if(health<=0){
            alive = false;
        }
    }
    public void setPosition(double x, double y){
        pose.setX(x);
        pose.setX(y);

    }
    public Rectangle2D.Double getMapPlacement(){
        return new Rectangle2D.Double(pose.getX(),pose.getY(),width,height);
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
    public boolean getAlive(){return alive;}
}

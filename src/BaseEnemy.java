import java.awt.*;
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
    private final Map mapInstance = Map.getInstance();
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
        //int distanceToTarget = (int)Math.sqrt((((pose.getX()-targetLocation.x)^2)+((pose.getY()-targetLocation.y)^2)));
        double distanceToTravel = moveSpeed*tickMultiplier;
        double directionToTarget = calculateDirectionToTarget();
        double targetX = (distanceToTravel * Math.sin(directionToTarget));
        double targetY = (distanceToTravel * Math.sin(directionToTarget));
        pose.setX(pose.getX()+targetX);
        pose.setY(pose.getY()+targetY);
        onScreenCheck();
    }
    private void move(gameConstants.DIRECTION direction,int speed){
        switch(direction){
            case UP:
                pose.setY(pose.getY()-speed);
            case DOWN:
                pose.setY(pose.getY()+speed);
            case LEFT:
                pose.setX(pose.getX()-speed);
            case RIGHT:
                pose.setX(pose.getX()+speed);
        }
        outOfBoundsCheck();
    }
    private void outOfBoundsCheck(){
        boolean outOfBounds = true;
        while(outOfBounds){
            gameConstants.DIRECTION directionOutOfBounds = mapInstance.sectionOutOfBoundsCheck((int)pose.getX(), (int)pose.getY(), 1,1);
            if (directionOutOfBounds == gameConstants.DIRECTION.NULL){
                outOfBounds=false;
            }else if (directionOutOfBounds == gameConstants.DIRECTION.RIGHT){
                pose.setX(pose.getX()-1);
            }else if (directionOutOfBounds == gameConstants.DIRECTION.LEFT){
                pose.setX(pose.getX()+1);
            }else if (directionOutOfBounds == gameConstants.DIRECTION.UP){
                pose.setY(pose.getY()+1);
            }else if (directionOutOfBounds == gameConstants.DIRECTION.DOWN){
                pose.setY(pose.getY()-1);
            }else{
                System.out.println("error at out of bounds check in camera");
                outOfBounds = false;
            }
        }
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

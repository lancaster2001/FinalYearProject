import java.awt.*;
public abstract class BaseEnemy {
    protected Pose pose = new Pose();
    protected double width = 1;//relative to size of slot
    protected double height = 1;//relative to size of slot
    protected int damage = 1;
    protected int health = 5;
    protected double moveSpeed = 2.5;
    protected String imageLink = "src/Enemies/Base/image.png";
    private final Map mapInstance = Map.getInstance();
    private Point targetLocation = new Point(50,50);
    private boolean isOnScreen = false;

    protected void draw(Graphics g){
        Camera cameraInstance = Camera.getInstance();
        int x = (int)((pose.getX()-cameraInstance.getX())*cameraInstance.getwidthOfSlot());
        int y =(int)((pose.getY()-cameraInstance.getY())*cameraInstance.getheightOfslot());
        int width =(int)(cameraInstance.getwidthOfSlot()*this.width);
        int height = (int)(cameraInstance.getheightOfslot()*this.height);
        g.drawImage(AssetManager.getInstance().getImage(imageLink), x, y, width, height, null);
    }
    protected void tick(double tickMultiplier){
        searchForTarget();
        calculateDirection();
        makeMovement(tickMultiplier);
        onScreenCheck();
    }
    private void calculateDirection(){
        pose.setTheta(calculateDirectionToTarget());
    }
    private double calculateDirectionToTarget(Point target){
        double atan2_x =((double)target.x)-pose.getX();
        double atan2_y =((double)target.y)-pose.getY();
        double rot1 = Math.atan2(atan2_y,atan2_x)-pose.getTheta();
        return rot1;
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
        /*todo temporary idea likely wont use, this idea entails using a timer to make the enemy appear as though its sliding
        int xDifference = Math.abs(pose.getX()-targetX);
        int yDifference = Math.abs(pose.getY()- targetX);
        Timer tickTimer = new Timer();
        for(BaseEnemy enemy: enemyList){
            enemy.act(1000/tickRate);
        }
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                tickLoop();
            }
        };
        tickTimer.schedule(task,tickRate );*/


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
    public void onScreenCheck(){
        isOnScreen = Camera.getInstance().isOnCamera((int)pose.getX(),(int)pose.getY());
    }
    public String getImageLink() {
        return imageLink;
    }
    public boolean getIsOnScreen(){
        return isOnScreen;
    }
}

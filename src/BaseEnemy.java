import java.awt.*;

public abstract class BaseEnemy {
    protected int x = 0;
    protected int y = 0;
    private Pose pose = new Pose();
    protected int width = 100;//in pixels
    protected int height = 100;//in pixels
    protected int damage = 1;
    protected int health = 5;
    protected double moveSpeed = 1.0;
    protected String imageLink = "src/Enemies/Base/image.png";
    private final Map mapInstance = Map.getInstance();

    protected void draw(Graphics g){
        g.drawImage(AssetManager.getInstance().getImage(imageLink), x, y, width, height, null);
    }
    protected void act(){

    }
    private void move(gameConstants.DIRECTION direction,int speed){
        switch(direction){
            case UP:
                y-=speed;
            case DOWN:
                y+=speed;
            case LEFT:
                x-=speed;
            case RIGHT:
                x+=speed;
        }
        outOfBoundsCheck();
    }
    private void outOfBoundsCheck(){
        boolean outOfBounds = true;
        while(outOfBounds){
            gameConstants.DIRECTION directionOutOfBounds = mapInstance.sectionOutOfBoundsCheck(x,y,width,height);
            if (directionOutOfBounds == gameConstants.DIRECTION.NULL){
                outOfBounds=false;
            }else if (directionOutOfBounds == gameConstants.DIRECTION.RIGHT){
                x-=1;
            }else if (directionOutOfBounds == gameConstants.DIRECTION.LEFT){
                x+=1;
            }else if (directionOutOfBounds == gameConstants.DIRECTION.UP){
                y+=1;
            }else if (directionOutOfBounds == gameConstants.DIRECTION.DOWN){
                y-=1;
            }else{
                System.out.println("error at out of bounds check in camera");
                outOfBounds = false;
            }
        }
    }
    public String getImageLink() {
        return imageLink;
    }
}

import java.awt.image.BufferedImage;

public abstract class BaseTower {
    protected int number = 1;
    protected String imageLink = "src/Towers/Base/image.png";
    public void shoot(){
        System.out.println("base tower shot");
    }
    protected void tick(BaseTile tile,double tickMultiplier){

    }
    protected void tick(double tickMultiplier){

    }
    protected String getImageLink(){return imageLink;}
}

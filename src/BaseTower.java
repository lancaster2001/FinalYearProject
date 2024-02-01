import java.awt.image.BufferedImage;

public abstract class BaseTower {
    protected String imageLink = "src/Towers/Base/image.png";
    public void shoot(){
        System.out.println("base tower shot");
    }
    protected void tick(BaseTile tile,double tickMultiplier){}
    protected void tick(double tickMultiplier){

    }
    public void setup(int x,int y){}
    protected String getImageLink(){return imageLink;}
}

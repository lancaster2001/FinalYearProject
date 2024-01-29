import java.awt.image.BufferedImage;

public abstract class BaseTower {
    protected int number = 1;
    protected String imageLink = "src/Towers/Tower1/image.png";
    protected BufferedImage image;
    public void shoot(){
        System.out.println("base tower shot");
    }
}

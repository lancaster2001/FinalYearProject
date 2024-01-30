import java.awt.image.BufferedImage;

public abstract class BaseTower {
    protected int number = 1;
    protected String imageLink = "src/Towers/Base/image.png";
    protected BufferedImage image;
    public void shoot(){
        System.out.println("base tower shot");
    }
    protected void allAct(BaseTile tile, ResourceManager resourceManagerInstance){
        act();
        act(tile,resourceManagerInstance);
    }
    protected void act(BaseTile tile, ResourceManager resourceManagerInstance){}
    protected void act(){}
    public void setImage(BufferedImage image){this.image = image;}
    public BufferedImage getImage() {return image;}
    public String getImageLink(){return imageLink;}
}

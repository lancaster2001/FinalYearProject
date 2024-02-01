import java.awt.image.BufferedImage;

public abstract class BaseTower {
    protected int number = 1;
    protected String imageLink = "src/Towers/Base/image.png";
    public void shoot(){
        System.out.println("base tower shot");
    }
    protected void allAct(BaseTile tile, ResourceManager resourceManagerInstance){
        act();
        act(tile,resourceManagerInstance);
    }
    protected void act(BaseTile tile, ResourceManager resourceManagerInstance){}
    protected void act(){}
    protected String getImageLink(){return imageLink;}
}

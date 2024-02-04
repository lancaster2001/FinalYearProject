import javax.naming.Name;
import java.awt.*;

public abstract class BaseTower {
    protected Pose pose = new Pose();
    protected String name;
    protected String imageLink = "src/Towers/Base/image.png";
    protected double width = 1.0;
    protected double height = 1.0;
    protected String costResource = "Rock";
    protected int costQuantity = 5;
    public void tick(BaseTile tile,double tickMultiplier){
    }
    public void tick(double tickMultiplier){
        System.out.println("");
    }
    public BaseTower(int x, int y,TowerTemplate template){
        this.pose.setX(x);
        this.pose.setY(y);
        this.name = template.getName();
        this.costResource = template.getCostResource();
        this.costQuantity = template.getCostQuantity();
        this.imageLink = template.getImageLink();
    }
    protected String getImageLink(){return imageLink;}

    public Pose getPose() {
        return pose;
    }
    protected void draw(Graphics g,int x, int y, int width ,int height,AssetManager assetManagerInstance){
        g.drawImage(AssetManager.getInstance().getImage(imageLink), x, y, width, height, null);
    }
    protected String getCostResource(){
        return costResource;
    }
    protected int getCostQuantity(){
        return costQuantity;
    }
}

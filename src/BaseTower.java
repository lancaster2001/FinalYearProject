import java.awt.*;

public abstract class BaseTower {
    protected Pose pose;
    protected String imageLink = "src/Towers/Base/image.png";
    protected void tick(BaseTile tile,double tickMultiplier){}
    protected void tick(double tickMultiplier){

    }
    public void setup(int x,int y){}
    protected String getImageLink(){return imageLink;}

    public Pose getPose() {
        return pose;
    }
    protected void draw(Graphics g,int x, int y, int width ,int height,AssetManager assetManagerInstance){

        g.drawImage(AssetManager.getInstance().getImage(imageLink), x, y, width, height, null);
    }
}

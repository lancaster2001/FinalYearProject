import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class BaseTile {
    protected String imageLink = "src/Tiles/Base/image.png";
    protected String resource = "";
    public BaseTile(TileTemplate template){
        imageLink = template.getImageLink();
        resource = template.getResource();
    }

    protected void setup(){AssetManager.getInstance().getImage(imageLink);}
    protected void draw(Graphics g, int x, int y, int width , int height,AssetManager assetManagerInstance){
        g.drawImage(AssetManager.getInstance().getImage(imageLink), x, y, width, height, null);
    }
    public String getImageLink(){
        return imageLink;
    }

    public String getResource() {
        return resource;
    }
}
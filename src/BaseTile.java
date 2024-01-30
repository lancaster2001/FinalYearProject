import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public abstract class BaseTile {
    protected String imageLink = "src/Tiles/Base/image.png";
    protected BufferedImage image;

    protected void setup(){
    }
    public void setImage(BufferedImage image){this.image = image;}
    public BufferedImage getImage() {return image;}
    public String getImageLink(){return imageLink;}
}
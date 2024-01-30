import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public abstract class BaseTile {
    protected String imageLink = "src/Tiles/Base/image.png";
    protected BufferedImage image;
    protected void setup(){
        try {
            image = ImageIO.read(new File(imageLink));
        } catch (IOException e) {
            System.out.print("error loading tile image");
            throw new RuntimeException(e);
        }
    }

    public BufferedImage getImage() {return image;}
}

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class TowerVariant1 extends BaseTower {
    private Tower1Properties values = new Tower1Properties();

    public TowerVariant1(){
        number = values.fixedNumber;
        image = null;
        imageLink ="src/Towers/Tower1/image.png";
        try {
            image = ImageIO.read(new File(imageLink));
        } catch (IOException e) {
            System.out.print("error loading tower 1 image");
            throw new RuntimeException(e);
        }
    }
    @Override
    public void shoot(){
        System.out.println("tower variant1 shot");
    }
}

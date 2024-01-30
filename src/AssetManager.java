import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class AssetManager {
    //singleton-------------------------------------------------------------------------
    private static AssetManager instance;
    private AssetManager(){

    }
    public static AssetManager getInstance() {
        if (instance == null) {
            instance = new AssetManager();
        }
        return instance;
    }
//----------------------------------------------------------------------------------------
    private ArrayList<String> ImageLinksArray = new ArrayList<String>();
    private ArrayList<BufferedImage> ImagesArray = new ArrayList<BufferedImage>();
    public BufferedImage getImage(String imageLink){
        BufferedImage desiredImage = null;
        boolean add = false;
        int index = 0;
        if (ImageLinksArray.size()!=0) {
            for (String currentLink : ImageLinksArray) {
                if (currentLink != imageLink) {
                    if (index == ImageLinksArray.size() - 1) {
                        add=true;
                        try {
                            desiredImage = ImageIO.read(new File(imageLink));
                            ImagesArray.add(desiredImage);
                        } catch (IOException e) {
                            System.out.print("error loading image");
                            throw new RuntimeException(e);
                        }
                    } else {
                        index += 1;
                    }
                } else {
                    desiredImage = ImagesArray.get(index);
                }
            }
        }else{
            ImageLinksArray.add(imageLink);
            try {
                desiredImage = ImageIO.read(new File(imageLink));
                ImagesArray.add(desiredImage);
            } catch (IOException e) {
                System.out.print("error loading image");
                throw new RuntimeException(e);
            }
        }

        if(add){
            ImageLinksArray.add(imageLink);
        }
        return desiredImage;
    }
}

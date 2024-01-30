import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
public class AssetManager {
    //singleton-------------------------------------------------------------------------
    private static AssetManager instance;
    private AssetManager(){
        loadAllImages("src/Resources/");
        loadAllImages("src/Towers/");
        loadAllImages("src/Tiles/");
    }
    public static AssetManager getInstance() {
        if (instance == null) {
            instance = new AssetManager();
        }
        return instance;
    }
//----------------------------------------------------------------------------------------
    private final ArrayList<String> ImageLinksArray = new ArrayList<String>();
    private final ArrayList<BufferedImage> ImagesArray = new ArrayList<BufferedImage>();
    public BufferedImage getImage(String imageLink){
        BufferedImage desiredImage = null;
        boolean add = false;
        int index = 0;
        if (!ImageLinksArray.isEmpty()) {
            for (String currentLink : ImageLinksArray) {
                if (!currentLink.equals(imageLink)) {
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
    public void loadAllImages(String dirLink){
        File file = new File(dirLink);
        String[] directories = file.list(new FilenameFilter() {
            @Override
            public boolean accept(File current, String name) {
                return new File(current, name).isDirectory();
            }
        });

        ArrayList<BufferedImage> allImages = new ArrayList<BufferedImage>();
        if (directories != null) {
            for(String currentResource: directories) {
                String theLink = dirLink + currentResource + "image.png";
                ImagesArray.add(getImage(theLink));
                ImageLinksArray.add(theLink);
            }
        }

    }
}

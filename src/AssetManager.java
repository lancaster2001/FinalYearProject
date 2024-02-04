import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
public class AssetManager {
    //singleton-------------------------------------------------------------------------
    private static AssetManager instance = new AssetManager();

    private AssetManager() {
        loadAllImages("src/Resources/");
        loadAllImages("src/Towers/Drill/");
        loadAllImages("src/Towers/Turret/");
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

    public BufferedImage getImage(String imageLink) {
        BufferedImage desiredImage = null;
        boolean add = false;
        int index = 0;
        if (!ImageLinksArray.isEmpty()) {
            for (String currentLink : ImageLinksArray) {
                if (!currentLink.equals(imageLink)) {
                    if (index == ImageLinksArray.size() - 1) {
                        add = true;
                        try {
                            desiredImage = ImageIO.read(new File(imageLink));
                            ImagesArray.add(desiredImage);
                        } catch (IOException e) {
                            System.out.print("error loading image");
                            throw new RuntimeException(e);
                        }catch (Exception e){
                            throw new RuntimeException(e);
                        }
                    } else {
                        index += 1;
                    }
                } else {
                    desiredImage = ImagesArray.get(index);
                    return desiredImage;
                }
            }
        } else {
            ImageLinksArray.add(imageLink);
            try {
                desiredImage = ImageIO.read(new File(imageLink));
                ImagesArray.add(desiredImage);
            } catch (Exception e) {
                System.out.print("error loading image");
                throw new RuntimeException(e);
            }
        }

        if (add) {
            ImageLinksArray.add(imageLink);
        }
        return desiredImage;
    }

    private void loadAllImages(String dirLink) {
        File file = new File(dirLink);
        String[] directories = file.list(new FilenameFilter() {
            @Override
            public boolean accept(File current, String name) {
                return new File(current, name).isDirectory();
            }
        });

        if (directories != null) {
            for (String currenFolder : directories) {
                String theLink = dirLink + currenFolder;
                for(String currentImage: getImagesInFolder(theLink)) {
                    ImagesArray.add(getImage(theLink+"/"+currentImage));
                    ImageLinksArray.add(theLink+"/"+currentImage);
                }
            }
        }
    }
    public String[] getImagesInFolder(String theLink) {
        File dir = new File(theLink);
        String[] files = dir.list(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return new File(dir, name).getName().endsWith(".png");
            }
        });
        return files;
    }
}

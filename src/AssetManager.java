import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

public class AssetManager {
    //singleton-------------------------------------------------------------------------
    private static AssetManager instance = new AssetManager();

    private AssetManager() {
        loadAllImages("src/Assets/");

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
    private final String assetsPath = gameConstants.assetsPath;
    private final String errorImagePath = gameConstants.errorImagePath;

    public BufferedImage getImage(String type, String image) {
        return getImage(assetsPath + type + "/" + image);
    }

    public ArrayList<String> checkForVariants(String type, String givenImageLink) {
        boolean check = false;
        ArrayList<String> returnList = new ArrayList<>();
        String compareString = assetsPath + type + "/" + (givenImageLink.replace(".png", "1.png"));
        for (String currentLink : ImageLinksArray) {
            if (compareString.equalsIgnoreCase(currentLink)) {
                check = true;
            }
        }
        if (check) {
            return getVariants(assetsPath + type + "/" + givenImageLink);
        } else {
            returnList.add(givenImageLink);
            return returnList;
        }
    }

    private ArrayList<String> getVariants(String givenImageLink) {
        ArrayList<String> returnList = new ArrayList<>();
        boolean check = true;
        int index = 1;
        while (check) {
            check = false;
            for (String currentLink : ImageLinksArray) {
                if (givenImageLink.replace(".png", index + ".png").equalsIgnoreCase(currentLink)) {
                    check = true;
                    String compareString2 = currentLink.substring(0, currentLink.lastIndexOf("/"));
                    returnList.add(currentLink.replace(compareString2 + "/", ""));
                }
            }
            index += 1;
        }
        return returnList;
    }

    private BufferedImage getImage(String imageLink) {
        BufferedImage desiredImage = null;
        String add = "";
        int index = 0;
        if (!ImageLinksArray.isEmpty()) {
            for (String currentLink : ImageLinksArray) {
                if (!currentLink.equalsIgnoreCase(imageLink)) {
                    if (index == ImageLinksArray.size() - 1) {
                        try {
                            add = imageLink;
                            desiredImage = ImageIO.read(new File(imageLink));
                            ImagesArray.add(desiredImage);
                        } catch (Exception e) {
                            //e.printStackTrace();
                            return getImage(errorImagePath);
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
        if (!add.isEmpty()) {
            ImageLinksArray.add(add);
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
                for (String currentImage : getImagesInFolder(theLink)) {
                    getImage(theLink + "/" + currentImage);
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

    public String[] getJsonsInFolder(String theLink) {
        File dir = new File(theLink);
        String[] files = dir.list(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return new File(dir, name).getName().endsWith(".json");
            }
        });
        return files;
    }


}

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

/*this class's purpose is to load, manage and provide images for other classes and objects*/
//todo: have a separate folder for custom assets so that an untampered folder with default assets can always be called back to
//todo: have an api that can be called upon if there are any missing assets in the default folder
public final class AssetManager {
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

    /*this could and probably should have been done as one variable
    imageLinksArray hold in order the list of directory paths for the images
    held on ImagesArray which hold all the buffered images in the same order*/
    private final ArrayList<String> ImageLinksArray = new ArrayList<String>();
    private final ArrayList<BufferedImage> ImagesArray = new ArrayList<BufferedImage>();
    private final String assetsPath = GameSettings.getInstance().assetsPath;
    /*a separate variable for an error image was made so that assets could be easily swapped in and out
    in the form of texture packs, and so any images that were not included in the pack or were failed to load
    from the pack could be replaced with a default error image that is always available*/

    private final String errorImagePath = GameSettings.getInstance().errorImagePath;

    //checks for variants of an image in a folder and returns an ArrayList of all the variant's image links if there are any
    //else returns a list with just the provided link
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
    //gets all variants of an image within its folder
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
    //allows for specification of image types to get image to help prevent duplicate image names interfering with each other
    public BufferedImage getImage(String type, String image) {
        return getImage(assetsPath + type + "/" + image);
    }

    /*goes through the list of image links to find a matching image if a match is found, the corresponding image
    * is returned, if no match is found, then an attempt to load the image from the assets folder will be made however
    * failing this the error image will be returned*/
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

    //method to load all the images from each folder in the assets folder and stores them and their paths the image arrayLists
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

    //returns a list of directory paths for all the png's in a provided directory
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

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;

public class ResourceManager {
    //singleton------------------------------------------------------------------------
    private static ResourceManager instance = new ResourceManager();
    public static ResourceManager getInstance(){
        if (instance == null) {
            instance = new ResourceManager();
        }
        return instance;
    }
    private ResourceManager(){setup();}
    //----------------------------------------------------------------------------------

    private HashMap<String, Resource> inventory = new HashMap<String, Resource>();
    private void setup(){
        File file = new File("src/Resources/");
        String[] directories = file.list(new FilenameFilter() {
            @Override
            public boolean accept(File current, String name) {
                return new File(current, name).isDirectory();
            }
        });

        ArrayList<BufferedImage> allImages = new ArrayList<BufferedImage>();
        if (directories != null) {
            for(String currentResource: directories) {
                inventory.put(currentResource, new Resource(currentResource));
            }
        }
    }
    public Resource queryInventory(String query){
        return inventory.get(query);
    }
    public ArrayList<Resource> getInventoryArray(){
        ArrayList<Resource> returnArray = new ArrayList<Resource>();
        ArrayList<Resource> inventoryArray = new ArrayList<Resource>();
        inventoryArray.addAll(inventory.values());
        for(Resource index: inventoryArray){
            if (index.getQuantity()>0){
                returnArray.add(index);
            }
        }
        return returnArray;
    }
}

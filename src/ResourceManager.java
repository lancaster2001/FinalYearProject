import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

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
    String tilePath ="src/Assets/Tiles/";
    String iconPath = "src/Assets/Icons/";
    String resourcesJsonPath = "src/Resources/Resources.json";
    private void setup(){
        String filePath = resourcesJsonPath;

        try (FileReader reader = new FileReader(filePath)) {
            // Using JSONTokener to parse the JSON file
            JSONTokener tokener = new JSONTokener(reader);
            JSONObject jsonObject = new JSONObject(tokener);

            // Accessing values from the JSON object
            JSONArray resources = jsonObject.getJSONArray("Resources");
            Iterator<Object> iterator = resources.iterator();
            while(iterator.hasNext()) {
                JSONObject object = (JSONObject) iterator.next();
                String name = object.getString("Name").toLowerCase();
                inventory.put(name, new Resource(name, "ore-"+name+".png",name+".png"));
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        /*
        String[] directories = file.list(new FilenameFilter() {
            @Override
            public boolean accept(File current, String name) {
                return new File(current, name).isDirectory();
            }
        });

        ArrayList<BufferedImage> allImages = new ArrayList<BufferedImage>();
        if (directories != null) {
            for(String currentResource: directories) {
                AssetManager.getInstance().re
                inventory.put(currentResource, new Resource(currentResource));
            }
        }*/
    }
    public Resource queryInventory(String query){
        return inventory.get(query.toLowerCase());
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

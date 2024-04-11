import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.awt.*;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

public final class ResourceManager {
    //singleton------------------------------------------------------------------------
    private static ResourceManager instance = new ResourceManager();

    public static ResourceManager getInstance() {
        if (instance == null) {
            instance = new ResourceManager();
        }
        return instance;
    }

    private ResourceManager() {
        setup();
    }
    //----------------------------------------------------------------------------------
    private HashMap<String, Resource> inventory = new HashMap<>();
    ArrayList<Resource> listToDraw = new ArrayList<>();
    String tilePath = "src/Assets/Tiles/";
    String iconPath = "src/Assets/Icons/";
    String resourcesJsonPath = "src/Resources/Resources.json";

    private void setup() {
        String filePath = resourcesJsonPath;

        try (FileReader reader = new FileReader(filePath)) {
            // Using JSONTokener to parse the JSON file
            JSONTokener tokener = new JSONTokener(reader);
            JSONObject jsonObject = new JSONObject(tokener);

            // Accessing values from the JSON object
            JSONArray resources = jsonObject.getJSONArray("Resources");
            Iterator<Object> iterator = resources.iterator();
            while (iterator.hasNext()) {
                JSONObject object = (JSONObject) iterator.next();
                String name = object.getString("Name").toLowerCase();
                inventory.put(name.toLowerCase(), getResource(name));
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    public Resource getResource(String name) {
        return new Resource(name, "ore-" + name + ".png", name + ".png");
    }

    public Resource getResource(String name, Pose pose) {
        return new Resource(name, "ore-" + name + ".png", name + ".png", pose);
    }

    public Resource queryInventory(String query) {
        return inventory.get(query.toLowerCase());
    }

    public ArrayList<Resource> getInventoryArray() {
        ArrayList<Resource> returnArray = new ArrayList<Resource>();
        ArrayList<Resource> inventoryArray = new ArrayList<Resource>();
        inventoryArray.addAll(inventory.values());
        for (Resource index : inventoryArray) {
            if (index.getQuantity() > 0) {
                returnArray.add(index);
            }
        }
        return returnArray;
    }

    public void addToDrawList(Resource resource) {
        boolean check = true;
        for (Resource resource1 : listToDraw) {
            if (resource1.getId().equals(resource.getId())) {
                check = false;
            }
        }
        if (check) {
            listToDraw.add(resource);
        }
    }

    public void draw(Graphics g, AssetManager assetManagerInstance) {
        for (Resource resource : listToDraw) {
            int[] x = Camera.getInstance().getOnScreenXandWidth(resource.getPose().getX());
            int[] y = Camera.getInstance().getOnScreenYandHeight(resource.getPose().getY());

            resource.draw(g, x[0], y[0], x[1], y[1], assetManagerInstance);
        }
        listToDraw.clear();
    }
    public JSONObject save(){
        JSONObject json = new JSONObject();
        Collection<Resource> inventoryList = inventory.values();
        for (Resource resource : inventoryList) {
            json.put(resource.getName(),resource.getQuantity());
        }
        return json;
    }
    public void load(JSONObject jsonObject){
        Collection<Resource> inventoryList = inventory.values();
        for (Resource resource : inventoryList) {
            inventory.get(resource.getName()).setQuantity(jsonObject.getInt(resource.getName()));

        }
    }
}

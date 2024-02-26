import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class SaveHandler {
    //singleton-------------------------------------------------------------------------
    private static SaveHandler instance = new SaveHandler();

    private SaveHandler() {
        try {
            Files.createDirectories(Paths.get(saveLink));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        loadListofSaves();
    }

    public static SaveHandler getInstance() {
        if (instance == null) {
            instance = new SaveHandler();
        }
        return instance;
    }

    //----------------------------------------------------------------------------------------
    private String saveSlot = "save1";
    private final String saveLink = "src/Saves/";
    private Map mapInstance = null;
    private String[] saves = new String[]{};
    private int mapWidth = gameConstants.mapWidth;
    private int mapHeight = gameConstants.mapHeight;

    public String getSaveSlot() {
        return saveSlot;
    }
    public void saveGame(){
        JSONObject resourceJson =ResourceManager.getInstance().save();
        String mapLink = GameState.getInstance().getMapInstance().save();
        String fileName = saveLink + saveSlot + ".json";
        JSONObject json = new JSONObject();
        json.put("Resources", resourceJson);
        json.put("MapLink", mapLink);
        try (FileWriter file = new FileWriter(fileName)) {
            file.write(json.toString());
            file.flush();
        } catch (IOException e) {
            e.fillInStackTrace();
        }
    }

    public void loadSave(){
        String filePath = saveLink+saveSlot+".json";
        try (FileReader reader = new FileReader(filePath)) {
            // Using JSONTokener to parse the JSON file
            JSONTokener tokener = new JSONTokener(reader);
            JSONObject jsonObject = new JSONObject(tokener);

            String mapLink =jsonObject.getString("MapLink");
            JSONObject inventoryJson = jsonObject.getJSONObject("Resources");

            ResourceManager.getInstance().load(inventoryJson);
            mapInstance = MapGenerator.getInstance().loadMap(mapLink);
        } catch (IOException | JSONException e) {
            System.out.println("no save data for" + saveSlot);
        }
    }
    public void newSave(){
        saveSlot = String.valueOf(Math.random());
        mapInstance = MapGenerator.getInstance().createNewMap(mapWidth,mapHeight);
    }

    public Map getMapInstance() {
        if (mapInstance != null) {
            return mapInstance;
        }
        return null;
    }
    private void loadListofSaves() {
        saves = getJsonsInFolder(saveLink);
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

    public String[] getSavesList() {
        return saves;
    }

    public void setSaveSlot(String saveSlot) {
        this.saveSlot = saveSlot;
        loadSave();
    }
}

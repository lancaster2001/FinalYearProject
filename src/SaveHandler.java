import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SaveHandler {
    //singleton-------------------------------------------------------------------------
    private static SaveHandler instance = new SaveHandler();

    private SaveHandler() {
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
    private Map mapInstance;

    public String getSaveSlot() {
        return saveSlot;
    }
    public void saveGame(){
        JSONObject resourceJson =ResourceManager.getInstance().save();
        String mapLink = GameState.getInstance().getMapInstance().save();
        try {
            Files.createDirectories(Paths.get(saveLink));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
            e.printStackTrace();
        }
    }

    public Map getMapInstance() {
        return mapInstance;
    }
}

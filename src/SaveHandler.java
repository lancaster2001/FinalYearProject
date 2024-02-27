import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SaveHandler {
    //singleton-------------------------------------------------------------------------
    private static SaveHandler instance = new SaveHandler();

    private SaveHandler() {
        try {
            Files.createDirectories(Paths.get(saveLink));
            Files.createDirectories(Paths.get(saveSlotFileLocation.substring(0,saveSlotFileLocation.lastIndexOf("/"))));
            saves = getJsonsInFolder(saveLink);
            loadSaveSlot();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    public static SaveHandler getInstance() {
        if (instance == null) {
            instance = new SaveHandler();
        }
        return instance;
    }

    //----------------------------------------------------------------------------------------
    String saveSlotFileLocation = gameSettings.getInstance().saveSlotFileLocation;
    private String saveSlot;
    private final String saveLink = gameSettings.getInstance().savesPath;
    private Map mapInstance = null;
    private String[] saves = new String[]{};
    private int mapWidth = gameSettings.getInstance().getMapWidth();
    private int mapHeight = gameSettings.getInstance().getMapHeight();

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
            System.out.println("no save data for " + saveSlot);
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
        if(saves == null){
            saves = getJsonsInFolder(saveLink);
        }
        return saves;
    }

    public void setSaveSlot(String saveSlot) {
        this.saveSlot = saveSlot;
        loadSave();
    }
    public void saveSaveSlot (){
        String fileName = saveSlotFileLocation;
        JSONObject json = new JSONObject();
        json.put("saveSlot", saveSlot);
        try (FileWriter file = new FileWriter(fileName)) {
            file.write(json.toString());
            file.flush();
        } catch (IOException e) {
            e.fillInStackTrace();
        }
    }
    private void loadSaveSlot (){
        String link = saveSlotFileLocation;
        try (FileReader reader = new FileReader(link)) {
            // Using JSONTokener to parse the JSON file
            JSONTokener tokener = new JSONTokener(reader);
            JSONObject jsonObject = new JSONObject(tokener);
            saveSlot = jsonObject.getString("saveSlot");
        } catch (IOException | JSONException e) {
            System.out.print("save slot didnt load from persistent storage resetting to first available slot");
            if(getSavesList()!=null) {
                saveSlot = getSavesList()[0];
            }else{
                System.out.print("no slots found setting save slot to fall back slot: save1");
                saveSlot = "save1";
            }
        }
    }
}

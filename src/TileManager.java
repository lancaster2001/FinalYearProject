import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;

public class TileManager {
    //singleton-------------------------------------------------------------------------
    private static TileManager instance = new TileManager();
    private TileManager(){
        loadTiles();
    }
    public static TileManager getInstance() {
        if (instance == null) {
            instance = new TileManager();
        }
        return instance;
    }
    //----------------------------------------------------------------------------------------
    private final ArrayList<TileTemplate> tileArrayList = new ArrayList<TileTemplate>();
    private final String tileLink = "src/Tiles/";
    private void loadTiles() {loadListOfTiles(tileLink);}
    private void loadListOfTiles(String tilesFolder){
        File towerTypesFolders = new File(tilesFolder);
        String[] directories = towerTypesFolders.list(new FilenameFilter() {
            @Override
            public boolean accept(File current, String name) {
                return new File(current, name).isDirectory();
            }
        });
        //get the directory of each tower type e.g. turret, driller, etc
        if (directories != null) {
            for (String currentTileType : directories) {
                tileArrayList.add(readTileTemplate(tilesFolder+currentTileType));
            }
        }
    }

    private TileTemplate readTileTemplate(String link) {
        // Replace "path/to/your/file.json" with the actual path to your JSON file
        String filePath = link + "/tile.json";
        TileTemplate template = new TileTemplate();

        try (FileReader reader = new FileReader(filePath)) {
            // Using JSONTokener to parse the JSON file
            JSONTokener tokener = new JSONTokener(reader);
            JSONObject jsonObject = new JSONObject(tokener);

            // Accessing values from the JSON object
            String resource = jsonObject.getString("Resource");
            String name = jsonObject.getString("Name");
            String imageLink = link+"/"+AssetManager.getInstance().getImagesInFolder(link)[0];
            template = new TileTemplate(name,resource, imageLink);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return template;
    }

    public TileTemplate getTile(String tileName){
        for(TileTemplate tile: tileArrayList){
            if(tileName.equals(tile.getName())){
                return tile;
            }
        }
        return new TileTemplate();
    }

}

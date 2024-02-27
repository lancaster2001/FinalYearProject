import org.json.JSONObject;

import java.util.ArrayList;

public class TileManager {
    //singleton-------------------------------------------------------------------------
    private static TileManager instance = new TileManager();

    private TileManager() {
        //loadTiles();
    }
    public static TileManager getInstance() {
        if (instance == null) {
            instance = new TileManager();
        }
        return instance;
    }
    //----------------------------------------------------------------------------------------
    private final ArrayList<TileTemplate> tileArrayList = new ArrayList<TileTemplate>();
    private final String assetFoldername = "Tiles";

    public TileTemplate creatNewTileTemplate(String tile, String resource) {
        if (resource != null) {
            return new TileTemplate(tile, resource, AssetManager.getInstance().checkForVariants(assetFoldername, tile + ".png"));
        } else {
            return new TileTemplate(tile, "rock", AssetManager.getInstance().checkForVariants(assetFoldername, tile + ".png"));
        }
    }

    public TileTemplate loadTileTemplate(JSONObject jsonArray) {
        String tile;
        String resource;
        tile = jsonArray.getString("ImageLink");
        resource = jsonArray.getString("Resource");
        return creatNewTileTemplate(tile, resource);
    }
}

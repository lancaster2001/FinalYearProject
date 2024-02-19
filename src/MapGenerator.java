import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class MapGenerator {
    //singleton------------------------------------------------------------------------
    private static MapGenerator instance = new MapGenerator();

    public static MapGenerator getInstance() {
        if (instance == null) {
            instance = new MapGenerator();
        }
        return instance;
    }

    private MapGenerator() {

    }

    //----------------------------------------------------------------------------------
    private Map latestMap;
    private final TileManager tileManagerInstance = TileManager.getInstance();

    public Map createNewMap(int mapWidth, int mapHeight) {
        return generateBasicMap(mapWidth, mapHeight);
    }

    private Map generateBasicMap(int mapWidth, int mapHeight) {
        boolean check = true;
        ArrayList<MapSlot> mapArray = new ArrayList<>();
        Random rnd = new Random();
        for (int slotNumber = 0; slotNumber < (mapWidth * mapHeight); slotNumber++) {
            int x = (slotNumber % mapWidth) + 1;
            int y = (slotNumber / mapWidth) + 1;
            if (rnd.nextInt(1, 10) == 5) {
                mapArray.add(new MapSlot(x, y, tileManagerInstance.creatNewTileTemplate("basalt", "copper")));
            } else if (rnd.nextInt(1, 10) == 5) {
                mapArray.add(new MapSlot(x, y, tileManagerInstance.creatNewTileTemplate("sand", null)));
            } else if (rnd.nextInt(1, 10) == 5) {
                mapArray.add(new MapSlot(x, y, tileManagerInstance.creatNewTileTemplate("basalt", "iron")));
            } else {
                mapArray.add(new MapSlot(x, y, tileManagerInstance.creatNewTileTemplate("basalt", null)));
            }
        }
        createResourcePatch(rnd.nextInt(1, mapWidth), rnd.nextInt(1, mapHeight), 4, "copper", mapArray, mapWidth);

        latestMap = new Map(mapArray, mapWidth, mapHeight);
        return latestMap;
    }

    private Map generateTestMap(int mapWidth, int mapHeight) {
        boolean check = true;
        ArrayList<MapSlot> mapArray = new ArrayList<>();
        for (int slotNumber = 0; slotNumber < (mapWidth * mapHeight); slotNumber++) {
            int x = (slotNumber % mapWidth) + 1;
            int y = (slotNumber / mapWidth) + 1;
            if (check) {
                check = !check;
                mapArray.add(new MapSlot(x, y, tileManagerInstance.creatNewTileTemplate("test", null)));
            } else {
                check = !check;
                mapArray.add(new MapSlot(x, y, tileManagerInstance.creatNewTileTemplate("sand", null)));
            }

        }
        latestMap = new Map(mapArray, mapWidth, mapHeight);
        return latestMap;
    }

    private ArrayList<MapSlot> createPatch(int x, int y, int radius, String tileImage, ArrayList<MapSlot> mapArray, int width) {
        if (radius > 0) {
            for (int circley = y - radius; circley < y + radius; circley++) {
                if (circley > 0) {
                    for (int circlex = x - radius; circlex < x + radius; circlex++) {
                        if (circlex > 0) {
                            int currentSlot = ((y * width) + x);
                            mapArray.add(currentSlot, new MapSlot(x, y, tileManagerInstance.creatNewTileTemplate(tileImage, null)));
                        }
                    }
                }
            }
        }

        return mapArray;
    }

    private ArrayList<MapSlot> createResourcePatch(int x, int y, int radius, String resource, ArrayList<MapSlot> mapArray, int width) {
        if (radius > 0) {
            for (int circley = y - radius; circley < y + radius; circley++) {
                if (circley > 0) {
                    for (int circlex = x - radius; circlex < x + radius; circlex++) {
                        if (circlex > 0) {
                            int currentSlot = ((y * width) + x);
                            mapArray.add(currentSlot, new MapSlot(x, y, tileManagerInstance.creatNewTileTemplate(mapArray.get(currentSlot).getTile().getImageLink(), resource)));
                        }
                    }
                }
            }
        }
        return mapArray;
    }

    public Map loadMap(String link) {
        ArrayList<MapSlot> mapArray = new ArrayList<>();
        try (FileReader reader = new FileReader(link)) {
            // Using JSONTokener to parse the JSON file
            JSONTokener tokener = new JSONTokener(reader);
            JSONObject jsonObject = new JSONObject(tokener);
            int mapWidth = jsonObject.getInt("Width");
            int mapHeight = jsonObject.getInt("Height");
            for (int index = 0; index < mapWidth * mapHeight; index++) {
                JSONObject mapslotjson = jsonObject.getJSONObject("" + index);
                mapArray.add(new MapSlot(mapslotjson));
            }
            return new Map(mapArray, mapWidth, mapHeight,link.substring(link.lastIndexOf("/") + 1, link.length() - 5));
        } catch (IOException | JSONException e) {
            e.fillInStackTrace();
            return null;
        }
    }
}

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
            mapArray.add(new MapSlot(x, y, tileManagerInstance.creatNewTileTemplate("basalt", null)));

        }
        for(int counter = 3; counter<=4;counter++){
            int thisX = rnd.nextInt(1, mapWidth);
            int thisY = rnd.nextInt(1, mapHeight);
            createPatch(thisX, thisY, counter, mapArray, mapWidth,"sand" , null);
            createPatch(thisX, thisY, counter-1, mapArray, mapWidth,"water" , null);
        }

        for(int counter = 2; counter<=3;counter++){
            createPatch(rnd.nextInt(1, mapWidth), rnd.nextInt(1, mapHeight), counter, mapArray, mapWidth,null , "iron");
        }
        for(int counter2 = 1; counter2<=3;counter2++) {
            for (int counter = 2; counter <= 3; counter++) {
                createPatch(rnd.nextInt(1, mapWidth), rnd.nextInt(1, mapHeight), counter, mapArray, mapWidth, null, "copper");
            }
        }
        for(int counter2 = 1; counter2<=3;counter2++) {
            for (int counter = 2; counter <= 3; counter++) {
                createPatch(rnd.nextInt(1, mapWidth), rnd.nextInt(1, mapHeight), counter, mapArray, mapWidth, null, "titanium");
            }
        }

        latestMap = new Map(mapArray, mapWidth, mapHeight);
        latestMap.setTower(TowerManager.getInstance().getTemplate("BasicBase"),new Pose(mapWidth / 2, mapHeight / 2, 0), false);
        return latestMap;
    }


    private ArrayList<MapSlot> createPatch(int x, int y, int radius, ArrayList<MapSlot> mapArray, int width,String tile, String resource) {
        if (radius > 0) {
            radius=Math.abs(radius);
            for (int circley = y - (radius-1); circley < y + radius; circley++) {
                if (circley >= 0) {
                   // for (int circlex = x - radius; circlex < x + radius; circlex++) {
                    for (int circlex = x - ((radius-1)-Math.abs(circley-y)); circlex <= x + ((radius-1)-Math.abs(circley-y)); circlex++) {
                        if (circlex >= 0) {
                            int currentSlot = ((circley * width) + circlex);
                            if ((currentSlot>0)&&(currentSlot<=mapArray.size())) {
                                //mapArray.add(currentSlot, new MapSlot(circlex, circley, tileManagerInstance.creatNewTileTemplate(mapArray.get(currentSlot).getTile().getImageLink(), resource)));
                                if (tile != null) {
                                    String holderResource = mapArray.get(currentSlot).getTile().getResource();
                                    mapArray.set(currentSlot, new MapSlot(circlex+1, circley+1, tileManagerInstance.creatNewTileTemplate(tile, null)));
                                    mapArray.get(currentSlot).getTile().setResource(holderResource);
                                }
                                if (resource != null) {
                                    mapArray.get(currentSlot).getTile().setResource(resource);
                                }
                            }
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

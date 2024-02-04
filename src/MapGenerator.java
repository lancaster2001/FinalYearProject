import java.util.ArrayList;

public class MapGenerator {
    //singleton------------------------------------------------------------------------
    private static MapGenerator instance = new MapGenerator();
    public static MapGenerator getInstance(){
        if (instance == null) {
            instance = new MapGenerator();
        }
        return instance;
    }
    private MapGenerator(){

    }
    //----------------------------------------------------------------------------------
    public Map createNewMap(int mapWidth, int mapHeight){
        return generateBasicMap(mapWidth,mapHeight);
    }
    private Map generateBasicMap(int mapWidth, int mapHeight){
        ArrayList<MapSlot> mapArray = new ArrayList<>();
        for(int slotNumber = 0; slotNumber < (gameConstants.mapSize); slotNumber++){
            int x = (slotNumber%gameConstants.mapWidth)+1;
            int y = (slotNumber/gameConstants.mapWidth)+1;
            mapArray.add(new MapSlot(x, y));
        }
        return new Map(mapArray, mapWidth, mapHeight);
    }
}

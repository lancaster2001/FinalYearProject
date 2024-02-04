import java.util.ArrayList;
import java.util.Random;

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
    private final TileManager tileManagerInstance = TileManager.getInstance();
    public Map createNewMap(int mapWidth, int mapHeight){
        return generateBasicMap(mapWidth,mapHeight);
    }
    private Map generateBasicMap(int mapWidth, int mapHeight){
        ArrayList<MapSlot> mapArray = new ArrayList<>();
        for(int slotNumber = 0; slotNumber < (gameConstants.mapSize); slotNumber++){
            int x = (slotNumber%gameConstants.mapWidth)+1;
            int y = (slotNumber/gameConstants.mapWidth)+1;
            Random rnd = new Random();
            if(rnd.nextInt(1,10)==5){
                mapArray.add(new MapSlot(x, y,tileManagerInstance.getTile("Rock-Iron")));
            }else{
                mapArray.add(new MapSlot(x, y,tileManagerInstance.getTile("Grass")));
            }

        }
        return new Map(mapArray, mapWidth, mapHeight);
    }
}

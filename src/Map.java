import java.awt.*;
import java.util.ArrayList;

public final class Map {
    //singleton------------------------------------------------------------------------
    private static Map instance;
    public static Map getInstance(){
        if (instance == null) {
            instance = new Map();
        }
        return instance;
    }
    private Map(){
        setup();
    }
    //----------------------------------------------------------------------------------
    private final ArrayList<MapSlot> mapArray = new ArrayList<>();
    private int mapWidth = 0;
    private int mapHeight = 0;
    private void setup(){
        mapWidth = gameConstants.mapWidth;
        mapHeight = gameConstants.mapHeight;
        for(int slotNumber = 0; slotNumber < (gameConstants.mapSize); slotNumber++){
            int x = (slotNumber%gameConstants.mapWidth)+1;
            int y = (slotNumber/gameConstants.mapWidth)+1;
            mapArray.add(new MapSlot(x, y));
        }
    }
    private MapSlot getSlotFromCoord(int x, int y){
        int desiredSlotNum = 0;
        MapSlot desiredSlot;
        desiredSlotNum += (y-1)*mapWidth;
        desiredSlotNum += x-1;
        try {
           desiredSlot = mapArray.get(desiredSlotNum);
        }catch (Exception IndexOutOfBoundsException){
            desiredSlot = mapArray.getFirst();
        }
        return desiredSlot;
    }


    public ArrayList<MapSlot> getMapSection(int x, int y, int numOslotsWide, int numOslotsTall){
        ArrayList<MapSlot> mapSection = new ArrayList<>();
        int currentYindex = -1;
        int currentXindex = -1;
        try {
            for (int yIndex = y;yIndex <(y+numOslotsTall);yIndex ++){
                currentYindex = yIndex;
                for (int xIndex = x;xIndex <(x+numOslotsWide);xIndex ++){
                    currentXindex = xIndex;
                    mapSection.add(getSlotFromCoord(xIndex,yIndex));
                }
            }
        }catch (Exception IndexOutOfBoundsException){
            System.out.println("out of bounds error at get map section\nyIndex:"+currentYindex+"\nxIndex:"+currentXindex);
            System.out.println(sectionOutOfBoundsCheck(x,y,numOslotsWide,numOslotsTall).name());//todo test this works
        }
        return mapSection;
    }

    public gameConstants.DIRECTION sectionOutOfBoundsCheck(int x, int y, int numOslotsWide, int numOslotsTall){
        gameConstants.DIRECTION directionOutOfBounds = gameConstants.DIRECTION.NULL;
        for (int yIndex = y;yIndex <(y+numOslotsTall-1);yIndex ++){
            for (int xIndex = x;xIndex <(x+numOslotsWide-1);xIndex ++){
                if (xIndex >= mapWidth){
                    directionOutOfBounds = gameConstants.DIRECTION.RIGHT;
                    return directionOutOfBounds;
                } else if (xIndex < 1) {
                    directionOutOfBounds = gameConstants.DIRECTION.LEFT;
                    return directionOutOfBounds;
                }
            }
            if (yIndex >= mapHeight){
                directionOutOfBounds = gameConstants.DIRECTION.DOWN;
                return directionOutOfBounds;
            } else if (yIndex<1) {
                directionOutOfBounds = gameConstants.DIRECTION.UP;
                return directionOutOfBounds;
            }
        }
        return directionOutOfBounds;
    }

    public void tick(double tickMultiplier){
        for(MapSlot slot:mapArray){
            slot.tick(tickMultiplier);
        }
    }
    public void draw(Graphics g,Camera cameraInstance, AssetManager assetManagerInstance){
        for(MapSlot slot: mapArray){
            if(slot.onScreenCheck(cameraInstance)){
                slot.draw(g,cameraInstance,assetManagerInstance);


            }
        }
    }

    public void setTower(BaseTower newTower, int x, int y){
        getSlotFromCoord(x,y).setTower(newTower);
    }

}

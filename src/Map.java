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
    private ArrayList<MapSlot> mapArray = new ArrayList<MapSlot>();
    private int mapWidth = 0;
    private int mapHeight = 0;
    private void setup(){

        mapWidth = gameConstants.mapWidth;
        mapHeight = gameConstants.mapHeight;
        for(int slotNumber = 0; slotNumber < (gameConstants.mapSize); slotNumber++){
            mapArray.add(new MapSlot(slotNumber%gameConstants.mapWidth,(int) slotNumber/gameConstants.mapWidth));
        }
    }
    private MapSlot getSlotInCoord(int x, int y){
        int desitredSlotNum = 0;
        MapSlot desiredSlot;
        desitredSlotNum += (y-1)*mapWidth;
        desitredSlotNum += x-1;
        try {
           desiredSlot = mapArray.get(desitredSlotNum);
        }catch (Exception IndexOutOfBoundsException){
            desiredSlot = mapArray.getFirst();
        }
        return desiredSlot;
    }


    public ArrayList<MapSlot> getMapSection(int x, int y, int numOslotsWide, int numOslotsTall){
        ArrayList<MapSlot> mapSection = new ArrayList<MapSlot>();
        int currentYindex = -1;
        int currentXindex = -1;
        try {
            for (int yIndex = y;yIndex <= y+numOslotsTall; yIndex++){
                currentYindex = yIndex;
                for(int xIndex = x;xIndex <= x+numOslotsWide; xIndex++){
                    currentXindex = xIndex;
                    mapSection.add(getSlotInCoord(xIndex,yIndex));
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
        for (int yIndex = y;yIndex <= y+numOslotsTall; yIndex++){
            for(int xIndex = x;xIndex <= x+numOslotsWide; xIndex++){
                 if (xIndex >= mapWidth){
                     directionOutOfBounds = gameConstants.DIRECTION.RIGHT;
                 } else if (xIndex < 0) {
                     directionOutOfBounds = gameConstants.DIRECTION.LEFT;
                 }
            }
            if (yIndex >= mapHeight){
                directionOutOfBounds = gameConstants.DIRECTION.DOWN;
            } else if (yIndex<0) {
                directionOutOfBounds = gameConstants.DIRECTION.UP;
            }
        }
        return directionOutOfBounds;
    }

}

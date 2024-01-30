import com.sun.source.tree.CatchTree;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
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
    private ArrayList<String> ImageLinksArray = new ArrayList<String>();
    private ArrayList<BufferedImage> ImagesArray = new ArrayList<BufferedImage>();
    private int mapWidth = 0;
    private int mapHeight = 0;
    private void setup(){
        mapWidth = gameConstants.mapWidth;
        mapHeight = gameConstants.mapHeight;
        for(int slotNumber = 0; slotNumber < (gameConstants.mapSize); slotNumber++){
            int x = slotNumber%gameConstants.mapWidth;
            int y = slotNumber/gameConstants.mapWidth;
            mapArray.add(new MapSlot(x, y));
            BaseTower currentTower = getSlotFromCoord(x, y).getTower();
            BaseTile currentTile = getSlotFromCoord(x, y).getTile();
            currentTower.setImage(getImage(currentTower.getImageLink()));
            currentTile.setImage(getImage(currentTile.getImageLink()));
        }
    }
    private MapSlot getSlotFromCoord(int x, int y){
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
                    mapSection.add(getSlotFromCoord(xIndex,yIndex));
                }
            }
        }catch (Exception IndexOutOfBoundsException){
            System.out.println("out of bounds error at get map section\nyIndex:"+currentYindex+"\nxIndex:"+currentXindex);
            System.out.println(sectionOutOfBoundsCheck(x,y,numOslotsWide,numOslotsTall).name());//todo test this works
        }
        return mapSection;
    }
    public BufferedImage getImage(String imageLink){
        BufferedImage desiredImage = null;
        boolean add = false;
        int index = 0;
        if (ImageLinksArray.size()!=0) {
            for (String currentLink : ImageLinksArray) {
                if (currentLink != imageLink) {
                    if (index == ImageLinksArray.size() - 1) {
                        add=true;
                        try {
                            desiredImage = ImageIO.read(new File(imageLink));
                            ImagesArray.add(desiredImage);
                        } catch (IOException e) {
                            System.out.print("error loading image");
                            throw new RuntimeException(e);
                        }
                    } else {
                        index += 1;
                    }
                } else {
                    desiredImage = ImagesArray.get(index);
                }
            }
        }else{
            ImageLinksArray.add(imageLink);
            try {
                desiredImage = ImageIO.read(new File(imageLink));
                ImagesArray.add(desiredImage);
            } catch (IOException e) {
                System.out.print("error loading image");
                throw new RuntimeException(e);
            }
        }

        if(add){
            ImageLinksArray.add(imageLink);
        }
        return desiredImage;
    }
    public gameConstants.DIRECTION sectionOutOfBoundsCheck(int x, int y, int numOslotsWide, int numOslotsTall){
        gameConstants.DIRECTION directionOutOfBounds = gameConstants.DIRECTION.NULL;
        for (int yIndex = y;yIndex <= y+numOslotsTall; yIndex++){
            for(int xIndex = x;xIndex <= x+numOslotsWide; xIndex++){
                 if (xIndex >= mapWidth){
                     directionOutOfBounds = gameConstants.DIRECTION.RIGHT;
                     return directionOutOfBounds;
                 } else if (xIndex < 0) {
                     directionOutOfBounds = gameConstants.DIRECTION.LEFT;
                     return directionOutOfBounds;
                 }
            }
            if (yIndex >= mapHeight){
                directionOutOfBounds = gameConstants.DIRECTION.DOWN;
                return directionOutOfBounds;
            } else if (yIndex<0) {
                directionOutOfBounds = gameConstants.DIRECTION.UP;
                return directionOutOfBounds;
            }
        }
        return directionOutOfBounds;
    }
    public void makeAllTowersAct(){
        for(MapSlot slot:mapArray){
            if (slot.getTower() != null){
                slot.getTower().act();
            }
        }
    }

}

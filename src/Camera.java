import com.sun.source.tree.ReturnTree;

import java.nio.charset.CoderResult;
import java.util.ArrayList;

public class Camera {
    //singleton------------------------------------------------------
    private static Camera instance;
    private Camera(){
        zoom = gameConstants.defaultZoom;
        coordinates = gameConstants.defaultCameraCoordinates;
    }
    public static Camera getInstance(){
        if (instance == null) {
            instance = new Camera();
        }
        return instance;
    }
    //------------------------------------------------------------------
    private int zoom = gameConstants.defaultZoom;//number of tiles in width
    private int numOslotsWide = zoom;
    private int numOslotsTall = zoom;
    private int[] coordinates = {0,0};//{x,y}
    private int widthOfSlot;
    private int heightOfslot;
    private ArrayList<MapSlot> viewableMap = new ArrayList<MapSlot>();
    private Map mapInstance = Map.getInstance();
    public int[] getCoordinates(){return coordinates;}
    public int getZoom(){return zoom;}
    private void calculateValues(){
        numOslotsWide = zoom;
        numOslotsTall = zoom;
        widthOfSlot = gameConstants.screenWidth/zoom;
        heightOfslot = gameConstants.screenHeight/zoom;
        viewableMap = mapInstance.getMapSection(coordinates[0], coordinates[1],numOslotsWide,numOslotsTall);
        outOfBoundsCheck();
    }
    public void increaseZoom(){
        if ((zoom < gameConstants.mapWidth-1) && (zoom < gameConstants.mapHeight-1)){
            zoom += 1;
            calculateValues();
        }
    }
    public void decreaseZoom(){
        if (zoom > 1){
            zoom -= 1;
            calculateValues();
        }
    }
    private void outOfBoundsCheck(){
        boolean outOfBounds = true;
        while(outOfBounds){
            gameConstants.DIRECTION directionOutOfBounds = mapInstance.sectionOutOfBoundsCheck(coordinates[0],coordinates[1],numOslotsWide,numOslotsTall);
            if (directionOutOfBounds == gameConstants.DIRECTION.NULL){
                outOfBounds=false;
            }else if (directionOutOfBounds == gameConstants.DIRECTION.RIGHT){
                coordinates[0]-=1;
            }else if (directionOutOfBounds == gameConstants.DIRECTION.LEFT){
                coordinates[0]+=1;
            }else if (directionOutOfBounds == gameConstants.DIRECTION.UP){
                coordinates[1]+=1;
            }else if (directionOutOfBounds == gameConstants.DIRECTION.DOWN){
                coordinates[1]-=1;
            }else{
                System.out.println("error at out of bounds check in camera");
                outOfBounds = false;
            }
        }
    }
    public int getheightOfslot() {return heightOfslot;}
    public int getwidthOfSlot() {return widthOfSlot;}
    public int getNumOslotsWide(){return numOslotsWide;}
    public int getNumOslotsTall() {return numOslotsTall;}
    public MapSlot getMapslot(int x, int y){
        MapSlot desiredSlot;
        int index = ((y-1)*numOslotsWide) + (x-1);
        desiredSlot = viewableMap.get(index);
        return desiredSlot;
    }

    public ArrayList<MapSlot> getViewableMap() {
        calculateValues();
        return viewableMap;
    }
}

import java.awt.*;
import java.util.ArrayList;

public class Camera {
    //singleton------------------------------------------------------
    private static Camera instance;
    private Camera(){
        zoom = gameConstants.defaultZoom;
        x = gameConstants.defaultCameraCoordinates[0];
        y = gameConstants.defaultCameraCoordinates[1];
        screenWidthProportion = (double)gameConstants.screenWidth/ (double)gameConstants.screenHeight;
    }
    public static Camera getInstance(){
        if (instance == null) {
            instance = new Camera();
        }
        return instance;
    }
    //------------------------------------------------------------------
    private int zoom = gameConstants.defaultZoom;//number of tiles in width
    double screenWidthProportion = 0.0;
    private int numOslotsWide = zoom;
    private int numOslotsTall = zoom;
    private int x;
    private int y;
    private int widthOfSlot;
    private int heightOfslot;
    private ArrayList<MapSlot> viewableMap = new ArrayList<MapSlot>();
    private Map mapInstance = Map.getInstance();
    public int getX(){
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZoom(){return zoom;}
    private void calculateValues(){
        numOslotsTall = zoom;
        numOslotsWide = (int)((double)numOslotsTall*screenWidthProportion);
        widthOfSlot = gameConstants.screenWidth/numOslotsWide;
        heightOfslot = gameConstants.screenHeight/numOslotsTall;
        viewableMap = mapInstance.getMapSection(x, y,numOslotsWide,numOslotsTall);
        outOfBoundsCheck();
    }
    public void increaseZoom(){
        if ((zoom < gameConstants.mapWidth-1) && (zoom < gameConstants.mapHeight-1)){
            zoom += 1;
            x-=1;
            y-=1;
            calculateValues();
        }
    }
    public void decreaseZoom(){
        if (zoom > 1){
            zoom -= 1;
            x+=1;
            y+=1;
            calculateValues();
        }
    }
    private void outOfBoundsCheck(){
        boolean outOfBounds = true;
        while(outOfBounds){
            gameConstants.DIRECTION directionOutOfBounds = mapInstance.sectionOutOfBoundsCheck(x,y,numOslotsWide,numOslotsTall);
            if (directionOutOfBounds == gameConstants.DIRECTION.NULL){
                outOfBounds=false;
            }else if (directionOutOfBounds == gameConstants.DIRECTION.RIGHT){
                x-=1;
            }else if (directionOutOfBounds == gameConstants.DIRECTION.LEFT){
                x+=1;
            }else if (directionOutOfBounds == gameConstants.DIRECTION.UP){
                y+=1;
            }else if (directionOutOfBounds == gameConstants.DIRECTION.DOWN){
                y-=1;
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
    public void move(gameConstants.DIRECTION direction){
        switch(direction) {
            case UP:
                y-=1;
                break;
            case DOWN:
                y+=1;
                break;
            case LEFT:
                x-=1;
                break;
            case RIGHT:
                x+=1;
                break;
        }
        outOfBoundsCheck();
    }
    public boolean isOnCamera(int x,int y){
        Rectangle checker = new Rectangle(this.x,this.y,numOslotsWide,numOslotsTall);
        return checker.contains(x,y);
    }
}

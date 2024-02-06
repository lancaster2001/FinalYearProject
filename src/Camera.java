import java.awt.*;
import java.awt.geom.Rectangle2D;

import java.util.ArrayList;

public class Camera {
    //singleton------------------------------------------------------
    private static Camera instance = new Camera();
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
    //-----------------------------------------------------------------
    private int zoom = gameConstants.defaultZoom;//number of tiles in width
    double screenWidthProportion = 0.0;
    private int numOslotsWide = zoom;
    private int numOslotsTall = zoom;
    private int x;
    private int y;
    private int widthOfSlot;
    private int heightOfslot;
    private ArrayList<MapSlot> viewableMap = new ArrayList<MapSlot>();
    public void setPosition(int x, int y){
        this.x = x;
        this.y = y;
        calculateValues();
    }
    public int getX(){
        return x;
    }

    public int getY() {
        return y;
    }
    public void draw(Graphics g){
        for(MapSlot slot: viewableMap){
            slot.draw(g,instance,AssetManager.getInstance());
        }
    }

    public int getZoom(){return zoom;}
    private void calculateValues(){
        numOslotsTall = zoom;
        numOslotsWide = (int)((double)numOslotsTall*screenWidthProportion);
        widthOfSlot = gameConstants.screenWidth/numOslotsWide;
        heightOfslot = gameConstants.screenHeight/numOslotsTall;
        viewableMap = GameState.getInstance().getMapInstance().getMapSection(new Rectangle2D.Double(x, y,numOslotsWide,numOslotsTall));
        outOfBoundsCheck();
    }
    public void increaseZoom(){
        if ((zoom*screenWidthProportion< gameConstants.mapWidth-1) && (zoom*screenWidthProportion < gameConstants.mapHeight-1)){
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
        Rectangle2D.Double placement = GameState.getInstance().outOfBoundsCheck(new Rectangle2D.Double(x,y,numOslotsWide,numOslotsTall));
        x = (int)placement.x;
        y = (int)placement.y;
        numOslotsWide = (int)placement.width;
        numOslotsTall = (int)placement.height;
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

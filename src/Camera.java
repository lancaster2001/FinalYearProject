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
    private final ResourceManager resourceManagerInstance = ResourceManager.getInstance();
    private int zoom = gameConstants.defaultZoom;//number of tiles in width
    double screenWidthProportion = 0.0;
    private int numOslotsWide = zoom;
    private int numOslotsTall = zoom;
    private double x;
    private double y;
    private int widthOfSlot;
    private int heightOfslot;
    private final int screenWidth = gameConstants.screenWidth;
    private final int screenHeight = gameConstants.screenHeight;
    private ArrayList<MapSlot> viewableMap = new ArrayList<MapSlot>();
    public void setPosition(double x, double y){
        this.x = x;
        this.y = y;
        calculateValues();
    }
    public double getX(){
        return x;
    }

    public double getY() {
        return y;
    }
    public void draw(Graphics g){
        if(viewableMap!=null) {
            for (MapSlot slot : viewableMap) {
                int[] xAndWidth = getOnScreenXandWidth(slot.getX());
                int[] yAndHeight = getOnScreenYandHeight(slot.getY());
                slot.draw(g,xAndWidth[0],yAndHeight[0],xAndWidth[1],yAndHeight[1],AssetManager.getInstance());
            }
        }else{
            g.setColor(gameConstants.resourceMenuTitleColour);
            g.setFont(new Font("Arial", Font.BOLD, 100));
            g.drawString("NO MAP", 100, 100);
        }
        resourceManagerInstance.draw(g,x,y,getwidthOfSlot(),getheightOfslot(),AssetManager.getInstance());
    }
    private int[] getOnScreenXandWidth (double x){
        double onCameraX = x-this.x;
        int initialExcess = getWidthExcess();
        int Excess= initialExcess;
        int nextXpos = 0;
        double accumulator = 0.00000000;
        int widthToDraw = 0;
        for(int index = 0;index<=(int)onCameraX;index++){
            nextXpos += widthToDraw;
            widthToDraw = getwidthOfSlot();
            if(Excess>0.00000000){
                if(initialExcess>numOslotsWide) {
                    widthToDraw += ((double) Excess / (double) numOslotsWide);
                    Excess -= ((double) Excess / (double) numOslotsWide);
                }else{
                    widthToDraw += 1;
                    Excess -= 1;
                }
            }
        }
        return new int[]{nextXpos+(int)((onCameraX-(int)onCameraX)*getwidthOfSlot()),widthToDraw};
    }
    public int getOnScreenX(double x) {
        return getOnScreenXandWidth(x)[0];
    }
    public int getWidthExcess() {
        int nextXpos = 0;
        double accumulator = 0.00000000;
        int widthToDraw = 0;
        for(int index = 0;index<=numOslotsWide;index++){
            nextXpos += widthToDraw;
            widthToDraw = getwidthOfSlot();
        }
        return screenWidth-(nextXpos);
    }
    public int[] getOnScreenYandHeight(double y) {
        double onCameraY = y-this.y;
        int initialExcess = getHeightExcess();
        int Excess= initialExcess;
        double ExcessAccumulator = 0.0;
        int nextYpos = 0;
        double accumulator = 0.00000000;
        int heightToDraw = 0;
        for(int index = 0;index<=(int)onCameraY;index++){
            nextYpos += heightToDraw;
            heightToDraw = getheightOfslot();
            if(Excess>0.00000000){
                if(initialExcess>numOslotsTall) {
                    heightToDraw += ((double) Excess / (double) numOslotsTall);
                    Excess -= ((double) Excess / (double) numOslotsTall);
                    ExcessAccumulator += ((double) Excess / (double) numOslotsTall) - (Excess / numOslotsTall);
                }else{
                    heightToDraw += 1;
                    Excess -= 1;
                    ExcessAccumulator += 1;
                }
            }
        }
        return new int[]{nextYpos+(int)((onCameraY-(int)onCameraY)*getheightOfslot()),heightToDraw};
    }
    public int getOnScreenY(double y) {
        return getOnScreenYandHeight(y)[0];
    }
    public int getHeightExcess() {
        int nextYpos = -getwidthOfSlot();
        double accumulator = 0.0;
        int heightToDraw = getwidthOfSlot();
        for(int index = 0;index<=numOslotsTall;index++){
            nextYpos += heightToDraw;
            heightToDraw = getheightOfslot();
        }
        return screenHeight-nextYpos;
    }
    private Rectangle getPlacementOfSlot(int x,int y) {
        int returnX = 0;
        int returnY = 0;
        int returnWidth = 0;
        int returnHeight = 0;

        int nextXpos = -getwidthOfSlot();
        int nextYpos = -getheightOfslot();
        double accumulatorX = 0.0;
        double accumulatorY = 0.0;
        double previousY = 0.0;
        double previousX = 0.0;
        for (MapSlot slot : viewableMap) {
            int widthToDraw = getwidthOfSlot();
            int heightToDraw = getheightOfslot();
            if ((int) previousX != slot.getX()) {
                accumulatorX += ((double) screenWidth / (double) numOslotsWide) - getwidthOfSlot();
                previousX = slot.getX();
                if (accumulatorX >= 1.0) {
                    accumulatorX -= 1.0;
                    widthToDraw += 1;
                }
                nextXpos += widthToDraw;
            }
            if ((int) previousY != slot.getY()) {
                accumulatorY += ((double) screenHeight / (double) numOslotsTall) - getheightOfslot();
                previousY = slot.getY();
                if (accumulatorY >= 1.0) {
                    accumulatorY -= 1.0;
                    heightToDraw += 1;
                }
                nextYpos += heightToDraw;
                nextXpos = 0;
            }
            if((slot.getX() == x)&&(slot.getY() == y)){
                returnX = nextXpos;
                returnY = nextYpos;
                returnWidth = widthToDraw;
                returnHeight = heightToDraw;
            }
        }
        return new Rectangle(returnX,returnY,returnWidth,returnHeight);
    }

    public int getZoom(){return zoom;}
    private void calculateValues(){
        numOslotsTall = zoom;
        getNumOslotsWide();
        //if(outOfBoundsCheck()){calculateValues();}


        viewableMap = GameState.getInstance().getMapInstance().getMapSection(getExcessMap(new Rectangle2D.Double(x, y,getNumOslotsWide(),numOslotsTall)));

    }
    private Rectangle2D.Double getExcessMap(Rectangle2D.Double map){
        if(map.width*getwidthOfSlot()<screenWidth){
            map.width = screenWidth/getwidthOfSlot();
        }
        if(map.height*getheightOfslot()<screenHeight){
            map.height = screenHeight/getheightOfslot();
        }
        return map;
    }
    public void increaseZoom(){
        if ((x+(zoom*screenWidthProportion)-1< gameConstants.mapWidth-1) && (y+(zoom*screenWidthProportion) < gameConstants.mapHeight-1)){
            zoom += 1;
           // x-=1;
           // y-=1;
            calculateValues();
        }
    }
    public void decreaseZoom(){
        if (zoom > 1){
            zoom -= 1;
           // x+=1;
           // y+=1;
            calculateValues();
        }
    }
    private boolean outOfBoundsCheck(){
        Rectangle2D.Double original = new Rectangle2D.Double(x,y,numOslotsWide,numOslotsTall);
        Rectangle2D.Double placement = GameState.getInstance().outOfBoundsCheck(new Rectangle2D.Double(x,y,numOslotsWide,numOslotsTall));
        x = placement.x;
        y = placement.y;
        numOslotsWide = (int)placement.width;
        numOslotsTall = (int)placement.height;
        return !original.equals(placement);
    }
    public int getheightOfslot() {
        heightOfslot = (screenHeight/numOslotsTall);
        return heightOfslot;
    }
    public int getwidthOfSlot() {
        widthOfSlot = (screenWidth/numOslotsWide);
        return widthOfSlot;
    }
    //public int getNumOslotsWide(){return numOslotsWide;}
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
        calculateValues();
    }
    public boolean isOnCamera(double x,double y){
        Rectangle2D.Double checker = new Rectangle2D.Double(this.x,this.y,numOslotsWide,numOslotsTall);
        return checker.contains(x,y);
    }
    private int getNumOslotsWide(){
        numOslotsWide = (int)((double)numOslotsTall*screenWidthProportion);
        numOslotsWide += (screenWidth - (numOslotsWide * widthOfSlot)) / getwidthOfSlot();

        return numOslotsWide;
    }
}

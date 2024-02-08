import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public final class Map {
    public Map(ArrayList<MapSlot> mapArray,int width, int height){
        mapWidth = width;
        mapHeight =height;
        this.mapArray = mapArray;
        BaseBaseTower playerbase =BaseBaseTower.getInstance();
        setTower(playerbase,(int)playerbase.getPose().getX(),(int)playerbase.getPose().getY());
    }
    private ArrayList<MapSlot> mapArray = new ArrayList<>();
    private int mapWidth = 0;
    private int mapHeight = 0;
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


    public ArrayList<MapSlot> getMapSection(Rectangle2D.Double section){
        ArrayList<MapSlot> mapSection = new ArrayList<>();
        int currentYindex = -1;
        int currentXindex = -1;
        try {
            /*for (int yIndex = (int)section.y;yIndex <(section.y+section.height);yIndex ++){
                currentYindex = yIndex;
                for (int xIndex = (int)section.x;xIndex <(section.x+section.width);xIndex ++){
                    currentXindex = xIndex;
                    mapSection.add(getSlotFromCoord(xIndex,yIndex));
                }
            }*/
            for(MapSlot currentSlot: mapArray){
                if(section.intersects(currentSlot.getX(),currentSlot.getY(),1.0,1.0)){
                    mapSection.add(currentSlot);
                }
            }
        }catch (Exception IndexOutOfBoundsException){
            System.out.println("out of bounds error at get map section\nyIndex:"+currentYindex+"\nxIndex:"+currentXindex);
            System.out.println(sectionOutOfBoundsCheck(section,false));//todo test this works
        }
        return mapSection;
    }
    public boolean intersects(Rectangle section){
        return section.intersects(1,1,mapWidth,mapHeight);
    }
    public Rectangle2D.Double sectionOutOfBoundsCheck(Rectangle2D.Double section,boolean canClipOutOfBound){
        if(section.intersects(1,1,mapWidth,mapHeight)&&canClipOutOfBound){
            return section;
        }else{
            if(section.x<1){
                section.setRect(1,section.y,section.width,section.height);
            }
            if(section.x+section.width-1>mapWidth){
                section.setRect((mapWidth+1)-section.width,section.y,section.width,section.height);
            }
            if(section.y<1){
                section.setRect(section.x,1,section.width,section.height);
            }
            if(section.y+section.height-1>mapHeight){
                section.setRect(section.x,(mapHeight+1)-section.height,section.width,section.height);
            }
            return section;
        }
    }

    public void tick(double tickMultiplier){
        for(MapSlot slot:mapArray){
            slot.tick(tickMultiplier);
        }
    }
    private void draw(Graphics g,Camera cameraInstance, AssetManager assetManagerInstance){
        for(MapSlot slot: mapArray){
            if(slot.onScreenCheck(cameraInstance)){
                //slot.draw(g,cameraInstance,assetManagerInstance);
            }
        }
    }

    public void setTower(TowerTemplate newTower, int x, int y){
        getSlotFromCoord(x,y).setTower(newTower);
    }
    public void setTower(BaseTower newTower, int x, int y){
        getSlotFromCoord(x,y).setTower(newTower);
    }

}

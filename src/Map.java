import org.json.JSONObject;

import javax.swing.plaf.ColorUIResource;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public final class Map {
    public Map(ArrayList<MapSlot> mapArray,int width, int height){
        name = String.valueOf(Math.random());
        mapWidth = width;
        mapHeight = height;
        this.mapArray = mapArray;
        BaseBaseTower playerbase =BaseBaseTower.getInstance();
        setTower(playerbase,(int)playerbase.getPose().getX(),(int)playerbase.getPose().getY());
    }
    private ArrayList<MapSlot> mapArray = new ArrayList<>();
    private String name;
    private int mapWidth = 0;
    private int mapHeight = 0;
    private MapSlot getSlotFromCoord(int x, int y){
        for(MapSlot currentSlot: mapArray){
            if((currentSlot.getX()==x)&&(currentSlot.getY()==y)){
                return currentSlot;
            }
        }
        return null;
    }


    public ArrayList<MapSlot> getMapSection(Rectangle2D.Double section){
        ArrayList<MapSlot> mapSection = new ArrayList<>();
        int currentYindex = -1;
        int currentXindex = -1;
        try {
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
    public String save(){
        String saveLink = "src/Saves/Maps/";
        try {
            Files.createDirectories(Paths.get(saveLink));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String fileName = saveLink+name+".json";
        JSONObject json = new JSONObject();
        json.put("Name", name);
        json.put("Width", mapWidth);
        json.put("Height", mapHeight);
        for(MapSlot currentSlot: mapArray) {
            json.put(""+(((currentSlot.getY()-1)*mapWidth)+(currentSlot.getX()-1)), currentSlot.getJsonObject());
        }
        try (FileWriter file = new FileWriter(fileName)) {
            file.write(json.toString());
            file.flush();
        } catch (IOException e) {
            e.fillInStackTrace();
        }

        return saveLink;
    }

    public void setTower(TowerTemplate newTower, int x, int y){
        getSlotFromCoord(x,y).setTower(newTower);
    }
    public void setTower(BaseTower newTower, int x, int y){
        getSlotFromCoord(x,y).setTower(newTower);
    }

}

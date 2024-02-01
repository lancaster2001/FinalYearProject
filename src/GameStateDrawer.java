import java.awt.*;
import java.util.ArrayList;

public class GameStateDrawer {
    //singleton-------------------------------------------------------------------------
    private static GameStateDrawer instance;

    private GameStateDrawer() {}
    public static GameStateDrawer getInstance() {
        if (instance == null) {
            instance = new GameStateDrawer();
        }
        return instance;
    }
    //----------------------------------------------------------------------------------------
    public void drawMap(Graphics g, ArrayList<MapSlot> map, int numOslotsWide, int numOslotsTall, int widthOfSlot, int heightOfslot){
        drawTiles(g,map,numOslotsWide,numOslotsTall,widthOfSlot,heightOfslot);
        drawTowers(g,map,numOslotsWide,numOslotsTall,widthOfSlot,heightOfslot);
    }

    private void drawTiles(Graphics g,ArrayList<MapSlot> map,int numOslotsWide, int numOslotsTall, int widthOfSlot, int heightOfslot){
        for (int yIndex = 0;yIndex < numOslotsTall; yIndex++){
            for(int xIndex = 0;xIndex < numOslotsWide; xIndex++){
                int index = yIndex*numOslotsWide+xIndex;
                int x = xIndex*widthOfSlot;
                int y = yIndex*heightOfslot;
                g.drawImage(map.get(index).getTile().getImage(), x, y, widthOfSlot, heightOfslot, null);
                g.setColor(Color.BLACK);
                g.drawRect(x, y, widthOfSlot, heightOfslot);
            }
        }
    }
    private void drawTowers(Graphics g,ArrayList<MapSlot> map,int numOslotsWide, int numOslotsTall, int widthOfSlot, int heightOfslot){
        for (int yIndex = 0;yIndex < numOslotsTall; yIndex++){
            for(int xIndex = 0;xIndex < numOslotsWide; xIndex++){
                int index = yIndex*numOslotsWide+xIndex;
                int x = xIndex*widthOfSlot;
                int y = yIndex*heightOfslot;
                g.drawImage(map.get(index).getTower().getImage(), x, y, widthOfSlot, heightOfslot, null);
                g.setColor(Color.BLACK);
                g.drawRect(x, y, widthOfSlot, heightOfslot);
            }
        }
    }
}

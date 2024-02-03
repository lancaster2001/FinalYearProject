import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class GameStateDrawer {
    //singleton-------------------------------------------------------------------------
    private static GameStateDrawer instance = new GameStateDrawer();

    private GameStateDrawer() {}
    public static GameStateDrawer getInstance() {
        if (instance == null) {
            instance = new GameStateDrawer();
        }
        return instance;
    }
    //----------------------------------------------------------------------------------------
    private final AssetManager assetManagerInstance = AssetManager.getInstance();
    private final Camera cameraInstance = Camera.getInstance();
    private final GameStateUI gameStateUIInstance = GameStateUI.getInstance();
    private final EnemyManager enemyManagerInstance = EnemyManager.getInstance();
    private final ProjectileManager projectileManagerInstance = ProjectileManager.getInstance();
    //private final Map mapInstance = GameState.getInstance().getMapInstance();
    public void draw(Graphics g, ArrayList<MapSlot> map, int numOslotsWide, int numOslotsTall, int widthOfSlot, int heightOfslot){
        drawMap(g,map,numOslotsWide,numOslotsTall,widthOfSlot,heightOfslot);
        GameState.getInstance().getMapInstance().draw(g,cameraInstance,assetManagerInstance);
        enemyManagerInstance.drawEnemies(g);
        projectileManagerInstance.draw(g,cameraInstance, assetManagerInstance);
        gameStateUIInstance.drawUI(g);

    }
    public void drawMap(Graphics g, ArrayList<MapSlot> map, int numOslotsWide, int numOslotsTall, int widthOfSlot, int heightOfslot){
        //drawTiles(g,map,numOslotsWide,numOslotsTall,widthOfSlot,heightOfslot);
        //drawTowers(g,map,numOslotsWide,numOslotsTall,widthOfSlot,heightOfslot);

    }

    private void drawTiles(Graphics g,ArrayList<MapSlot> map,int numOslotsWide, int numOslotsTall, int widthOfSlot, int heightOfslot){
        for (int yIndex = 0;yIndex < numOslotsTall; yIndex++){
            for(int xIndex = 0;xIndex < numOslotsWide; xIndex++){
                int index = yIndex*numOslotsWide+xIndex;
                int x = xIndex*widthOfSlot;
                int y = yIndex*heightOfslot;
                if(map.get(index).getTile() != null){
                    g.drawImage(assetManagerInstance.getImage(map.get(index).getTile().getImageLink()), x, y, widthOfSlot, heightOfslot, null);

                    g.setColor(gameConstants.resourceMenuTitleColour);
                    g.setFont(new Font("Arial", Font.BOLD, 20));
                    //g.drawString(map.get(index).getX()+", "+map.get(index).getY(), x, y + gameConstants.ResourcesMenuTitleSize);
                }
                g.setColor(Color.BLACK);
                g.drawRect(x, y, widthOfSlot, heightOfslot);
            }
        }
    }
    private void drawTowers(Graphics g,ArrayList<MapSlot> map,int numOslotsWide, int numOslotsTall, int widthOfSlot, int heightOfslot){
        for (int yIndex = 1;yIndex <= numOslotsTall; yIndex++){
            for(int xIndex = 1;xIndex <= numOslotsWide; xIndex++){
                int index = (yIndex-1)*numOslotsWide+xIndex;
                int x = (xIndex-1)*widthOfSlot;
                int y = (yIndex-1)*heightOfslot;
                if(map.get(index).getTower() != null){

                    double rotationRequired = map.get(index).getTower().getPose().getTheta();
                    BufferedImage image = assetManagerInstance.getImage(map.get(index).getTower().getImageLink());

                    Graphics2D g2d = (Graphics2D) g;
                    AffineTransform backup = g2d.getTransform();
                    AffineTransform trans = new AffineTransform();
                    trans.rotate( rotationRequired, (x+(widthOfSlot/2)), (y+(heightOfslot/2)) ); // the points to rotate around (the center in my example, your left side for your problem)

                    g2d.transform( trans );
                    g2d.drawImage( image, x, y ,widthOfSlot,heightOfslot,null);  // the actual location of the sprite

                    g2d.setTransform( backup ); // restore previous transform
                    //g.drawImage(assetManagerInstance.getImage(map.get(index).getTower().getImageLink()), x, y, widthOfSlot, heightOfslot, null);
                }
                g.setColor(Color.BLACK);
                g.drawRect(x, y, widthOfSlot, heightOfslot);
            }
        }
    }
}

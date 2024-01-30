import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;


public class GameStateUI {
    //singleton-------------------------------------------------------------------------
    private static GameStateUI instance;
    private GameStateUI(){

    }
    public static GameStateUI getInstance() {
        if (instance == null) {
            instance = new GameStateUI();
        }
        return instance;
    }
//----------------------------------------------------------------------------------------
private final AssetManager assetManagerInstance = AssetManager.getInstance();
    public void drawUI(Graphics g){
        drawReasourcesMenu(g);
        drawTitle(g);
    }
    private void drawReasourcesMenu(Graphics g){
        g.setColor(gameConstants.resourceMenuBackgroundColour);
        g.fill3DRect(gameConstants.resourcesMenuX, gameConstants.resourcesMenuY, gameConstants.resourcesMenuWidth, gameConstants.resourcesMenuHeight,true);
    }
    private void drawTitle(Graphics g){
        g.setColor(gameConstants.resourceMenuTitleColour);
        g.setFont(new Font("Arial", Font.BOLD, gameConstants.getResourcesMenuTitleSize));
        g.drawString("Resources:", gameConstants.resourcesMenuX, gameConstants.resourcesMenuY +gameConstants.getResourcesMenuTitleSize);
    }
    private void drawResources(Graphics g){

    }
    private ArrayList<BufferedImage> loadAllImages(String dirLink){
        ArrayList<BufferedImage> allImages = new ArrayList<BufferedImage>();
        File file = new File(dirLink);
        String[] directories = file.list(new FilenameFilter() {
            @Override
            public boolean accept(File current, String name) {
                return new File(current, name).isDirectory();
            }
        });
        for(String currentResource: Arrays.asList(directories)) {
            allImages.add(assetManagerInstance.getImage("src/Resources/" + currentResource + "image.png"));
        }
        return allImages;
    }
}

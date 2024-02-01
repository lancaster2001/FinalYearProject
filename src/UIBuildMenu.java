import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

public class UIBuildMenu{
    //singleton-------------------------------------------------------------------------
    private static UIBuildMenu instance;
    private UIBuildMenu() {
        loadBuildMenu();
    }

    public static UIBuildMenu getInstance() {
        if (instance == null) {
            instance = new UIBuildMenu();
        }
        return instance;
    }
    //----------------------------------------------------------------------------------------
    private final AssetManager assetManagerInstance = AssetManager.getInstance();
    private Rectangle buildMenuBackground = new Rectangle(gameConstants.buildMenux, gameConstants.buildMenuy, gameConstants.buildMenuWidth, gameConstants.buildMenuHeight);
    private ArrayList<BaseTower> buildMenu = new ArrayList<BaseTower>();
    private boolean buildMenuState = true;
    private final ArrayList<String> ImageLinksArray = new ArrayList<String>();
    private final ArrayList<BufferedImage> ImagesArray = new ArrayList<BufferedImage>();
    private int numberOfElementsInBuildMenu = gameConstants.numberOfElementsInBuildMenu;
    private int spaceBetweenBuildMenuElements = gameConstants.spaceBetweenBuildMenuElements;
    private int buildMenuElementWidth = gameConstants.buildMenuElementWidth;
    private int buildMenuElementHeight = gameConstants.buildMenuElementHeight;
    private ArrayList<Rectangle> buildMenuIconHitBox = new ArrayList<Rectangle>();
    private int selectedBuildMenuElement = -1;
    private void loadBuildMenu() {
        String dirLink = "src/Towers/";
        File file = new File(dirLink);
        String[] directories = file.list(new FilenameFilter() {
            @Override
            public boolean accept(File current, String name) {
                return new File(current, name).isDirectory();
            }
        });
        if (directories != null) {
            for (String currentResource : directories) {
                String theLink = dirLink + currentResource + "/image.png";
                ImagesArray.add(assetManagerInstance.getImage(theLink));
                ImageLinksArray.add(theLink);
            }
        }
    }
    public void drawBuildMenu(Graphics g, boolean open) {
        if (open) {
            drawBuildMenuBackground(g);
            drawBuildMenuIcons(g);
        }
    }
    private void drawBuildMenuBackground(Graphics g) {
        g.setColor(gameConstants.buildMenuBackgroundColour);
        g.fill3DRect(buildMenuBackground.x, buildMenuBackground.y, buildMenuBackground.width, buildMenuBackground.height, true);
    }

    private void drawBuildMenuIcons(Graphics g) {
        int index = 0;
        buildMenuIconHitBox.clear();
        for (BufferedImage image : ImagesArray) {
            int x = index * ((2 * spaceBetweenBuildMenuElements) + buildMenuElementWidth);
            int y = buildMenuBackground.y + spaceBetweenBuildMenuElements;
            g.drawImage(image, x, y, buildMenuElementWidth, buildMenuElementHeight, null);
            buildMenuIconHitBox.add(new Rectangle(x, y, buildMenuElementWidth, buildMenuElementHeight));
            if(selectedBuildMenuElement == (index+1)){
                g.setColor(Color.BLACK);
                g.drawRect(x, y, buildMenuElementWidth, buildMenuElementHeight);
            }
            if (index + 1 == numberOfElementsInBuildMenu) {
                break;
            }else{index += 1;}
        }
    }
    public boolean takeInput(Point p) {
        if (buildMenuState) {
            if (buildMenuBackground.contains(p)) {
                int index = 1;
                for(Rectangle currentElement: buildMenuIconHitBox){
                    boolean test = !currentElement.intersection(new Rectangle(p)).isEmpty();
                    if (currentElement.contains(p.x,p.y)){
                        selectedBuildMenuElement = index;
                        break;
                    }else {index+=1;}
                }
                return true;
            } else {
                return false;
            }
        }
        return false;
    }
    public boolean getBuildMenuState() {
        return buildMenuState;
    }
    public Rectangle getBuildMenuBackground() {
        return buildMenuBackground;
    }
    public int getSelectedBuildMenuElement() {
        return selectedBuildMenuElement;
    }
}
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
public class GameStateUI {
    //singleton-------------------------------------------------------------------------
    private static GameStateUI instance;

    private GameStateUI() {
        loadBuildMenu();
    }

    public static GameStateUI getInstance() {
        if (instance == null) {
            instance = new GameStateUI();
        }
        return instance;
    }

    //----------------------------------------------------------------------------------------
    private final AssetManager assetManagerInstance = AssetManager.getInstance();

    //todo this is temporary (probably)
    //-----------------------------------
    private final ArrayList<String> ImageLinksArray = new ArrayList<String>();
    private final ArrayList<BufferedImage> ImagesArray = new ArrayList<BufferedImage>();
    //-----------------------------------
    private ArrayList<BaseTower> buildMenu = new ArrayList<BaseTower>();
    private boolean buildMenuState = true;
    private boolean resourceMenuState = true;
    private Rectangle buildMenuBackground = new Rectangle(gameConstants.buildMenux, gameConstants.buildMenuy, gameConstants.buildMenuWidth, gameConstants.buildMenuHeight);
    private Rectangle resourceMenuBackground = new Rectangle(gameConstants.resourcesMenuX, gameConstants.resourcesMenuY, gameConstants.resourcesMenuWidth, gameConstants.resourcesMenuHeight);
    private int numberOfElementsInBuildMenu = gameConstants.numberOfElementsInBuildMenu;
    private int spaceBetweenBuildMenuElements = gameConstants.spaceBetweenBuildMenuElements;
    private int buildMenuElementWidth = gameConstants.buildMenuElementWidth;
    private int buildMenuElementHeight = gameConstants.buildMenuElementHeight;
    private ArrayList<Rectangle> buildMenuIconHitBox = new ArrayList<Rectangle>();
    private int selectedBuildMenuElement = -1;

    public void drawUI(Graphics g) {
        drawResourcesMenu(g, resourceMenuState);
        drawBuildMenu(g, buildMenuState);
    }

    private void drawResourcesMenu(Graphics g, boolean open) {
        if (open) {
            drawResourcesMenuBackground(g);
            drawResourcesMenuTitle(g);
            drawResourcesMenuResources(g);
        }
    }

    private void drawResourcesMenuBackground(Graphics g) {
        g.setColor(gameConstants.resourceMenuBackgroundColour);
        g.fill3DRect(resourceMenuBackground.x, resourceMenuBackground.y, resourceMenuBackground.width, resourceMenuBackground.height, true);
    }

    private void drawResourcesMenuTitle(Graphics g) {
        g.setColor(gameConstants.resourceMenuTitleColour);
        g.setFont(new Font("Arial", Font.BOLD, gameConstants.ResourcesMenuTitleSize));
        g.drawString("Resources:", gameConstants.resourcesMenuX, gameConstants.resourcesMenuY + gameConstants.ResourcesMenuTitleSize);
    }

    private void drawResourcesMenuResources(Graphics g) {
        ArrayList<Resource> inventory = new ArrayList<Resource>();
        inventory = ResourceManager.getInstance().getInventoryArray();
        int index = 0;
        for (Resource currentResource : inventory) {
            index += 1;
            int y = gameConstants.resourcesMenuY + (gameConstants.ResourcesMenuTitleSize * (index + 1));
            g.setFont(new Font("Arial", Font.BOLD, gameConstants.ResourcesMenuTitleSize));
            g.drawImage(currentResource.getImage(), gameConstants.resourcesMenuX, y, gameConstants.ResourcesMenuTitleSize, gameConstants.ResourcesMenuTitleSize, null);
            g.drawString(currentResource.getName() + ": " + currentResource.getQuantity(), gameConstants.resourcesMenuX + gameConstants.ResourcesMenuTitleSize, y);
        }
    }

    private void drawBuildMenu(Graphics g, boolean open) {
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

    public boolean getBuildMenuState() {
        return buildMenuState;
    }

    public boolean getresourceMenuState() {
        return resourceMenuState;
    }

    public Rectangle getBuildMenuBackground() {
        return buildMenuBackground;
    }

    public Rectangle getResourceMenuBackground() {
        return resourceMenuBackground;
    }

    public boolean takeInput(Point p) {
        if (buildMenuState) {
            if (buildMenuBackground.intersection(new Rectangle(p)).isEmpty()) {
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
}

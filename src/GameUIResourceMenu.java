import java.awt.*;
import java.util.ArrayList;

public class GameUIResourceMenu {
    //singleton-------------------------------------------------------------------------
    private static GameUIResourceMenu instance = new GameUIResourceMenu();
    private GameUIResourceMenu() {
        //loadBuildMenu();
    }

    public static GameUIResourceMenu getInstance() {
        if (instance == null) {
            instance = new GameUIResourceMenu();
        }
        return instance;
    }
    //----------------------------------------------------------------------------------------
    private final AssetManager assetManagerInstance = AssetManager.getInstance();
    private boolean resourceMenuState = true;
    private int sizeOfTitle = gameConstants.ResourcesMenuTitleSize;
    private int sizeOfResourceName = gameConstants.ResourcesMenuSizeOfResourceName;
    private Rectangle resourceMenuBackground = new Rectangle(gameConstants.resourcesMenuX, gameConstants.resourcesMenuY, gameConstants.resourcesMenuWidth, gameConstants.resourcesMenuHeight);
    private Rectangle resourceMenuButton = new Rectangle(gameConstants.resourcesMenuX-(gameConstants.resourcesMenuWidth/10), gameConstants.resourcesMenuY+(gameConstants.resourcesMenuHeight/10), gameConstants.resourcesMenuWidth/10, gameConstants.resourcesMenuHeight/7);
    public void drawResourcesMenu(Graphics g) {
        if(resourceMenuState) {
            drawResourcesMenuBackground(g);
            drawResourcesMenuTitle(g);
            drawResourcesMenuResources(g);
        }
        drawResourceMenuButton(g);
    }
    private void drawResourcesMenuBackground(Graphics g) {
        g.setColor(gameConstants.resourceMenuBackgroundColour);
        g.drawImage(assetManagerInstance.getImage("Menus","resourceMenuBackground.png"),resourceMenuBackground.x, resourceMenuBackground.y, resourceMenuBackground.width, resourceMenuBackground.height,null);
    }
    private void drawResourcesMenuTitle(Graphics g) {
        g.setColor(gameConstants.resourceMenuTitleColour);
        g.setFont(new Font("Arial", Font.BOLD, sizeOfTitle));
        g.drawString("Resources:", gameConstants.resourcesMenuX, gameConstants.resourcesMenuY + gameConstants.ResourcesMenuTitleSize);
    }

    private void drawResourcesMenuResources(Graphics g) {
        ArrayList<Resource> inventory = new ArrayList<Resource>();
        inventory = ResourceManager.getInstance().getInventoryArray();
        int index = 0;
        for (Resource currentResource : inventory) {
            index += 1;
            int y = resourceMenuBackground.y + sizeOfTitle + (sizeOfResourceName * (index));
            int width = sizeOfResourceName;
            g.setFont(new Font("Arial", Font.BOLD, sizeOfResourceName));
            g.drawImage(assetManagerInstance.getImage("Icons",currentResource.getIconImageLink()), resourceMenuBackground.x, y, width, width, null);
            g.drawString(currentResource.getName() + ": " + currentResource.getQuantity(), resourceMenuBackground.x + width, y+width);
        }
    }
    private void drawResourceMenuButton(Graphics g){
        g.setColor(gameConstants.buildMenuBackgroundColour);
        g.drawImage(assetManagerInstance.getImage("Menus","resourceMenuButton.png"),resourceMenuButton.x, resourceMenuButton.y, resourceMenuButton.width, resourceMenuButton.height,null);
    }
    public boolean takeInput(Point p) {
        if (resourceMenuButton.contains(p.x, p.y)) {
            resourceMenuState= !resourceMenuState;
            if (resourceMenuState) {
                resourceMenuButton.setLocation(resourceMenuButton.x - resourceMenuBackground.width, resourceMenuButton.y);
            } else {
                resourceMenuButton.setLocation(resourceMenuButton.x + resourceMenuBackground.width, resourceMenuButton.y);
            }
            return true;
        }
        return false;
    }

}

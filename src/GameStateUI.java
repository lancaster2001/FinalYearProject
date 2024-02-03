import java.awt.*;
import java.util.ArrayList;
public class GameStateUI {
    //singleton-------------------------------------------------------------------------
    private static GameStateUI instance = new GameStateUI();

    private GameStateUI() {}

    public static GameStateUI getInstance() {
        if (instance == null) {
            instance = new GameStateUI();
        }
        return instance;
    }

    //----------------------------------------------------------------------------------------
    private final GameUIBuildMenu GameUIBuildMenuInstance = GameUIBuildMenu.getInstance();
    private boolean resourceMenuState = true;
    private Rectangle resourceMenuBackground = new Rectangle(gameConstants.resourcesMenuX, gameConstants.resourcesMenuY, gameConstants.resourcesMenuWidth, gameConstants.resourcesMenuHeight);

    public void drawUI(Graphics g) {
        drawResourcesMenu(g, resourceMenuState);
        GameUIBuildMenuInstance.drawBuildMenu(g);
    }

    private void drawResourcesMenu(Graphics g, boolean open) {
        drawResourcesMenuBackground(g);
        drawResourcesMenuTitle(g);
        drawResourcesMenuResources(g);
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


    public boolean takeInput(Point p) {
        return GameUIBuildMenuInstance.takeInput(p);
    }


}

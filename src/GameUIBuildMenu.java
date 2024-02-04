import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class GameUIBuildMenu {
    //singleton-------------------------------------------------------------------------
    private static GameUIBuildMenu instance = new GameUIBuildMenu();
    private GameUIBuildMenu() {
        loadBuildMenu();
    }

    public static GameUIBuildMenu getInstance() {
        if (instance == null) {
            instance = new GameUIBuildMenu();
        }
        return instance;
    }
    //----------------------------------------------------------------------------------------
    private final AssetManager assetManagerInstance = AssetManager.getInstance();
    private final TowerManager towerManagerInstance = TowerManager.getInstance();
    private final Rectangle buildMenuBackground = new Rectangle(gameConstants.buildMenux, gameConstants.buildMenuy, gameConstants.buildMenuWidth, gameConstants.buildMenuHeight);
    private ArrayList<BaseTower> buildMenu = new ArrayList<BaseTower>();
    private boolean buildMenuState = true;
    private Rectangle buildMenuButton = new Rectangle(buildMenuBackground.width-(buildMenuBackground.width/20), buildMenuBackground.y-(buildMenuBackground.height/7), buildMenuBackground.width/20, buildMenuBackground.height/7);
    private final int numberOfElementsInBuildMenu = gameConstants.numberOfElementsInBuildMenu;
    private final int spaceBetweenBuildMenuElements = gameConstants.spaceBetweenBuildMenuElements;
    private final int buildMenuElementWidth = gameConstants.buildMenuElementWidth;
    private final int buildMenuElementHeight = gameConstants.buildMenuElementHeight;
    private ArrayList<Rectangle> buildMenuIconHitBox = new ArrayList<>();
    private ArrayList<TowerTemplate> towerArrayList = new ArrayList<>();
    private ArrayList<TowerTemplate> displayTowerArrayList = new ArrayList<>();;
    private int selectedMenuElement = -1;
    private int selectedMenuList = -1;
    private String[] menuListsNames = {"Turret","Drill"};
    private void loadBuildMenu() {
        towerArrayList = towerManagerInstance.getTowerArrayList();
        selectedMenuList = 0;
        setDisplayList();
    }
    public void drawBuildMenu(Graphics g) {
        if(buildMenuState) {
            drawBuildMenuBackground(g);
            drawBuildMenuIcons(g);
        }
        drawBuildMenuButton(g);

    }
    private void drawBuildMenuButton(Graphics g){
        g.setColor(gameConstants.buildMenuBackgroundColour);
        g.fill3DRect(buildMenuButton.x, buildMenuButton.y, buildMenuButton.width, buildMenuButton.height, true);
    }
    private void drawBuildMenuBackground(Graphics g) {
        g.setColor(gameConstants.buildMenuBackgroundColour);
        g.fill3DRect(buildMenuBackground.x, buildMenuBackground.y, buildMenuBackground.width, buildMenuBackground.height, true);
    }
    private void setDisplayList(){
        displayTowerArrayList.clear();

        for (TowerTemplate tower : towerArrayList) {
            if(tower.getType().equals(menuListsNames[selectedMenuList])){
                displayTowerArrayList.add(tower);
            }
        }
    }
    private void drawBuildMenuIcons(Graphics g) {
        int index = 0;
        buildMenuIconHitBox.clear();
        for (TowerTemplate tower : displayTowerArrayList) {
            int x = index * ((2 * spaceBetweenBuildMenuElements) + buildMenuElementWidth);
            int y = buildMenuBackground.y + spaceBetweenBuildMenuElements;
            g.drawImage(assetManagerInstance.getImage(tower.getImageLink()), x, y, buildMenuElementWidth, buildMenuElementHeight, null);
            buildMenuIconHitBox.add(new Rectangle(x, y, buildMenuElementWidth, buildMenuElementHeight));
            if(selectedMenuElement == (index)){
                g.setColor(Color.BLACK);
                g.drawRect(x, y, buildMenuElementWidth, buildMenuElementHeight);
            }
            if (index + 1 == numberOfElementsInBuildMenu) {
                break;
            }else{index += 1;}
        }
    }
    public boolean takeInput(Point p) {
        if(buildMenuButton.contains(p.x,p.y)) {
            buildMenuState = !buildMenuState;
            if(buildMenuState){
                buildMenuButton.setLocation(buildMenuButton.x,buildMenuButton.y-buildMenuBackground.height);
            }else{
                buildMenuButton.setLocation(buildMenuButton.x,buildMenuButton.y+buildMenuBackground.height);
            }
            return true;
        }

        if (buildMenuState) {
            if (buildMenuBackground.contains(p)) {
                int index = 0;
                for(Rectangle currentElement: buildMenuIconHitBox){
                    boolean test = !currentElement.intersection(new Rectangle(p)).isEmpty();
                    if (currentElement.contains(p.x,p.y)){
                        selectedMenuElement = index;
                        break;
                    } else {index+=1;}
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
        return selectedMenuElement;
    }
    public TowerTemplate getSelectedTower(){
        return displayTowerArrayList.get(selectedMenuElement);
    }
}
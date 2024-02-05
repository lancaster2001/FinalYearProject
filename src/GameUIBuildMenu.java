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
    private ArrayList<Rectangle> MenuListsButtons = new ArrayList<>();
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
        MenuListsButtons();
    }
    private void MenuListsButtons(){
        for(int index = 0;index<menuListsNames.length;index++){
            int width = ((buildMenuBackground.width-buildMenuButton.width)/menuListsNames.length);
            int height = buildMenuButton.height;
            int x = index*width;
            int y = buildMenuBackground.y-height;
            MenuListsButtons.add(new Rectangle(x,y,width,height));
        }

    }
    public void drawBuildMenu(Graphics g) {
        if(buildMenuState) {
            drawBuildMenuBackground(g);
            drawBuildMenuIcons(g);
            drawMenuListsButtons(g);
        }
        drawBuildMenuButton(g);

    }
    private void drawMenuListsButtons(Graphics g){
        for(Rectangle button: MenuListsButtons){
            g.setColor(gameConstants.buildMenuBackgroundColour);
            g.fill3DRect(button.x, button.y, button.width, button.height, true);
        }
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
            g.drawImage(assetManagerInstance.getImage("Towers",tower.getImageLink()), x, y, buildMenuElementWidth, buildMenuElementHeight, null);
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
            if (buildMenuIconCheck(p)) {
                return true;
            } else if(MenuListsButtonsCheck(p)){
                setDisplayList();
                return true;
            }else{
                return false;
            }
        }
        return false;
    }
    private boolean buildMenuIconCheck(Point p){
        boolean check = false;
        int index = 0;
        for(Rectangle currentElement: buildMenuIconHitBox){
            boolean test = !currentElement.intersection(new Rectangle(p)).isEmpty();
            if (currentElement.contains(p.x,p.y)){
                selectedMenuElement = index;
                check = true;
                break;
            } else {index+=1;}
        }
        return check;
    }
    private boolean MenuListsButtonsCheck(Point p) {
        boolean check = false;
        int index = 0;
        for(Rectangle button: MenuListsButtons){
            if (button.contains(p)) {
                selectedMenuList = index;
                check = true;
                break;
            }
            index+=1;
        }
        return check;
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
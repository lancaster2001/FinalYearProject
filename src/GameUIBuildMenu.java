import java.awt.*;
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
    private int buildMenuWidth = gameSettings.getInstance().getScreenWidth();
    private int buildMenuHeight = gameSettings.getInstance().getScreenHeight() / 7;
    private int buildMenux = 0;
    private int buildMenuy = gameSettings.getInstance().getScreenHeight() - buildMenuHeight;
    private final Rectangle buildMenuBackground = new Rectangle(buildMenux, buildMenuy, buildMenuWidth, buildMenuHeight);
    private ArrayList<Rectangle> MenuListsButtons = new ArrayList<>();
    private TowerTemplate toolTip = null;
    private boolean buildMenuState = true;
    private Rectangle buildMenuButton = new Rectangle(buildMenuBackground.width - (buildMenuBackground.width / 20), buildMenuBackground.y - (buildMenuBackground.height / 7), buildMenuBackground.width / 20, buildMenuBackground.height / 7);
    private final int numberOfElementsInBuildMenu = buildMenuWidth / ((buildMenuHeight / 10) * 12);
    private final int spaceBetweenBuildMenuElements = (buildMenuHeight / 10);
    private final int buildMenuElementWidth = ((buildMenuHeight / 10) * 8);
    private final int buildMenuElementHeight = ((buildMenuHeight / 10) * 8);
    private Color backgroundColour = Color.white;
    private final int toolTipWidth = 300;
    private final int toolTipHeight = 300;
    private ArrayList<Rectangle> buildMenuIconHitBox = new ArrayList<>();
    private ArrayList<TowerTemplate> towerArrayList = new ArrayList<>();
    private ArrayList<TowerTemplate> displayTowerArrayList = new ArrayList<>();
    ;
    private int selectedMenuElement = -1;
    private int selectedMenuList = -1;
    private String[] menuListsNames = {"Turret", "Drill", "Support"};

    private void loadBuildMenu() {
        towerArrayList = towerManagerInstance.getTowerArrayList();
        selectedMenuList = 0;
        setDisplayList();
        MenuListsButtons();
    }

    private void MenuListsButtons() {
        for (int index = 0; index < menuListsNames.length; index++) {
            int width = ((buildMenuBackground.width - buildMenuButton.width) / menuListsNames.length);
            int height = buildMenuButton.height;
            int x = index * width;
            int y = buildMenuBackground.y - height;
            MenuListsButtons.add(new Rectangle(x, y, width, height));
        }

    }

    public void drawBuildMenu(Graphics g) {
        if (buildMenuState) {
            drawBuildMenuBackground(g);
            drawBuildMenuIcons(g);
            drawMenuListsButtons(g);
            if(checkForToolTip(GameStateInputHandler.getInstance().getCurrentMouseLocation())){
                drawToolTip(g);
            }
        }
        drawBuildMenuButton(g);

    }

    private void drawMenuListsButtons(Graphics g) {
        int index = 0;
        for (Rectangle button : MenuListsButtons) {
            g.setColor(Color.white);
            if (selectedMenuList == index) {
                g.setColor(Color.gray);
            }
            g.fill3DRect(button.x, button.y, button.width, button.height, true);
            if (button.width > button.height) {
                g.drawImage(assetManagerInstance.getImage("Icons", menuListsNames[index] + ".png"), button.x + (button.width / 2) - (button.height / 2), button.y, button.height, button.height, null);

            } else {
                g.drawImage(assetManagerInstance.getImage("Icons", menuListsNames[index] + ".png"), button.x, button.y + (button.height / 2) - (button.width / 2), button.width, button.width, null);
            }
            index += 1;
        }
    }

    private void drawBuildMenuButton(Graphics g) {
        g.setColor(backgroundColour);
        g.fill3DRect(buildMenuButton.x, buildMenuButton.y, buildMenuButton.width, buildMenuButton.height, true);
        g.drawImage(assetManagerInstance.getImage("Menus", "buildmenuButton.png"), buildMenuButton.x, buildMenuButton.y, buildMenuButton.width, buildMenuButton.height, null);
    }

    private void drawBuildMenuBackground(Graphics g) {
        g.setColor(backgroundColour);
        g.drawImage(assetManagerInstance.getImage("Menus", "buildmenubackground.png"), buildMenuBackground.x, buildMenuBackground.y, buildMenuBackground.width, buildMenuBackground.height, null);
    }

    private void setDisplayList() {
        displayTowerArrayList.clear();
        selectedMenuElement = -1;

        for (TowerTemplate tower : towerArrayList) {
            if (tower.getBuildMenuList().equalsIgnoreCase(menuListsNames[selectedMenuList])) {
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
            g.drawImage(assetManagerInstance.getImage("Towers", tower.getImageLink()), x, y, buildMenuElementWidth, buildMenuElementHeight, null);
            buildMenuIconHitBox.add(new Rectangle(x, y, buildMenuElementWidth, buildMenuElementHeight));
            if (selectedMenuElement == (index)) {
                g.setColor(Color.BLACK);
                g.drawRect(x, y, buildMenuElementWidth, buildMenuElementHeight);
            }
            if (index + 1 == numberOfElementsInBuildMenu) {
                break;
            } else {
                index += 1;
            }
        }
    }

    public boolean takeInput(Point p) {
        if (buildMenuButton.contains(p.x, p.y)) {
            buildMenuState = !buildMenuState;
            if (buildMenuState) {
                buildMenuButton.setLocation(buildMenuButton.x, buildMenuButton.y - buildMenuBackground.height);
            } else {
                buildMenuButton.setLocation(buildMenuButton.x, buildMenuButton.y + buildMenuBackground.height);
            }
            return true;
        }

        if (buildMenuState) {
            if (buildMenuIconCheck(p)) {
                return true;
            } else if (MenuListsButtonsCheck(p)) {
                setDisplayList();
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    private boolean buildMenuIconCheck(Point p) {
        boolean check = false;
        int index = 0;
        for (Rectangle currentElement : buildMenuIconHitBox) {
            boolean test = !currentElement.intersection(new Rectangle(p)).isEmpty();
            if (currentElement.contains(p.x, p.y)) {
                selectedMenuElement = index;
                check = true;
                break;
            } else {
                index += 1;
            }
        }
        return check;
    }

    private boolean MenuListsButtonsCheck(Point p) {
        boolean check = false;
        int index = 0;
        for (Rectangle button : MenuListsButtons) {
            if (button.contains(p)) {
                selectedMenuList = index;
                check = true;
                break;
            }
            index += 1;
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

    public TowerTemplate getSelectedTower() {
        if (selectedMenuElement != -1) {
            return displayTowerArrayList.get(selectedMenuElement);
        } else {
            return null;
        }
    }
    public void clearSelectedElement(){
        selectedMenuElement = -1;
    }

    private boolean checkForToolTip(Point p){
        if(p== null){
            return false;
        }
        if(buildMenuBackground.contains(p)){
            int index = 0;
            for(Rectangle icon:buildMenuIconHitBox){
                if(icon.contains(p)){
                     toolTip = displayTowerArrayList.get(index);
                    return true;
                }
                index+=1;
            }
        }
        return false;
    }

    private void drawToolTip(Graphics g){
        Point currentMouseLocation = GameStateInputHandler.getInstance().getCurrentMouseLocation();
        if(currentMouseLocation!=null) {
            int ySpaceLeft =toolTipHeight;
            //background
            g.drawImage(assetManagerInstance.getImage("Menus", "tooltipbackground.png"), currentMouseLocation.x, currentMouseLocation.y-toolTipHeight, toolTipWidth, toolTipHeight, null);

            //icon
            int iconScale = toolTipWidth/5;
            int iconpadding = 15;
            Rectangle icon = new Rectangle(currentMouseLocation.x+toolTipWidth/iconpadding, (currentMouseLocation.y-toolTipHeight)+toolTipWidth/iconpadding, iconScale, iconScale);
            g.drawImage(assetManagerInstance.getImage("Towers", toolTip.getImageLink()), icon.x,icon.y,icon.width,icon.height, null);

            //title
            int titleScale = 20;
            g.setColor(Color.black);
            g.setFont(new Font("Arial", Font.BOLD, titleScale));
            g.drawString(toolTip.getName(), icon.x+icon.width+toolTipWidth/10,icon.y+titleScale+icon.height/10);

            //cost
            int costIconScale = toolTipWidth/10;
            Rectangle costIcon = new Rectangle(icon.x, icon.y+icon.height+ toolTipHeight/20,costIconScale,costIconScale );
            g.drawImage(assetManagerInstance.getImage("icons", toolTip.getCostResource()+".png"), costIcon.x,costIcon.y,costIcon.width,costIcon.height, null);
            int resourceCostScale = costIconScale;
            g.setColor(Color.black);
            g.setFont(new Font("Arial", Font.BOLD, titleScale));
            g.drawString(toolTip.getCostQuantity()+" "+toolTip.getCostResource(), costIcon.x+costIcon.width,costIcon.y+costIcon.height);
            ySpaceLeft = currentMouseLocation.y - costIcon.y+costIcon.height;



        }
    }
    public boolean hasSelectedTower(){
        if(selectedMenuElement<=0){
            return false;
        }
        return true;
    }
}
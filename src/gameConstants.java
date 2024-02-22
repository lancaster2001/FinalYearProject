import java.awt.*;

public interface gameConstants {
    boolean debugging = true;
    boolean outlineSlots = false;
    boolean autosave = false;

    //FILE LOCATIONS----------------------------------------------------------------------------------------
    String assetsPath = "src/Assets/";
    String errorImagePath = "src/Assets/error.png";
    String towersPath = "src/Assets/Towers/";
    String iconsPath = "src/Assets/Icons/";
    String projectilesPath = "src/Assets/Projectiles/";
    String enemiesPath = "src/Assets/Units/";
    String blocksPath = "src/Assets/Blocks/";
    //FRAME-------------------------------------------------------------------------------------------------
    GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    int screenWidth = gd.getDisplayMode().getWidth();
    int screenHeight = gd.getDisplayMode().getHeight();
    Dimension screenSize = new Dimension(screenWidth, screenHeight);
    //CONTROLS----------------------------------------------------------------------------------------------
    char moveCameraUp = 'w';
    char moveCameraDown = 's';
    char moveCameraLeft = 'a';
    char moveCameraRight = 'd';
    char pauseButton = ' ';
    //CAMERA-------------------------------------------------------------------------------------------------
    int defaultZoom = 5;
    int[] defaultCameraCoordinates = {2, 2};
    //MAP-------------------------------------------------------------------------------------------------------
    int mapWidth = 100;
    int mapHeight = 100;
    int mapSize = mapWidth * mapHeight;// 5x5 map should be 5*5

    //ENUMS--------------------------------------------------------------------------------------------------
    enum STATE {GAME, STARTMENU, GAMEOVER}

    ;

    enum DIRECTION {UP, DOWN, LEFT, RIGHT, NULL}

    ;
    //Game Settings-------------------------------------------------------------------------------------------
    int gameTickRate = 25;//number of milliseconds per tick
    //UI------------------------------------------------------------------------------------------------------
    int resourcesMenuWidth = screenWidth / 9;
    int resourcesMenuHeight = screenHeight / 4;
    int ResourcesMenuTitleSize = 20;
    int ResourcesMenuSizeOfResourceName = 30;
    int resourcesMenuX = screenWidth - resourcesMenuWidth;
    int resourcesMenuY = 0;
    Color resourceMenuBackgroundColour = Color.WHITE;
    Color resourceMenuTitleColour = Color.black;
    int buildMenuWidth = screenWidth;
    int buildMenuHeight = screenHeight / 7;
    int buildMenux = 0;
    int buildMenuy = screenHeight - buildMenuHeight;
    Color buildMenuBackgroundColour = Color.white;
    int numberOfElementsInBuildMenu = buildMenuWidth / ((buildMenuHeight / 10) * 12);
    int spaceBetweenBuildMenuElements = (buildMenuHeight / 10);
    int buildMenuElementWidth = ((buildMenuHeight / 10) * 8);
    int buildMenuElementHeight = ((buildMenuHeight / 10) * 8);
    //MAIN MENU----------------------------------------------------------------------------------------------
    Color mainMenuBackgroundColor = Color.white;
}
import java.awt.*;
public interface gameConstants {
    //FRAME-------------------------------------------------------------------------------------------------
    GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    int screenWidth = gd.getDisplayMode().getWidth();
    int screenHeight = gd.getDisplayMode().getHeight();
    Dimension screenSize = new Dimension(screenWidth, screenHeight);

    //CAMERA-------------------------------------------------------------------------------------------------
    int defaultZoom = 20;
    int[] defaultCameraCoordinates = {2,2};
    //MAP-------------------------------------------------------------------------------------------------------
    int mapWidth = 100;
    int mapHeight = 100;
    int mapSize = mapWidth * mapHeight;// 5x5 map should be 5*5
    //ENUMS--------------------------------------------------------------------------------------------------
    enum STATE {GAME, STARTMENU, GAMEOVER};
    enum DIRECTION {UP, DOWN, LEFT, RIGHT, NULL};
    //view----------------------------------------------------------------------------------------------------
    //UI------------------------------------------------------------------------------------------------------
    int resourcesMenuWidth = screenWidth / 10;
    int resourcesMenuHeight = screenHeight / 5;
    int ResourcesMenuTitleSize = 13;
    int resourcesMenuX = screenWidth - resourcesMenuWidth;
    int resourcesMenuY = 0;
    Color resourceMenuBackgroundColour = Color.WHITE;
    Color resourceMenuTitleColour = Color.black;
    int buildMenuWidth = screenWidth;
    int buildMenuHeight = screenHeight / 7;
    int buildMenux = 0;
    int buildMenuy = screenHeight-buildMenuHeight;
    Color buildMenuBackgroundColour = Color.white;
    int numberOfElementsInBuildMenu = buildMenuWidth/((buildMenuHeight/10)*12);
    int spaceBetweenBuildMenuElements = (buildMenuHeight/10);
    int buildMenuElementWidth = ((buildMenuHeight/10)*8);
    int buildMenuElementHeight = ((buildMenuHeight/10)*8);
}
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
    int getResourcesMenuTitleSize = 13;
    int resourcesMenuX = screenWidth - resourcesMenuWidth;
    int resourcesMenuY = 0;
    Color resourceMenuBackgroundColour = Color.WHITE;
    Color resourceMenuTitleColour = Color.black;
}
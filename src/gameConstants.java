import java.awt.*;
public interface gameConstants {
    //FRAME-------------------------------------------------------------------------------------------------
    GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    int screenWidth = gd.getDisplayMode().getWidth();
    int screenHeight = gd.getDisplayMode().getHeight();
    Dimension screenSize = new Dimension(screenWidth, screenHeight);
    int defaultZoom = 20;

    //MAP-------------------------------------------------------------------------------------------------------
    int mapWidth = 100;
    int mapHeight = 100;
    int mapSize = mapWidth * mapHeight;// 5x5 map should be 5*5
    //ENUMS--------------------------------------------------------------------------------------------------
    enum STATE {GAME, STARTMENU, GAMEOVER};
    enum DIRECTION {UP, DOWN, LEFT, RIGHT, NULL};
    //view----------------------------------------------------------------------------------------------------
    //UI------------------------------------------------------------------------------------------------------
    int reasourcesMenuWidth = screenWidth / 10;
    int reasourcesMenuHeight = screenHeight / 5;
    int reasourcesMenuX = screenWidth - reasourcesMenuWidth;
    int reasourcesMenuY = 0;
    Color reasourceMenuBackgroundColour = Color.WHITE;

}
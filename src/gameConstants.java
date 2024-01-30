import java.awt.*;
public interface gameConstants {
    //FRAME-------------------------------------------------------------------------------------------------
    GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    int screenWidth = gd.getDisplayMode().getWidth();
    int screenHeight = gd.getDisplayMode().getHeight();
    Dimension screenSize = new Dimension(screenWidth,screenHeight);

    //MAP-------------------------------------------------------------------------------------------------------
    int mapWidth = 10;
    int mapHeight = 10;
    int mapSize = mapWidth*mapHeight;// 5x5 map should be 5*5
    //ENUMS--------------------------------------------------------------------------------------------------
    enum STATE {GAME,STARTMENU,GAMEOVER};
    enum DIRECTION {UP,DOWN,LEFT,RIGHT,NULL};

    //view----------------------------------------------------------------------------------------------------

}
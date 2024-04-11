import java.awt.*;

public final class GameSettings {
    //todo: save & load this data to and from a .txt file
    //singleton------------------------------------------------------------------------
    private static GameSettings instance = new GameSettings();
    public static GameSettings getInstance() {
        if (instance == null) {
            instance = new GameSettings();
        }
        return instance;
    }
    private GameSettings() {
    }
    //----------------------------------------------------------------------------------
    private boolean debugging = true;
    private boolean outlineSlots = false;
    private boolean autosave = true;
    public boolean isDebugging() {
        return debugging;
    }
    public boolean isOutlineSlots() {
        return outlineSlots;
    }
    public boolean isAutosave() {
        return autosave;
    }
    public void toggledebug(){
        debugging= !debugging;
    }

    //FILE LOCATIONS----------------------------------------------------------------------------------------
    final String assetsPath = "src/Assets/";
    final String errorImagePath = "src/Assets/error.png";
    final String savesPath= "src/Saves/";
    final String saveSlotFileLocation = "src/Settings/menuSettings.json";
    //FRAME-------------------------------------------------------------------------------------------------
    private GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    private int screenWidth = gd.getDisplayMode().getWidth();
    private int screenHeight = gd.getDisplayMode().getHeight();
    public int getScreenWidth() {
        return screenWidth;
    }
    public int getScreenHeight() {
        return screenHeight;
    }
    //CONTROLS----------------------------------------------------------------------------------------------
    final char moveCameraUp = 'w';
    final char moveCameraDown = 's';
    final char moveCameraLeft = 'a';
    final char moveCameraRight = 'd';
    final char pauseButton = ' ';
    final char rotateTower = 'r';
    final char debugKey = ';';
    //CAMERA-------------------------------------------------------------------------------------------------
    final int defaultZoom = 5;
    //MAP-------------------------------------------------------------------------------------------------------
    //todo: implement a menu for map creation so that default map values are not needed in this section
    private int mapWidth = 200;
    private int mapHeight = 200;

    public int getMapWidth() {
        return mapWidth;
    }
    public int getMapHeight() {
        return mapHeight;
    }

    //ENUMS--------------------------------------------------------------------------------------------------
    enum STATE {GAME, STARTMENU, GAMEOVER}
    enum DIRECTION {UP, DOWN, LEFT, RIGHT, NULL}
    //Game Settings-------------------------------------------------------------------------------------------
    private int gameTickRate = 25;//number of milliseconds per tick
    public int getGameTickRate() {
        return gameTickRate;
    }


}
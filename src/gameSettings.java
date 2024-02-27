import java.awt.*;

public class gameSettings {
    //singleton------------------------------------------------------------------------
    private static gameSettings instance = new gameSettings();
    public static gameSettings getInstance() {
        if (instance == null) {
            instance = new gameSettings();
        }
        return instance;
    }
    private gameSettings() {
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

    //FILE LOCATIONS----------------------------------------------------------------------------------------
    final String assetsPath = "src/Assets/";
    final String errorImagePath = "src/Assets/error.png";
    final String savesPath= "src/Saves/";
    final String saveSlotFileLocation = "src/Settings/menuSettings.json";
    //FRAME-------------------------------------------------------------------------------------------------
    GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
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
    //CAMERA-------------------------------------------------------------------------------------------------
    final int defaultZoom = 5;
    final int[] defaultCameraCoordinates = {2, 2};
    //MAP-------------------------------------------------------------------------------------------------------
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
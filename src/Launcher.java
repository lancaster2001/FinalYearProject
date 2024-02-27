public class Launcher {
    //singleton------------------------------------------------------------------------
    private static Launcher instance = new Launcher();

    public static Launcher getInstance() {
        if (instance == null) {
            instance = new Launcher();
        }
        return instance;
    }

    private Launcher() {
        System.setProperty("sun.java2d.uiScale", "1.0");
        gameSettings.getInstance();
        StateManager.getInstance().setCurrentState(gameSettings.STATE.STARTMENU);
    }

    //----------------------------------------------------------------------------------
    private final MainFrame frameInstance = MainFrame.getInstance();

    public static void main(String[] args) {
        Launcher theLauncher = Launcher.getInstance();
    }


}

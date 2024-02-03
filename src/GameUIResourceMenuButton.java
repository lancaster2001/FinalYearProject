public class GameUIResourceMenuButton {
    //singleton-------------------------------------------------------------------------
    private static GameUIResourceMenuButton instance = new GameUIResourceMenuButton();
    private GameUIResourceMenuButton() {
        //loadBuildMenu();
    }

    public static GameUIResourceMenuButton getInstance() {
        if (instance == null) {
            instance = new GameUIResourceMenuButton();
        }
        return instance;
    }
    //----------------------------------------------------------------------------------------

}

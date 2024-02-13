public class StartMenu {
    //singleton------------------------------------------------------------------------
    private static StartMenu instance = new StartMenu();

    public static StartMenu getInstance() {
        if (instance == null) {
            instance = new StartMenu();
        }
        return instance;
    }

    private StartMenu() {
        StateManager stateManagerInstance = StateManager.getInstance();
        stateManagerInstance.setCurrentState(gameConstants.STATE.GAME);
    }
    //----------------------------------------------------------------------------------
}

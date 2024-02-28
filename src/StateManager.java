public class StateManager {
    //singleton------------------------------------------------------------------------
    private static StateManager instance = new StateManager();

    public static StateManager getInstance() {
        if (instance == null) {
            instance = new StateManager();
        }
        return instance;
    }

    private StateManager() {

    }

    //----------------------------------------------------------------------------------
    gameSettings.STATE currentState;
    GameState gameStateInstance;

    public void setCurrentState(gameSettings.STATE givenState) {
        currentState = givenState;
        if (currentState.equals(gameSettings.STATE.GAME)) {
            gameStateInstance = GameState.getInstance();
            gameStateInstance.startup();
        } else if (currentState.equals(gameSettings.STATE.STARTMENU)) {
            MenuState.getInstance().startup();

        } else if (currentState.equals(gameSettings.STATE.GAMEOVER)) {
            MenuState.getInstance().showErrorMessage("Game Over");
            currentState = gameSettings.STATE.STARTMENU;
        }
    }

    public gameSettings.STATE getCurrentState() {
        return currentState;
    }
}

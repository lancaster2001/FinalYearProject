public final class StateManager {
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
    GameSettings.STATE currentState;
    GameState gameStateInstance;

    public void setCurrentState(GameSettings.STATE givenState) {
        currentState = givenState;
        if (currentState.equals(GameSettings.STATE.GAME)) {
            gameStateInstance = GameState.getInstance();
            gameStateInstance.startup();
        } else if (currentState.equals(GameSettings.STATE.STARTMENU)) {
            MenuState.getInstance().startup();

        } else if (currentState.equals(GameSettings.STATE.GAMEOVER)) {
            MenuState.getInstance().showErrorMessage("Game Over");
            currentState = GameSettings.STATE.STARTMENU;
        }
    }

    public GameSettings.STATE getCurrentState() {
        return currentState;
    }
}

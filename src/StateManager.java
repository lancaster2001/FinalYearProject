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
    gameConstants.STATE currentState;
    GameState gameStateInstance;

    public void setCurrentState(gameConstants.STATE givenState) {
        currentState = givenState;
        if (currentState.equals(gameConstants.STATE.GAME)) {
            gameStateInstance = GameState.getInstance();
            gameStateInstance.startup();
        } else if (currentState.equals(gameConstants.STATE.STARTMENU)) {
            MenuState.getInstance();
        }
    }

    public gameConstants.STATE getCurrentState() {
        return currentState;
    }
}

public class StateManager {
    //singleton------------------------------------------------------------------------
    private static StateManager instance;
    public static StateManager getInstance(){
        if (instance == null) {
            instance = new StateManager();
        }
        return instance;
    }
    private StateManager(){

    }
    //----------------------------------------------------------------------------------
    gameConstants.STATE currentState = gameConstants.STATE.GAME;

    public void setCurrentState(gameConstants.STATE givenState) {
        currentState = givenState;
    }

    public gameConstants.STATE getCurrentState() {
        return currentState;
    }
}

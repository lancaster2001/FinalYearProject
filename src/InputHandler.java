import java.awt.event.*;
public class InputHandler {
    //singleton------------------------------------------------------------------------
    private static InputHandler instance = new InputHandler();
    public static InputHandler getInstance(){
        if (instance == null) {
            instance = new InputHandler();
        }
        return instance;
    }
    private InputHandler(){}
    //----------------------------------------------------------------------------------
    private final StateManager stateManagerInstance = StateManager.getInstance();
    private  final  GameStateInputHandler gameStateInputHandlerInstance = GameStateInputHandler.getInstance();
    private final MenuState menuStateInstance = MenuState.getInstance();
    public void userInput(KeyEvent e){
        if (stateManagerInstance.getCurrentState().equals(gameConstants.STATE.GAME)){
            gameStateInputHandlerInstance.userInput(e);
        } else if (stateManagerInstance.getCurrentState().equals(gameConstants.STATE.STARTMENU)){
            menuStateInstance.userInput(e);
        }
    }
    public void userInput(MouseWheelEvent e){
        if (stateManagerInstance.getCurrentState().equals(gameConstants.STATE.GAME)){
            gameStateInputHandlerInstance.userInput(e);
        } else if (stateManagerInstance.getCurrentState().equals(gameConstants.STATE.STARTMENU)){
            menuStateInstance.userInput(e);
        }
    }
    public void userInput(MouseEvent e){
        if (stateManagerInstance.getCurrentState().equals(gameConstants.STATE.GAME)){
            gameStateInputHandlerInstance.userInput(e);
        } else if (stateManagerInstance.getCurrentState().equals(gameConstants.STATE.STARTMENU)){
            menuStateInstance.userInput(e);
        }
    }
}

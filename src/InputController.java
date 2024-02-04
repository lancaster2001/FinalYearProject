import java.awt.event.*;
public class InputController {
    //singleton------------------------------------------------------------------------
    private static InputController instance = new InputController();
    public static InputController getInstance(){
        if (instance == null) {
            instance = new InputController();
        }
        return instance;
    }
    private InputController(){}
    //----------------------------------------------------------------------------------
    private final StateManager stateManagerInstance = StateManager.getInstance();
    private  final  GameStateInputHandler gameStateInputHandlerInstance = GameStateInputHandler.getInstance();
    public void userInput(KeyEvent e){
       GameState.getInstance().userInput(e);
    }
    public void userInput(MouseWheelEvent e){
        if (stateManagerInstance.getCurrentState() == gameConstants.STATE.GAME){
            gameStateInputHandlerInstance.userInput(e);
        }
    }
    public void userInput(MouseEvent e){
        gameStateInputHandlerInstance.userInput(e);
    }
}

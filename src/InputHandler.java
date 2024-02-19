import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

public class InputHandler {
    //singleton------------------------------------------------------------------------
    private static InputHandler instance = new InputHandler();

    public static InputHandler getInstance() {
        if (instance == null) {
            instance = new InputHandler();
        }
        return instance;
    }

    private InputHandler() {
    }

    //----------------------------------------------------------------------------------
    private final StateManager stateManagerInstance = StateManager.getInstance();
    private final GameStateInputHandler gameStateInputHandlerInstance = GameStateInputHandler.getInstance();
    private final MenuState menuStateInstance = MenuState.getInstance();

    public void userInput(KeyEvent e) {
        if (stateManagerInstance.getCurrentState().equals(gameConstants.STATE.GAME)) {
            gameStateInputHandlerInstance.userInput(e);
        } else if (stateManagerInstance.getCurrentState().equals(gameConstants.STATE.STARTMENU)) {
            menuStateInstance.userInput(e);
        }
    }

    public void userInput(MouseWheelEvent e) {
        if (stateManagerInstance.getCurrentState().equals(gameConstants.STATE.GAME)) {
            gameStateInputHandlerInstance.userInput(e);
        } else if (stateManagerInstance.getCurrentState().equals(gameConstants.STATE.STARTMENU)) {
            menuStateInstance.userInput(e);
        }
    }
    public void mouseReleased(MouseEvent e){
        if (stateManagerInstance.getCurrentState().equals(gameConstants.STATE.GAME)) {
            gameStateInputHandlerInstance.mouseReleased(e);
        } else if (stateManagerInstance.getCurrentState().equals(gameConstants.STATE.STARTMENU)) {
            menuStateInstance.userInput(e);
        }
    }
    public void mousePressed(MouseEvent e) {
        if (stateManagerInstance.getCurrentState().equals(gameConstants.STATE.GAME)) {
            gameStateInputHandlerInstance.mousePressed(e);
        }
    }
    public void mouseMoved(MouseEvent e) {
        if (stateManagerInstance.getCurrentState().equals(gameConstants.STATE.GAME)) {
           gameStateInputHandlerInstance.mouseMoved(e);
        }
    }
    public void mouseDragged(MouseEvent e) {
        if (stateManagerInstance.getCurrentState().equals(gameConstants.STATE.GAME)) {
            gameStateInputHandlerInstance.mouseDragged(e);
        }
    }
}

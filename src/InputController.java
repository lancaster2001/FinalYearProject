import java.awt.event.KeyEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class InputController {
    //singleton------------------------------------------------------------------------
    private static InputController instance;
    public static InputController getInstance(){
        if (instance == null) {
            instance = new InputController();
        }
        return instance;
    }
    private InputController(){}
    //----------------------------------------------------------------------------------
    private final StateManager stateManagerInstance = StateManager.getInstance();
    private final MainPanel panelInstance = MainPanel.getInstance();
    private final Camera cameraInstance = Camera.getInstance();
    public void userInput(KeyEvent e){

    }
    public void userInput(MouseWheelEvent e){
        if (stateManagerInstance.getCurrentState() == gameConstants.STATE.GAME){
            if (e.getWheelRotation() < 0) {
                cameraInstance.decreaseZoom();
            } else {
                cameraInstance.increaseZoom();
            }
            panelInstance.repaint();
        }
    }
}

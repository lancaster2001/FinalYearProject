import java.awt.event.*;

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
    private final Map mapInstance = Map.getInstance();
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
    public void userInput(MouseEvent e){
        int slotx = e.getX()/cameraInstance.getwidthOfSlot();
        int sloty = e.getY()/cameraInstance.getheightOfslot();
        mapInstance.setTower(new DrillTower(), slotx, sloty);
    }
}

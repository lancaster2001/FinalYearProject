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
    private final MainPanel panelInstance = MainPanel.getInstance();
    private final Camera cameraInstance = Camera.getInstance();
    int titleHeight = MainFrame.getInstance().getHeight() - panelInstance.getHeight();
    public void userInput(KeyEvent e){
       GameState.getInstance().userInput(e);
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
        titleHeight = MainFrame.getInstance().getHeight() - panelInstance.getHeight();
        boolean check = false;
        if(GameStateUI.getInstance().takeInput(e.getPoint())){
            check = true;
        }else {

            int slotx = e.getX()/cameraInstance.getwidthOfSlot()+1;
            int sloty = (e.getY()-titleHeight)/cameraInstance.getheightOfslot()+1;
            MapSlot clickedSlot = cameraInstance.getMapslot(slotx, sloty);
            if(GameUIBuildMenu.getInstance().getSelectedBuildMenuElement()==2){
                GameState.getInstance().getMapInstance().setTower(new DrillTower(), clickedSlot.getX(), clickedSlot.getY());
            }else if(GameUIBuildMenu.getInstance().getSelectedBuildMenuElement()==3) {
                GameState.getInstance().getMapInstance().setTower(new TowerVariant1(), clickedSlot.getX(), clickedSlot.getY());
            }
        }

    }
}

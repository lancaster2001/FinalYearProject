import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

public class GameStateInputHandler {
    //singleton------------------------------------------------------------------------
    private static GameStateInputHandler instance = new GameStateInputHandler();
    public static GameStateInputHandler getInstance(){
        if (instance == null) {
            instance = new GameStateInputHandler();
        }
        return instance;
    }
    private GameStateInputHandler(){}
    //----------------------------------------------------------------------------------
    private final MainPanel panelInstance = MainPanel.getInstance();
    private final Camera cameraInstance = Camera.getInstance();
    private final GameUIBuildMenu gameUIBuildMenuInstance = GameUIBuildMenu.getInstance();
    private double buildRotation = 0;
    int titleHeight = MainFrame.getInstance().getHeight() - panelInstance.getHeight();
    public void userInput(KeyEvent e){
        if(GameState.getInstance().userInput(e)){

        } else if (e.getKeyChar() == 'r') {
            buildRotation+= Math.PI/2;
            if(buildRotation>=2* Math.PI){
                buildRotation-=2*Math.PI;
            }
        }
    }
    public void userInput(MouseWheelEvent e){
        if (e.getWheelRotation() < 0) {
            cameraInstance.decreaseZoom();
        } else {
            cameraInstance.increaseZoom();
        }
        panelInstance.repaint();
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
            GameState.getInstance().getMapInstance().setTower(gameUIBuildMenuInstance.getSelectedTower(), new Pose(clickedSlot.getX(), clickedSlot.getY(),buildRotation));
            GameState.getInstance().getMapInstance().save();
        }
    }
}

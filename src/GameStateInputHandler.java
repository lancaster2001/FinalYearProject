import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

public class GameStateInputHandler {
    //singleton------------------------------------------------------------------------
    private static GameStateInputHandler instance = new GameStateInputHandler();

    public static GameStateInputHandler getInstance() {
        if (instance == null) {
            instance = new GameStateInputHandler();
        }
        return instance;
    }

    private GameStateInputHandler() {
    }

    //----------------------------------------------------------------------------------
    private final MainPanel panelInstance = MainPanel.getInstance();
    private final Camera cameraInstance = Camera.getInstance();
    private final GameUIBuildMenu gameUIBuildMenuInstance = GameUIBuildMenu.getInstance();
    private double buildRotation = 0;
    int titleHeight = MainFrame.getInstance().getHeight() - panelInstance.getHeight();
    private int[] previousMouseCoord;
    private int[] currentMouseCoord;
    private Point currentMouseLocation;

    public void userInput(KeyEvent e) {
        if (GameState.getInstance().userInput(e)) {

        } else if (e.getKeyChar() == 'r') {
            buildRotation += Math.PI / 2;
            if (buildRotation >= 2 * Math.PI) {
                buildRotation -= 2 * Math.PI;
            }
        } else if (e.getKeyChar() == ' ') {
            GameState.getInstance().paused = !GameState.getInstance().paused;
        } else if (e.getKeyChar() == 'p') {
            SaveHandler.getInstance().saveGame();
        }
    }

    public void userInput(MouseWheelEvent e) {
        if (e.getWheelRotation() < 0) {
            cameraInstance.decreaseZoom();
        } else {
            cameraInstance.increaseZoom();
        }
        panelInstance.repaint();
    }

    public void userInput(MouseEvent e) {
        titleHeight = MainFrame.getInstance().getHeight() - panelInstance.getHeight();
        boolean check = false;
        if (GameStateUI.getInstance().takeInput(e.getPoint())) {
            check = true;
        } else {
            if (e.getButton() == MouseEvent.BUTTON3) {
                int[] g = cameraInstance.slotOnScreen(e.getPoint());
                MapSlot clickedSlot = cameraInstance.getMapslot(g[0], g[1]);
                if (gameUIBuildMenuInstance.getSelectedTower() != null) {
                    GameState.getInstance().getMapInstance().clearTower(clickedSlot.getX(), clickedSlot.getY());
                }
            } else {
                int[] g = cameraInstance.slotOnScreen(e.getPoint());
                MapSlot clickedSlot = cameraInstance.getMapslot(g[0], g[1]);
                if (gameUIBuildMenuInstance.getSelectedTower() != null) {
                    GameState.getInstance().getMapInstance().setTower(gameUIBuildMenuInstance.getSelectedTower(), new Pose(clickedSlot.getX(), clickedSlot.getY(), buildRotation));
                }
            }
        }
    }
    public void mouseMoved(MouseEvent e) {
        currentMouseLocation = e.getPoint();
    }
    public void changeMousePosition(){
        if(currentMouseLocation!=null){
            currentMouseCoord = cameraInstance.slotOnScreen(currentMouseLocation);
        }
        if(currentMouseCoord!=previousMouseCoord) {
            if (previousMouseCoord != null) {
                MapSlot clickedSlot = cameraInstance.getMapslot(previousMouseCoord[0], previousMouseCoord[1]);
                GameState.getInstance().getMapInstance().clearTempTower(clickedSlot.getX(), clickedSlot.getY());
            }
            if (currentMouseLocation != null) {
                previousMouseCoord = currentMouseCoord;
                MapSlot clickedSlot = cameraInstance.getMapslot(currentMouseCoord[0], currentMouseCoord[1]);
                if (gameUIBuildMenuInstance.getSelectedTower() != null) {
                    GameState.getInstance().getMapInstance().setTempTower(gameUIBuildMenuInstance.getSelectedTower(), new Pose(clickedSlot.getX(), clickedSlot.getY(), buildRotation));
                }
            }
        }
    }
    public Point getCurrentMouseLocation(){
        return  currentMouseLocation;
    }
}

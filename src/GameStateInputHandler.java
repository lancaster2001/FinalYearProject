import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.Arrays;

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
    private int lastClickedButton;
    private int currentHeldButton;
    private int[] dragStartSlot;
    private ArrayList<int[]> draggedTempPoints = new ArrayList<>();

    public void userInput(KeyEvent e) {
        if (GameState.getInstance().userInput(e)) {

        } else if (e.getKeyChar() == 'r') {
            buildRotation += Math.PI / 2;
            if (buildRotation >= 2 * Math.PI) {
                buildRotation -= 2 * Math.PI;
            }
            draggedToBuild();
        } else if (e.getKeyChar() == ' ') {
            GameState.getInstance().paused = !GameState.getInstance().paused;
        } else if (e.getKeyChar() == 'p') {
            SaveHandler.getInstance().saveGame();
        }
    }

    public void userInput(MouseWheelEvent e) {
        if (GameStateUI.getInstance().takeInput(e.getPoint())) {
            return;
        }

        if (e.getWheelRotation() < 0) {
            cameraInstance.decreaseZoom();
        } else {
            cameraInstance.increaseZoom();
        }
        panelInstance.repaint();
    }

    public void mouseReleased(MouseEvent e) {
        if(currentHeldButton==0){
            return;
        }
        lastClickedButton = currentHeldButton;
        currentHeldButton = 0;
        if (GameStateUI.getInstance().takeInput(e.getPoint())) {
            clearDraggedTempPoints();
            return;
        }
        if (!draggedTempPoints.isEmpty()) {
            setTempsToPermenants();
            return;
        }
        clickToBuild();
    }

    public void mousePressed(MouseEvent e) {
        if(currentHeldButton!=0 && e.getButton()!=currentHeldButton){
            clearDraggedTempPoints();
            currentHeldButton = 0;
            return;
        }
        currentHeldButton = e.getButton();
        dragStartSlot = cameraInstance.slotOnScreen(e.getPoint());
    }

    public void mouseMoved(MouseEvent e) {
        currentMouseLocation = e.getPoint();
    }


    public void changeMousePosition() {
        if (currentHeldButton == 0) {
            if (currentMouseLocation != null) {
                currentMouseCoord = cameraInstance.slotOnScreen(currentMouseLocation);
            }
            if (currentMouseCoord != previousMouseCoord) {
                if ((previousMouseCoord != null)&&(previousMouseCoord.length!=0)) {
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
    }

    public Point getCurrentMouseLocation() {
        return currentMouseLocation;
    }

    public void mouseDragged(MouseEvent e) {
        currentMouseLocation = e.getPoint();

        if ((currentHeldButton == MouseEvent.BUTTON1) && (gameUIBuildMenuInstance.getSelectedBuildMenuElement() != -1) && (!gameUIBuildMenuInstance.takeInput(e.getPoint()))) {
            draggedToBuild();
        } else if ((currentHeldButton == MouseEvent.BUTTON3) && (!gameUIBuildMenuInstance.takeInput(e.getPoint()))) {
            draggedToDelete();
        }
    }

    private void draggedToDelete() {
        if (currentHeldButton != 0) {
            clearDraggedTempPoints();
            gameUIBuildMenuInstance.clearSelectedElement();

            int[] startCoord = new int[]{0,0};
            startCoord[0] = dragStartSlot[0];
            startCoord[1] = dragStartSlot[1];
            int[] endCoord = cameraInstance.slotOnScreen(new Point((int) (currentMouseLocation.getX()), (int) (currentMouseLocation.getY())));


            if (startCoord[0] > endCoord[0]) {
                int holder = startCoord[0];
                startCoord[0] = endCoord[0];
                endCoord[0] = holder;
            }
            if (startCoord[1] > endCoord[1]) {
                int holder = startCoord[1];
                startCoord[1] = endCoord[1];
                endCoord[1] = holder;
            }
            for (int indexY = startCoord[1]; indexY <= endCoord[1]; indexY++) {
                for (int indexX = startCoord[0]; indexX <= endCoord[0]; indexX++) {
                    GameState.getInstance().getMapInstance().setTempTower(TowerManager.getInstance().getDeletionTower(), new Pose(indexX, indexY, buildRotation));
                    draggedTempPoints.add(new int[]{indexX, indexY});
                }
            }
        }
    }
    private void draggedToBuild() {
        if (currentHeldButton != 0) {
            clearDraggedTempPoints();
            boolean onXaxis;

            int[] startCoord = new int[]{0,0};
            startCoord[0] = dragStartSlot[0];
            startCoord[1] = dragStartSlot[1];
            int[] endCoord = cameraInstance.slotOnScreen(new Point((int) (currentMouseLocation.getX()), (int) (currentMouseLocation.getY())));

            if (Math.abs(startCoord[0] - endCoord[0]) > Math.abs(startCoord[1] - endCoord[1])) {
                endCoord[1] = startCoord[1];
                onXaxis = true;
            } else {
                endCoord[0] = startCoord[0];
                onXaxis = false;
            }
            double dragBuildRotation = getDragBuildRotation(startCoord,endCoord,onXaxis);

            if (startCoord[0] > endCoord[0]) {
                int holder = startCoord[0];
                startCoord[0] = endCoord[0];
                endCoord[0] = holder;
            }
            if (startCoord[1] > endCoord[1]) {
                int holder = startCoord[1];
                startCoord[1] = endCoord[1];
                endCoord[1] = holder;
            }


            for (int indexY = startCoord[1]; indexY <= endCoord[1]; indexY++) {
                for (int indexX = startCoord[0]; indexX <= endCoord[0]; indexX++) {
                    GameState.getInstance().getMapInstance().setTempTower(gameUIBuildMenuInstance.getSelectedTower(), new Pose(indexX, indexY, dragBuildRotation));
                    draggedTempPoints.add(new int[]{indexX, indexY});
                }
            }
        }
    }
    private double getDragBuildRotation(int[] startCoord, int[] endCoord, boolean onXaxis){
        double dragBuildRotation = buildRotation;
        if (!Arrays.equals(startCoord, endCoord)) {
            if (onXaxis) {
                if (startCoord[0] > endCoord[0]){
                    dragBuildRotation = Math.PI*1.5;
                }else{
                    dragBuildRotation = Math.PI/2;
                }
            } else {
                if (startCoord[1] > endCoord[1]){
                    dragBuildRotation = 0;
                }else{
                    dragBuildRotation = Math.PI;
                }
            }
        }
        return dragBuildRotation;
    }
    private void clearDraggedTempPoints(){
        for(int[] currentcoord:draggedTempPoints){
            GameState.getInstance().getMapInstance().clearTempTower(currentcoord[0], currentcoord[1]);
        }
        draggedTempPoints.clear();
    }
    private void setTempsToPermenants(){
        for(int[] currentcoord:draggedTempPoints){
            MapSlot Slot = GameState.getInstance().getMapInstance().getMapslot(currentcoord[0], currentcoord[1]);
            if(Slot.getTempTower().getName().equalsIgnoreCase("deletion")) {
                GameState.getInstance().getMapInstance().clearTower(Slot.getX(), Slot.getY());

            }else {
                GameState.getInstance().getMapInstance().setTowerFromTempTower(currentcoord[0],currentcoord[1]);
            }
                GameState.getInstance().getMapInstance().clearTempTower(Slot.getX(), Slot.getY());
        }
        draggedTempPoints.clear();
    }
    private void clickToBuild(){
            if (lastClickedButton == MouseEvent.BUTTON3) {
                if(GameUIBuildMenu.getInstance().getSelectedBuildMenuElement()==-1) {
                    int[] g = cameraInstance.slotOnScreen(currentMouseLocation);
                    MapSlot clickedSlot = cameraInstance.getMapslot(g[0], g[1]);
                    GameState.getInstance().getMapInstance().clearTower(clickedSlot.getX(), clickedSlot.getY());
                }else{
                    if(!draggedTempPoints.isEmpty()){
                        clearDraggedTempPoints();
                    }else {
                        GameUIBuildMenu.getInstance().clearSelectedElement();
                    }
                }
            } else if (lastClickedButton == MouseEvent.BUTTON1){
                int[] g = cameraInstance.slotOnScreen(currentMouseLocation);
                MapSlot clickedSlot = cameraInstance.getMapslot(g[0], g[1]);
                if (gameUIBuildMenuInstance.getSelectedTower() != null) {
                    GameState.getInstance().getMapInstance().setTower(gameUIBuildMenuInstance.getSelectedTower(), new Pose(clickedSlot.getX(), clickedSlot.getY(), buildRotation));
                }
            }
        }
}

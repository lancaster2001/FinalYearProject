import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;

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
    private Point lastClickedPoint;
    private int lastClickedButton;
    private ArrayList<int[]> draggedTempPoints = new ArrayList<>();

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
        lastClickedPoint = e.getPoint();
        lastClickedButton = e.getButton();
        titleHeight = MainFrame.getInstance().getHeight() - panelInstance.getHeight();
        setTempsToPermenants();

        boolean check = false;
        if (GameStateUI.getInstance().takeInput(e.getPoint())) {
            check = true;
        } else {
            if (e.getButton() == MouseEvent.BUTTON3) {
                if(GameUIBuildMenu.getInstance().getSelectedBuildMenuElement()==-1) {
                    int[] g = cameraInstance.slotOnScreen(e.getPoint());
                    MapSlot clickedSlot = cameraInstance.getMapslot(g[0], g[1]);
                    GameState.getInstance().getMapInstance().clearTower(clickedSlot.getX(), clickedSlot.getY());
                }else{
                    if(!draggedTempPoints.isEmpty()){
                        clearDraggedTempPoints();
                    }else {
                        GameUIBuildMenu.getInstance().clearSelectedElement();
                    }
                }
            } else if (e.getButton() == MouseEvent.BUTTON1){
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
    public void mouseDragged(MouseEvent e) {
        if ((lastClickedButton == MouseEvent.BUTTON1)&&(gameUIBuildMenuInstance.getSelectedBuildMenuElement()!=-1)&&(!gameUIBuildMenuInstance.takeInput(e.getPoint()))) {
            clearDraggedTempPoints();
            lastClickedPoint = currentMouseLocation;
            Point startPoint;
            Point endPoint;
            int startX;
            int startY;
            int endX;
            int endY;
            if(Math.abs(lastClickedPoint.getX()-e.getPoint().getX())>Math.abs(lastClickedPoint.getY()-e.getPoint().getY())){
                startPoint = new Point((int)(lastClickedPoint.getX()),(int)(lastClickedPoint.getY()));
                endPoint = new Point((int)(e.getPoint().getX()),(int)(lastClickedPoint.getY()));
            }else{
                startPoint = new Point((int)(lastClickedPoint.getX()),(int)(lastClickedPoint.getY()));
                endPoint = new Point((int)(lastClickedPoint.getX()),(int)(e.getPoint().getY()));
            }
            int[] startCoord = cameraInstance.slotOnScreen(new Point((int)(startPoint.getX()),(int)(startPoint.getY())));
            int[] endCoord = cameraInstance.slotOnScreen(new Point((int)(endPoint.getX()),(int)(endPoint.getY())));
            MapSlot startSlot = cameraInstance.getMapslot(startCoord[0], startCoord[1]);
            MapSlot endSlot = cameraInstance.getMapslot(endCoord[0], endCoord[1]);
            startX = cameraInstance.getMapslot(startCoord[0], startCoord[1]).getX();
            startY = cameraInstance.getMapslot(startCoord[0], startCoord[1]).getY();
            endX = cameraInstance.getMapslot(endCoord[0], endCoord[1]).getX();
            endY = cameraInstance.getMapslot(endCoord[0], endCoord[1]).getY();

            if(startX>endX){
                int holder = startX;
                startX = endX;
                endX = holder;
            }
            if(startY>endY){
                int holder = startY;
                startY = endY;
                endY = holder;
            }
            for(int indexY = startY;indexY <= endY;indexY++){
                for(int indexX = startX;indexX <= endX;indexX++){
                    GameState.getInstance().getMapInstance().setTempTower(gameUIBuildMenuInstance.getSelectedTower(), new Pose(indexX, indexY, buildRotation));
                    draggedTempPoints.add(new int[]{indexX,indexY});
                }
            }
        }
    }
    private void clearDraggedTempPoints(){
        if (draggedTempPoints.size()>10){
            System.out.println();
        }
        for(int[] currentcoord:draggedTempPoints){
            MapSlot Slot = cameraInstance.getMapslot(currentcoord[0], currentcoord[1]);
            GameState.getInstance().getMapInstance().clearTempTower(Slot.getX(), Slot.getY());
        }
        draggedTempPoints.clear();
    }
    private void setTempsToPermenants(){
        for(int[] currentcoord:draggedTempPoints){
            MapSlot Slot = cameraInstance.getMapslot(currentcoord[0], currentcoord[1]);
            GameState.getInstance().getMapInstance().setTower(gameUIBuildMenuInstance.getSelectedTower(), new Pose(Slot.getX(), Slot.getY(), buildRotation));
            GameState.getInstance().getMapInstance().clearTempTower(Slot.getX(), Slot.getY());
        }
        draggedTempPoints.clear();
    }
}

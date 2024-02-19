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
    private int currentHeldButton;
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
    public void mouseReleased(MouseEvent e){
        currentHeldButton = 0;
        lastClickedPoint = e.getPoint();
        lastClickedButton = e.getButton();
        titleHeight = MainFrame.getInstance().getHeight() - panelInstance.getHeight();
        if(!draggedTempPoints.isEmpty()){
            setTempsToPermenants();
            return;
        }
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
    public void mousePressed(MouseEvent e) {
        currentHeldButton  = e.getButton();
    }
    public void mouseMoved(MouseEvent e) {
        currentMouseLocation = e.getPoint();
    }
    public void changeMousePosition(){
        if(currentHeldButton!=0) {
            if (currentMouseLocation != null) {
                currentMouseCoord = cameraInstance.slotOnScreen(currentMouseLocation);
            }
            if (currentMouseCoord != previousMouseCoord) {
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
    }
    public Point getCurrentMouseLocation(){
        return  currentMouseLocation;
    }
    public void mouseDragged(MouseEvent e) {
        if ((currentHeldButton == MouseEvent.BUTTON1)&&(gameUIBuildMenuInstance.getSelectedBuildMenuElement()!=-1)&&(!gameUIBuildMenuInstance.takeInput(e.getPoint()))) {
            draggedToBuild(e);
        }else if ((currentHeldButton == MouseEvent.BUTTON3)&&(!gameUIBuildMenuInstance.takeInput(e.getPoint()))){
            draggedToDelete(e);
        }
    }
    private void draggedToDelete(MouseEvent e){
        clearDraggedTempPoints();
        lastClickedPoint = currentMouseLocation;
        Point startPoint = new Point((int)(lastClickedPoint.getX()),(int)(lastClickedPoint.getY()));
        Point endPoint = new Point((int)(e.getPoint().getX()),(int)(e.getPoint().getY()));
        int[] startCoord = cameraInstance.slotOnScreen(new Point((int)(startPoint.getX()),(int)(startPoint.getY())));
        int[] endCoord = cameraInstance.slotOnScreen(new Point((int)(endPoint.getX()),(int)(endPoint.getY())));
        MapSlot startSlot = cameraInstance.getMapslot(startCoord[0], startCoord[1]);
        MapSlot endSlot = cameraInstance.getMapslot(endCoord[0], endCoord[1]);
        int startX = startSlot.getX();
        int startY = startSlot.getY();
        int endX = endSlot.getX();
        int endY = endSlot.getY();

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
                GameState.getInstance().getMapInstance().setTempTower(TowerManager.getInstance().getDeletionTower(), new Pose(indexX, indexY, buildRotation));
                draggedTempPoints.add(new int[]{indexX,indexY});
            }
        }
        if(draggedTempPoints.size()>10){
            System.out.println();//todo remove this
            //check mouse movement
        }
    }
    private void draggedToBuild(MouseEvent e){
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
        startX = startSlot.getX();
        startY = startSlot.getY();
        endX = endSlot.getX();
        endY = endSlot.getY();

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
    private void clearDraggedTempPoints(){
        for(int[] currentcoord:draggedTempPoints){
            MapSlot Slot = cameraInstance.getMapslot(currentcoord[0], currentcoord[1]);
            GameState.getInstance().getMapInstance().clearTempTower(Slot.getX(), Slot.getY());
        }
        draggedTempPoints.clear();
    }
    private void setTempsToPermenants(){
        for(int[] currentcoord:draggedTempPoints){
            MapSlot Slot = cameraInstance.getMapslot(currentcoord[0], currentcoord[1]);
            if(Slot.getTempTower().getName().equalsIgnoreCase("deletion")) {
                GameState.getInstance().getMapInstance().clearTower(Slot.getX(), Slot.getY());

            }else {
                GameState.getInstance().getMapInstance().setTower(gameUIBuildMenuInstance.getSelectedTower(), new Pose(Slot.getX(), Slot.getY(), buildRotation));
            }
                GameState.getInstance().getMapInstance().clearTempTower(Slot.getX(), Slot.getY());
        }
        draggedTempPoints.clear();
    }
}

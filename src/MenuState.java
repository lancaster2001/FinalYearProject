import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.*;

public class MenuState {
    //singleton-------------------------------------------------------------------------
    private static MenuState instance = new MenuState();

    private MenuState() {
        menues.put("main", new String[]{"Play Game", "New Game","Saves", "Options"});
        menu = new Rectangle(screenWidth/5, screenHeight/5, (screenWidth/5)*3, (screenHeight/5)*3);
        screenRefresher();
    }

    public static MenuState getInstance() {
        if (instance == null) {
            instance = new MenuState();
        }
        return instance;
    }

    //----------------------------------------------------------------------------------------
    private int screenWidth = gameSettings.getInstance().getScreenWidth();
    private int screenHeight = gameSettings.getInstance().getScreenHeight();
    Color BackgroundColor = Color.white;
    Color titleColor = Color.BLACK;
    private final StateManager stateManagerInstance = StateManager.getInstance();
    private final MainPanel panelInstance = MainPanel.getInstance();
    private HashMap<String, Rectangle> menuButtons = new HashMap<>();
    private HashMap<String, String[]> menues = new HashMap<>();
    private ArrayList<String> menuPath = new ArrayList<>();
    private Rectangle menu;
    private boolean errorMessageDisplay = false;
    private String errorMessage;
    private Rectangle errorMessageHitBox;

    public void startup(){
        resetSelectedMenu();
        screenRefresher();
        if(!menues.containsKey("Saves")){
            String[] saves = SaveHandler.getInstance().getSavesList();
            if(saves.length>0) {
                menues.put("Saves", saves);
            }
        }
    }
    private void screenRefresher() {
        Timer screenRefreshTimer = new Timer();
        panelInstance.repaint();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if(stateManagerInstance.currentState== gameSettings.STATE.STARTMENU) {
                    screenRefresher();
                }
            }
        };
        screenRefreshTimer.schedule(task, 1000 / 60);
    }
    public void draw(Graphics g) {
        if(errorMessageDisplay){
            drawErrorMessage(g);
        }else {
            drawMenu(g);
        }

    }
    private void drawMenu(Graphics g) {
        g.setColor(Color.BLACK);
        g.drawRect(menu.x, menu.y, menu.width, menu.height);
        g.setColor(BackgroundColor);
        g.fill3DRect(menu.x, menu.y, menu.width, menu.height, true);

        for (String buttonOptions : menuButtons.keySet()) {
            Rectangle button = menuButtons.get(buttonOptions);
            g.setColor(Color.BLACK);
            g.drawRect(button.x, button.y, button.width, button.height);
            g.setColor(BackgroundColor);
            g.fill3DRect(button.x, button.y, button.width, button.height, true);
            g.setColor(Color.BLACK);

            g.setColor(titleColor);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            try {
                g.drawString(buttonOptions.replace(".json", ""), button.x, button.y + 20);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    private void drawErrorMessage(Graphics g) {
        errorMessageHitBox = new Rectangle(menu.x+(menu.width/6),menu.y+((menu.height/10)*4),(menu.width/6)*4,((menu.height/10)*2));
        g.setColor(Color.BLACK);
        g.drawRect(errorMessageHitBox.x, errorMessageHitBox.y, errorMessageHitBox.width, errorMessageHitBox.height);
        g.setColor(BackgroundColor);
        g.fill3DRect(errorMessageHitBox.x, errorMessageHitBox.y, errorMessageHitBox.width, errorMessageHitBox.height, true);
        g.setColor(Color.BLACK);

        g.setColor(titleColor);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        if(errorMessage==null){
            errorMessage = "error message was null";
        }
        g.drawString(errorMessage, errorMessageHitBox.x, errorMessageHitBox.y + 20);
    }
    public void userInput(MouseEvent e) {
        if (errorMessageDisplay){
            userInputErrorMessage(e);
        }else {
            userInputMenu(e);
        }
    }
    public void userInputErrorMessage(MouseEvent e) {
        if(errorMessageHitBox !=null){
            closeErrorMessage();
        }
    }
    public void userInputMenu(MouseEvent e) {
        String[] menuOptions = menues.get(getSelectedMenu());
        try {
            if ((menuOptions != null) && (menuButtons != null)) {
                for (String buttonOptions : menuButtons.keySet()) {
                    if (menuButtons.get(buttonOptions).contains(e.getPoint())) {
                        setSelectedMenu(buttonOptions);
                    }
                }
            }
        }catch (ConcurrentModificationException exception){

        }
    }
    public void userInput(MouseWheelEvent e) {

    }

    public void userInput(KeyEvent e) {
        if (e.getKeyChar()==KeyEvent.VK_ESCAPE){
            if (errorMessageDisplay){
                closeErrorMessage();
            }else {
                goToPreviousMenu();
            }
        }
    }
    private void loadMenu() {
        selectedMenuCheck();
        Rectangle placement = menu;
        int index = 0;
        String[] buttons = menues.get(getSelectedMenu());
        if(buttons != null) {
            int spaceBetweenButtonsHorizontal = (int) (placement.width * 0.2);
            int spaceBetweenButtonsVertical = (int) ((placement.height / buttons.length) * 0.2);
            menuButtons.clear();
            for (String button : buttons) {
                int x = placement.x + spaceBetweenButtonsHorizontal;
                int y = placement.y + (index * (placement.height / buttons.length)) + spaceBetweenButtonsVertical;
                int width = (placement.width - (2 * spaceBetweenButtonsHorizontal));
                int height = (placement.height / buttons.length) - (2 * spaceBetweenButtonsVertical);
                menuButtons.put(button,new Rectangle(x, y, width, height));
                index += 1;
            }
        }
    }
    private void closeErrorMessage(){
        resetSelectedMenu();
        errorMessageDisplay = false;
    }
    private boolean selectedMenuCheck() {
        if (getSelectedMenu().equalsIgnoreCase("Play Game")) {
            SaveHandler.getInstance().loadSave();
            stateManagerInstance.setCurrentState(gameSettings.STATE.GAME);
            return true;
        }else if(getSelectedMenu().equalsIgnoreCase("New Game")) {
            SaveHandler.getInstance().newSave();
            stateManagerInstance.setCurrentState(gameSettings.STATE.GAME);
            return true;
        }else if(Objects.equals(getSelectedMenu(), "Saves")){
            if (!menues.containsKey("Saves")){
                showErrorMessage("there are no saves currently available");
            }
        }else if (menuPath.contains("Saves") && !Objects.equals(getSelectedMenu(), "Saves")){
            SaveHandler.getInstance().setSaveSlot(getSelectedMenu().replace(".json",""));
            stateManagerInstance.setCurrentState(gameSettings.STATE.GAME);
        }else{
            goToPreviousMenu();
        }
        return false;
    }
    public void showErrorMessage(String errorMessage){
        this.errorMessage = errorMessage;
        errorMessageDisplay = true;
    }
    public void resetSelectedMenu(){
        if(menuPath.size()>0){
            menuPath.clear();
        }
        menuPath.add("main");
        loadMenu();
    }
    private void goToPreviousMenu(){
        if((menuPath!=null)&&(!menuPath.isEmpty())&&(!Objects.equals(getSelectedMenu(), "main"))) {
            menuPath.removeLast();
            loadMenu();
        }
    }
    public void setSelectedMenu(String selectedMenu) {
        menuPath.add(selectedMenu);
        loadMenu();
    }
    public String getSelectedMenu() {
        try {
            return menuPath.getLast();
        }catch(Exception e){
            return "";
        }
    }
}

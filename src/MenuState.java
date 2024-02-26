import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class MenuState {
    //singleton-------------------------------------------------------------------------
    private static MenuState instance = new MenuState();

    private MenuState() {
        menues.put("main", new String[]{"Play Game", "New Game","Saves", "Options"});
        menues.put("Saves", SaveHandler.getInstance().getSavesList());
        menu = new Rectangle(gameConstants.screenWidth/5, gameConstants.screenHeight/5, (gameConstants.screenWidth/5)*3, (gameConstants.screenHeight/5)*3);
        loadMenu();
        screenRefresher();
    }

    public static MenuState getInstance() {
        if (instance == null) {
            instance = new MenuState();
        }
        return instance;
    }

    //----------------------------------------------------------------------------------------
    private boolean onSavesMenu = false;
    private final StateManager stateManagerInstance = StateManager.getInstance();
    private final MainPanel panelInstance = MainPanel.getInstance();
    private ArrayList<Rectangle> menuButtons = new ArrayList<>();
    private HashMap<String, String[]> menues = new HashMap<String, String[]>();
    private Rectangle menu;
    private boolean errorMessageDisplay = false;
    private String errorMessage;
    private String selectedMenu = "main";
    Rectangle errorMessageHitbox;

    public void startup(){
        selectedMenu = "main";
        screenRefresher();
    }
    private void screenRefresher() {
        Timer screenRefreshTimer = new Timer();
        panelInstance.repaint();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if(stateManagerInstance.currentState== gameConstants.STATE.STARTMENU) {
                    screenRefresher();
                }
            }
        };
        screenRefreshTimer.schedule(task, 1000 / 60);
    }

    private void loadMenu() {
        Rectangle placement = menu;
        int index = 0;
        String[] buttons = menues.get(selectedMenu);
        if(buttons != null) {
            int spaceBetweenButtonsHorizontal = (int) (placement.width * 0.2);
            int spaceBetweenButtonsVertical = (int) ((placement.height / buttons.length) * 0.2);
            menuButtons.clear();
            for (String button : buttons) {
                int x = placement.x + spaceBetweenButtonsHorizontal;
                int y = placement.y + (index * (placement.height / buttons.length)) + spaceBetweenButtonsVertical;
                int width = (placement.width - (2 * spaceBetweenButtonsHorizontal));
                int height = (placement.height / buttons.length) - (2 * spaceBetweenButtonsVertical);
                menuButtons.add(new Rectangle(x, y, width, height));
                index += 1;
            }
        }
    }

    public void draw(Graphics g) {
        if(errorMessageDisplay){
            drawErrorMessage(g);
        }else {
            drawMenu(g);
        }

    }
    public void drawMenu(Graphics g) {
        g.setColor(Color.BLACK);
        g.drawRect(menu.x, menu.y, menu.width, menu.height);
        g.setColor(gameConstants.mainMenuBackgroundColor);
        g.fill3DRect(menu.x, menu.y, menu.width, menu.height, true);
        int index = 0;
        String[] menuOptions = menues.get(selectedMenu);
        for (Rectangle button : menuButtons) {
            g.setColor(Color.BLACK);
            g.drawRect(button.x, button.y, button.width, button.height);
            g.setColor(gameConstants.mainMenuBackgroundColor);
            g.fill3DRect(button.x, button.y, button.width, button.height, true);
            g.setColor(Color.BLACK);

            g.setColor(gameConstants.resourceMenuTitleColour);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            try {
                g.drawString(menuOptions[index].replace(".json", ""), button.x, button.y + 20);
            }catch (Exception e){
                e.printStackTrace();
            }
            index += 1;
        }
    }
    public void drawErrorMessage(Graphics g) {
        errorMessageHitbox = new Rectangle(menu.x+(menu.width/6),menu.y+((menu.height/10)*4),(menu.width/6)*4,((menu.height/10)*2));
        g.setColor(Color.BLACK);
        g.drawRect(errorMessageHitbox.x, errorMessageHitbox.y, errorMessageHitbox.width, errorMessageHitbox.height);
        g.setColor(gameConstants.errorMessageBackgroundColor);
        g.fill3DRect(errorMessageHitbox.x, errorMessageHitbox.y, errorMessageHitbox.width, errorMessageHitbox.height, true);
        g.setColor(Color.BLACK);

        g.setColor(gameConstants.resourceMenuTitleColour);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        if(errorMessage==null){
            errorMessage = "error message was null";
        }
        g.drawString(errorMessage, errorMessageHitbox.x, errorMessageHitbox.y + 20);
    }

    public void userInput(MouseEvent e) {
        if (errorMessageDisplay){
            userInputErrorMessage(e);
        }else {
            userInputMenu(e);
        }
    }
    public void userInputErrorMessage(MouseEvent e) {
        if(errorMessageHitbox!=null){
            closeErrorMessage();
        }
    }
    public void userInputMenu(MouseEvent e) {
        String[] menuOptions = menues.get(selectedMenu);
        if ((menuOptions != null) && (menuButtons != null)) {
            for (int buttonIndex = 0; buttonIndex<menuButtons.size(); buttonIndex++) {
                if (menuButtons.get(buttonIndex).contains(e.getPoint())) {
                    selectedMenu = menuOptions[buttonIndex];
                    selectedMenuCheck();
                }
            }
        }
    }
    public void userInput(MouseWheelEvent e) {

    }

    public void userInput(KeyEvent e) {

    }
    private void closeErrorMessage(){
        selectedMenu = "main";
        errorMessageDisplay = false;
    }
    private void selectedMenuCheck() {
        if (selectedMenu.equalsIgnoreCase("Play Game")) {
            SaveHandler.getInstance().loadSave();
            stateManagerInstance.setCurrentState(gameConstants.STATE.GAME);
        }else if(selectedMenu.equalsIgnoreCase("New Game")){
            SaveHandler.getInstance().newSave();
            stateManagerInstance.setCurrentState(gameConstants.STATE.GAME);
        }else if(selectedMenu.equalsIgnoreCase("Saves")) {
            onSavesMenu = true;
        }else if (onSavesMenu){
            SaveHandler.getInstance().setSaveSlot(selectedMenu.replace(".json",""));
            stateManagerInstance.setCurrentState(gameConstants.STATE.GAME);
        }else{
            selectedMenu = "main";
        }
        loadMenu();
        MainPanel.getInstance().repaint();
    }
    public void showErrorMessage(String errorMessage){
        this.errorMessage = errorMessage;
        errorMessageDisplay = true;
    }

}

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.HashMap;

public class MenuState {
    //singleton-------------------------------------------------------------------------
    private static MenuState instance = new MenuState();

    private MenuState() {
        menues.put("main", new String[]{"Play Game", "Saves", "Options"});
        menu = new Rectangle(500,300,1000,1000);
        loadMenu();
    }
    public static MenuState getInstance() {
        if (instance == null) {
            instance = new MenuState();
        }
        return instance;
    }
    //----------------------------------------------------------------------------------------
    private final StateManager stateManagerInstance = StateManager.getInstance();

    private ArrayList<Rectangle> menuButtons = new ArrayList<>();
    private HashMap<String, String[]> menues = new HashMap<String, String[]>();
    private Rectangle menu;
    private String selectedMenu = "main";

    private void loadMenu() {
        Rectangle placement = menu;
        int index = 0;
        String[] buttons = menues.get(selectedMenu);
        int spaceBetweenButtonsHorizontal = (int) (placement.width * 0.2);
        int spaceBetweenButtonsVertical = (int) ((placement.height / buttons.length) * 0.2);
        menuButtons.clear();
        for (String button : buttons) {
            int x = placement.x + spaceBetweenButtonsHorizontal;
            int y = placement.y+(index * (placement.height / buttons.length)) + spaceBetweenButtonsVertical;
            int width = (placement.width - (2 * spaceBetweenButtonsHorizontal));
            int height = (placement.height / buttons.length) - (2 * spaceBetweenButtonsVertical);
            menuButtons.add(new Rectangle(x, y, width, height));
            index += 1;
        }
    }

    public void draw(Graphics g){
        g.setColor(Color.BLACK);
        g.drawRect(menu.x, menu.y, menu.width, menu.height);
        g.setColor(gameConstants.mainMenuBackgroundColor);
        g.fill3DRect(menu.x, menu.y, menu.width, menu.height, true);
        int index = 0;
        String[] menuOptions = menues.get(selectedMenu);
        for(Rectangle button: menuButtons){
            g.setColor(Color.BLACK);
            g.drawRect(button.x, button.y, button.width, button.height);
            g.setColor(gameConstants.mainMenuBackgroundColor);
            g.fill3DRect(button.x, button.y, button.width, button.height, true);
            g.setColor(Color.BLACK);

            g.setColor(gameConstants.resourceMenuTitleColour);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString(menuOptions[index], button.x, button.y+20);

            index+=1;
        }
    }
    public void userInput(MouseEvent e){
        int index = 0;
        String[] menuOptions = menues.get(selectedMenu);
        for(Rectangle button: menuButtons) {
            if(button.contains(e.getPoint())){
                selectedMenu = menuOptions[index];
                selectedMenuCheck();
            }

            index+=1;
        }
    }
    public void userInput(MouseWheelEvent e){

    }
    public void userInput(KeyEvent e){

    }
    private void selectedMenuCheck(){
        if(selectedMenu.equalsIgnoreCase("Play Game")){
            stateManagerInstance.setCurrentState(gameConstants.STATE.GAME);
        }else{
           loadMenu();
        }
    }

}

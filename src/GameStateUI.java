import java.awt.*;

public class GameStateUI {
    //singleton-------------------------------------------------------------------------
    private static GameStateUI instance;
    private GameStateUI(){
    }
    public static GameStateUI getInstance() {
        if (instance == null) {
            instance = new GameStateUI();
        }
        return instance;
    }
//----------------------------------------------------------------------------------------

    public void drawUI(Graphics g){
        drawReasourcesMenu(g);
        drawTitle(g);
    }
    private void drawReasourcesMenu(Graphics g){
        g.setColor(gameConstants.reasourceMenuBackgroundColour);
        g.fill3DRect(gameConstants.reasourcesMenuX, gameConstants.reasourcesMenuY, gameConstants.reasourcesMenuWidth, gameConstants.reasourcesMenuHeight,true);
    }
    private void drawTitle(Graphics g){
        g.drawString("Hello Text!", gameConstants.reasourcesMenuX, gameConstants.reasourcesMenuY);
    }
}

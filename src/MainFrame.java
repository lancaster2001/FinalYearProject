import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class MainFrame extends JFrame implements KeyListener {

    private MainPanel PanelInstance = MainPanel.getInstance();
    private JComponent draw = new JComponent(){};
    private static MainFrame instance;
    private MainFrame(){
    }
    public static MainFrame getInstance() {
        if (instance == null) {
            instance = new MainFrame();
        }
        return instance;
    }
    public MainPanel getPanelInstance(){
        return PanelInstance;
    }
    private void setup() {
        this.setTitle("Space Invaders Assignment 1");
        this.setResizable(false);
        this.setSize(gameConstants.screenSize.width, gameConstants.screenSize.height);
        this.setMinimumSize(new Dimension(gameConstants.screenSize.width, gameConstants.screenSize.height));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().add(this.draw);
        this.pack();
        this.setVisible(true);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        this.add(PanelInstance);
        PanelInstance.setBackground(Color.red);
        this.setVisible(true);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}

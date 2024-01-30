import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainFrame extends JFrame implements KeyListener, MouseWheelListener, MouseListener {

    private final MainPanel PanelInstance = MainPanel.getInstance();
    private final JComponent draw = new JComponent(){};
    private static MainFrame instance;
    private MainFrame(){
        setup();
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
        this.setTitle("Resource Management and Tower Defence Game");
        this.setResizable(false);
        this.setSize(gameConstants.screenSize.width, gameConstants.screenSize.height);
        this.setMinimumSize(new Dimension(gameConstants.screenSize.width, gameConstants.screenSize.height));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().add(this.draw);
        this.pack();
        this.setVisible(true);
        addKeyListener(this);
        addMouseWheelListener(this);
        addMouseListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        this.add(PanelInstance);
        //PanelInstance.setBackground(Color.red);
        this.setVisible(true);
    }
    @Override
    public void mouseClicked(MouseEvent e) {}
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        InputController.getInstance().userInput(e);
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
    @Override
    public void mousePressed(MouseEvent e) {
        InputController.getInstance().userInput(e);

    }
    @Override
    public void mouseReleased(MouseEvent e) {

    }
    @Override
    public void mouseEntered(MouseEvent e) {

    }
    @Override
    public void mouseExited(MouseEvent e) {

    }
}
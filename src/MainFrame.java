import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainFrame extends JFrame implements KeyListener, MouseWheelListener, MouseListener,MouseMotionListener {
    //singleton-------------------------------------------------------------------------
    private static MainFrame instance = new MainFrame();

    private MainFrame() {
        setup();
    }

    public static MainFrame getInstance() {
        if (instance == null) {
            instance = new MainFrame();
        }
        return instance;
    }

    //----------------------------------------------------------------------------------------
    private final MainPanel PanelInstance = MainPanel.getInstance();
    private final JComponent draw = new JComponent() {
    };

    public MainPanel getPanelInstance() {
        return PanelInstance;
    }

    private void setup() {
        this.setTitle("Resource Management and Tower Defence Game");
        this.setResizable(false);
        this.setSize(gameConstants.screenSize.width, gameConstants.screenSize.height);
        this.setMinimumSize(new Dimension(gameConstants.screenSize.width, gameConstants.screenSize.height));
        this.setUndecorated(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().add(this.draw);
        this.pack();
        this.setVisible(true);
        addKeyListener(this);
        addMouseWheelListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                close();
            }});
        this.add(PanelInstance);
        this.setVisible(true);
    }
    private void close(){
        SaveHandler.getInstance().saveSaveSlot();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        InputHandler.getInstance().userInput(e);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        InputHandler.getInstance().userInput(e);
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        InputHandler.getInstance().mousePressed(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        InputHandler.getInstance().mouseReleased(e);
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        InputHandler.getInstance().mouseDragged(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        InputHandler.getInstance().mouseMoved(e);
    }
}
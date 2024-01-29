import java.awt.*;

public interface gameConstants {
    int mapSize = 5*5;// 5x5 map should be 5*5
    GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    int screenWidth = gd.getDisplayMode().getWidth();
    int screenHeight = gd.getDisplayMode().getHeight();
    Dimension screenSize = new Dimension(screenWidth,screenHeight);
}

import java.util.Timer;
import java.util.TimerTask;

public class Launcher {
    //singleton------------------------------------------------------------------------
    private static Launcher instance;
    public static Launcher getInstance(){
        if (instance == null) {
            instance = new Launcher();
        }
        return instance;
    }
    private Launcher(){}
    //----------------------------------------------------------------------------------
    GameState gameStateInstance = GameState.getInstance();
    public static void main(String[] args) {
        Launcher theLauncher = Launcher.getInstance();
    }

}

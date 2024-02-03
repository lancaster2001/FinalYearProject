import java.util.Timer;
import java.util.TimerTask;

public class Launcher {
    //singleton------------------------------------------------------------------------
    private static Launcher instance = new Launcher();
    public static Launcher getInstance(){
        if (instance == null) {
            instance = new Launcher();
        }
        return instance;
    }
    private Launcher(){}
    //----------------------------------------------------------------------------------

    public static void main(String[] args) {
        Launcher theLauncher = Launcher.getInstance();
        GameState gameStateInstance = GameState.getInstance();
        gameStateInstance.startup();
    }

}

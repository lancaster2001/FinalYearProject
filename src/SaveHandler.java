public class SaveHandler {
    //singleton-------------------------------------------------------------------------
    private static SaveHandler instance = new SaveHandler();

    private SaveHandler() {
    }

    public static SaveHandler getInstance() {
        if (instance == null) {
            instance = new SaveHandler();
        }
        return instance;
    }

    //----------------------------------------------------------------------------------------
    private String saveSlot = "save1";

    public String getSaveSlot() {
        return saveSlot;
    }
}

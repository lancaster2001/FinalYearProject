public class BaseBaseTower extends BaseTower{
    //singleton-------------------------------------------------------------------------
    private static BaseBaseTower instance = new BaseBaseTower(gameConstants.mapWidth/2,gameConstants.mapHeight/2,TowerManager.getInstance().getTemplate("BasicBase"));

    private BaseBaseTower(int x, int y, TowerTemplate template) {
        super(x,y,template);
    }

    public static BaseBaseTower getInstance() {
        if (instance == null) {
            instance = new BaseBaseTower(gameConstants.mapWidth/2,gameConstants.mapHeight/2,TowerManager.getInstance().getTemplate("BasicBase"));
        }
        return instance;
    }

    //----------------------------------------------------------------------------------------
}

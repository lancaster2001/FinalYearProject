public class BaseBaseTower extends BaseTower{
    //singleton-------------------------------------------------------------------------
    private static BaseBaseTower instance = new BaseBaseTower(new Pose(gameConstants.mapWidth/2,gameConstants.mapHeight/2, 0),TowerManager.getInstance().getTemplate("BasicBase"));

    private BaseBaseTower(Pose pose, TowerTemplate template) {
        super(pose,template);
    }

    public static BaseBaseTower getInstance() {
        if (instance == null) {
            instance = new BaseBaseTower(new Pose(gameConstants.mapWidth/2,gameConstants.mapHeight/2, 0),TowerManager.getInstance().getTemplate("BasicBase"));
        }
        return instance;
    }

    //----------------------------------------------------------------------------------------

}

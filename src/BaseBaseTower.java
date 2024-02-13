public class BaseBaseTower extends BaseTower {
    //singleton-------------------------------------------------------------------------
    private static BaseBaseTower instance = new BaseBaseTower(new Pose(gameConstants.mapWidth / 2, gameConstants.mapHeight / 2, 0), TowerManager.getInstance().getTemplate("BasicBase"));

    private BaseBaseTower(Pose pose, TowerTemplate template) {
        super(pose, template);
        for (double index = 0; index < 4; index += 1.0) {
            inputDirections.add(index * (Math.PI / 2));
        }
    }

    public static BaseBaseTower getInstance() {
        if (instance == null) {
            instance = new BaseBaseTower(new Pose(gameConstants.mapWidth / 2, gameConstants.mapHeight / 2, 0), TowerManager.getInstance().getTemplate("BasicBase"));
        }
        return instance;
    }

    //----------------------------------------------------------------------------------------
    public boolean addToInventory(int x, int y, Resource resource) {
        ResourceManager.getInstance().queryInventory(resource.getName()).add(resource.getQuantity());
        return true;
    }

}

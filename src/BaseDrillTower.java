public class BaseDrillTower extends BaseTower{
    private double actAccumulator = 0.0;
    private final double actAccumulatorLimit = 1.0;
    public BaseDrillTower(int x, int y, TowerTemplate template){
        super(x,y,template);
        imageLink = template.getImageLink();
    }
    public void act(BaseTile tile, ResourceManager resourceManagerInstance){
        resourceManagerInstance.queryInventory(tile.getResource()).add();
    }
    public void tick(BaseTile tile,double tickMultiplier){
        actAccumulator+=tickMultiplier;
        if (actAccumulator>=actAccumulatorLimit){
            actAccumulator-=actAccumulatorLimit;
            ResourceManager.getInstance().queryInventory(tile.getResource()).add();
        }
    }
}

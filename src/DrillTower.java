public class DrillTower extends BaseTower{
    public void act(BaseTile tile, ResourceManager resourceManagerInstance){
        resourceManagerInstance.queryInventory(tile.getResource()).add();
    }
}

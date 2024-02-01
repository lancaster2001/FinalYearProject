public class DrillTower extends BaseTower{
    public DrillTower(){
        imageLink = "src/Towers/Driller/image.png";
    }
    public void act(BaseTile tile, ResourceManager resourceManagerInstance){
        resourceManagerInstance.queryInventory(tile.getResource()).add();
    }
}

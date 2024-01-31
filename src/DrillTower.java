public class DrillTower extends BaseTower{
    public DrillTower(){
        imageLink = "src/Towers/Driller/image.png";
        image = AssetManager.getInstance().getImage(imageLink);
    }
    public void act(BaseTile tile, ResourceManager resourceManagerInstance){
        resourceManagerInstance.queryInventory(tile.getResource()).add();
    }
}

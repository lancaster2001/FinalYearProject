public class BaseBaseTower extends BaseTower {
    public BaseBaseTower(Pose pose, TowerTemplate template) {
        super(pose, template);
        for (double index = 0; index < 4; index += 1.0) {
            inputDirections.add(index * (Math.PI / 2));
        }
    }
    public boolean addToInventory(int x, int y, Resource resource) {
        ResourceManager.getInstance().queryInventory(resource.getName()).add(resource.getQuantity());
        return true;
    }

}

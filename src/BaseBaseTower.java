public final class BaseBaseTower extends BaseTower {
    //player's base that holds their resources and is the enemy target
    public BaseBaseTower(Pose pose, TowerTemplate template) {
        super(pose, template);
        // makes all sides input directions
        for (double index = 0; index < 4; index += 1.0) {
            inputDirections.add(index * (Math.PI / 2));
        }
    }
    /*attempts to add a provided resource to the player's inventory and returns if it was successful or no.
    (should always be successful unless that resource is full in the inventory)*/
    public boolean addToInventory(int x, int y, Resource resource) {
        ResourceManager.getInstance().queryInventory(resource.getName()).add(resource.getQuantity());
        return true;
    }

}

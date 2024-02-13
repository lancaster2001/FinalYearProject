import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class BaseDrillTower extends BaseTower {
    private double actAccumulator = 0.0;
    private final double actAccumulatorLimit = 1.0;

    public BaseDrillTower(Pose pose, TowerTemplate template) {
        super(pose, template);
        imageLink = template.getImageLink();
        inventorySize = 5;
        for (double index = 0; index < 4; index += 1.0) {
            outputDirections.add(index * (Math.PI / 2));
        }
    }

    public void act(BaseTile tile, ResourceManager resourceManagerInstance) {
        resourceManagerInstance.queryInventory(tile.getResource()).add();
    }

    public void tick(BaseTile tile, double tickMultiplier) {
        actAccumulator += tickMultiplier / speed;
        if (actAccumulator >= actAccumulatorLimit) {
            actAccumulator -= actAccumulatorLimit;
            Resource resource = ResourceManager.getInstance().getResource(tile.getResource());
            resource.add();
            if (inventory.size() < inventorySize) {
                inventory.add(resource);
            }
        }
        if (!inventory.isEmpty()) {
            boolean conveyerCheck = false;
            Map mapInstance = GameState.getInstance().getMapInstance();
            MapSlot slotToCheck = null;
            if (pose.getTheta() == 0) {
                slotToCheck = mapInstance.getMapSection(new Rectangle2D.Double(pose.getX(), pose.getY() - 1, 0.5, 0.5)).getFirst();
            } else if (pose.getTheta() == Math.PI / 2) {
                slotToCheck = mapInstance.getMapSection(new Rectangle2D.Double(pose.getX() + 1, pose.getY(), 0.5, 0.5)).getFirst();
            } else if (pose.getTheta() == Math.PI) {
                slotToCheck = mapInstance.getMapSection(new Rectangle2D.Double(pose.getX(), pose.getY() + 1, 0.5, 0.5)).getFirst();
            } else if (pose.getTheta() == Math.PI * 1.5) {
                slotToCheck = mapInstance.getMapSection(new Rectangle2D.Double(pose.getX() - 1, pose.getY(), 0.5, 0.5)).getFirst();
            }
            if (slotToCheck != null) {
                outputResource(inventory.getFirst());
            }
        }
    }
}

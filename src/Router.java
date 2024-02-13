import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;

public class Router extends conveyor {
    double nextDirection;
    ArrayList<Double> directionsToOutput = new ArrayList<>();
    private HashMap<String, Double> timers = new HashMap<String, Double>();

    public Router(Pose pose, TowerTemplate template) {
        super(pose, template);
        nextDirection = pose.getTheta();
        for (double index = 0; index < 4; index += 1.0) {
            inputDirections.add(index * (Math.PI / 2));
        }
        speed = 2;
    }

    public void draw(Graphics g, int x, int y, int slotWidth, int slotHeight, AssetManager assetManagerInstance) {
        super.drawNoResources(g, x, y, slotWidth, slotHeight, assetManagerInstance);
    }

    public void tick(double tickMultiplier) {
        if (!inventory.isEmpty()) {
            setDirectionOfResources();
            setDirectionCheckScammer(inventory.getFirst());
            if (timers.containsKey(inventory.getFirst().getId())) {
                timers.replace(inventory.getFirst().getId(), timers.get(inventory.getFirst().getId()) + tickMultiplier);
            } else {
                timers.put(inventory.getFirst().getId(), tickMultiplier);
            }
            if ((timers.get(inventory.getFirst().getId()) > (1 / speed))) {
                outputResource(inventory.getFirst());
            }
        }
    }

    private void setDirectionCheckScammer(Resource resource) {
        boolean setDirectionCheck = false;
        for (String resourceID : setDirectionList) {
            if (resource.getId().equalsIgnoreCase(resourceID)) {
                setDirectionCheck = true;
            }
        }
        if (!setDirectionCheck) {
            setDirectionList.add(resource.getId());
        }
    }

}

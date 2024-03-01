import java.awt.*;

public class Router extends conveyor {
    double nextDirection;
    public Router(Pose pose, TowerTemplate template) {
        super(pose, template);
        outputAccumulatorLimit =0;
        outputDirections.clear();
        nextDirection = pose.getTheta();
        for (double index = -1; index < 4; index += 1.0) {
            outputDirections.add(index * (Math.PI / 2));
        }
    }

    public void draw(Graphics g, int x, int y, int slotWidth, int slotHeight, AssetManager assetManagerInstance) {
        super.draw(g, x, y, slotWidth, slotHeight, assetManagerInstance);
    }

    public void tick(double tickMultiplier) {
        outputAccumulator+=tickMultiplier;
        if (outputAccumulator>outputAccumulatorLimit){
            outputAccumulator = outputAccumulatorLimit;
        }
        if (!inventory.isEmpty()) {
            setDirectionOfResources(tickMultiplier);
            setDirectionCheckScammer(inventory.getFirst());
            if(!inventory.isEmpty()) {
                outputResource(inventory.getFirst());
            }
        }
    }

    private void setDirectionCheckScammer(Resource resource) {
        //trollolololoolooloolololololololollool
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


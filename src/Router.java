import java.awt.*;

public final class Router extends Conveyor {
    public Router(Pose pose, TowerTemplate template) {
        super(pose, template);
        outputAccumulatorLimit =0;
        outputDirections.clear();
        for (double index = -1; index < 4; index += 1.0) {
            outputDirections.add(index * (Math.PI / 2));
        }
    }

    public void draw(Graphics g, Rectangle rectangle, AssetManager assetManagerInstance) {
        super.draw(g,rectangle,assetManagerInstance);
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
        //this was a temporary fix for an issue of inheriting from conveyor that I intended to replace but ran out of time for
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


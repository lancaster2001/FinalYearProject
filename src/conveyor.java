import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Objects;

public class conveyor extends BaseTower {
    ArrayList<String> setDirectionList = new ArrayList<>();

    public conveyor(Pose pose, TowerTemplate template) {
        super(pose, template);
        inventorySize = 4;
        for (double index = -1; index < 3; index += 1.0) {
            if (index * (Math.PI / 2) != pose.getTheta()) {
                inputDirections.add(index * (Math.PI / 2));
            }
        }
        outputDirections.add(pose.getTheta());
    }

    public void tick(double tickMultiplier) {
        int index = 0;
        ArrayList<Integer> indexesToRemove = new ArrayList<>();
        setDirectionOfResources(tickMultiplier);
        try {
            for (Resource resource : inventory) {
                Pose resourcePose = resource.getPose();
                boolean withinSlot = new Rectangle2D.Double(pose.getX(), pose.getY(), 1, 1).intersects(resourcePose.getX(), resourcePose.getY(), resource.getWidth(), resource.getHeight());
                if (!withinSlot) {
                    if (moveResourceToOtherSlots(resource)) {
                        indexesToRemove.add(index);
                    }
                } else {
                    boolean collisioncheck = false;
                    for (int resourceIndex = index; resourceIndex > -1; resourceIndex--) {
                        if ((!Objects.equals(resource.getId(), inventory.get(resourceIndex).getId())) && (resource.collisionCheck(inventory.get(resourceIndex)))) {
                            collisioncheck = true;
                        }
                    }
                    if (!collisioncheck) {
                        resource.move(speed * tickMultiplier);
                    }

                }
                index += 1;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        removeListOfIndexes(indexesToRemove);
    }

    protected void setDirectionOfResources(double tickMultiplier) {
        for (Resource resource : inventory) {
            boolean setDirectionCheck = false;
            Pose resourcePose = resource.getPose();
            double xCentre = pose.getX() + (0.5);
            double yCentre = pose.getY() + (0.5);
            double bounds = ((tickMultiplier) * speed);
            boolean xBound = (xCentre - bounds < resourcePose.getX() + (resource.getWidth() / 2)) && (xCentre + bounds > resourcePose.getX() + (resource.getWidth() / 2));
            boolean yBound = (yCentre - bounds < resourcePose.getY() + (resource.getHeight() / 2)) && (yCentre + bounds > resourcePose.getY() + (resource.getHeight() / 2));
            boolean centreCheck = false;
            if (xBound && yBound) {
                centreCheck = true;
            }
            if (centreCheck) {
                for (String resourceID : setDirectionList) {
                    if (resource.getId().equalsIgnoreCase(resourceID)) {
                        setDirectionCheck = true;
                    }
                }
                if (!setDirectionCheck) {
                    resource.setTheta(pose.getTheta());
                    setDirectionList.add(resource.getId());
                }
            }
        }
    }

    protected boolean moveResourceToOtherSlots(Resource resource) {

        Map mapInstance = GameState.getInstance().getMapInstance();
        MapSlot slotToCheck = null;
        Pose resourcePose = resource.getPose();
        boolean withinSlot = new Rectangle2D.Double(pose.getX(), pose.getY(), 1, 1).intersects(resourcePose.getX(), resourcePose.getY(), resource.getWidth(), resource.getHeight());

        if (!withinSlot) {
            slotToCheck = mapInstance.getMapSection(new Rectangle2D.Double(resourcePose.getX(), resourcePose.getY(), 0.5, 0.5)).getFirst();
        }
        if (slotToCheck != null) {
            boolean containsCheck = new Rectangle2D.Double(slotToCheck.getX(), slotToCheck.getY(), 1, 1).contains(resource.getPose().getX(), resource.getPose().getY(), resource.getWidth(), resource.getHeight());
            if(!containsCheck){
                for(double direction:outputDirections){
                    if(getDirectionOfInput((int)resourcePose.getX(),(int)resourcePose.getY())==direction){
                        containsCheck=true;
                    }
                }
            }
            if (containsCheck) {
                try {
                    if (slotToCheck.getTower().addToInventory((int) pose.getX(), (int) pose.getY(), resource)) {
                        int index = 0;
                        for (String resourceID : setDirectionList) {
                            if(resourceID.equalsIgnoreCase(resource.getId())){
                                break;
                            }
                            index+=1;
                        }
                        setDirectionList.remove(index);
                        return true;
                    }
                } catch (Exception e) {

                }
            }
        }
        return false;
    }

    public void draw(Graphics g, int x, int y, int slotWidth, int slotHeight, AssetManager assetManagerInstance) {
        super.draw(g, x, y, slotWidth, slotHeight, assetManagerInstance);
        for (Resource resource : inventory) {
            ResourceManager.getInstance().addToDrawList(resource);
        }
    }

    protected void drawNoResources(Graphics g, int x, int y, int slotWidth, int slotHeight, AssetManager assetManagerInstance) {
        super.draw(g, x, y, slotWidth, slotHeight, assetManagerInstance);
    }
    /*public boolean addResource(Resource resource){
        boolean check = true;
        for(Resource resource1:inventory){
            if(resource1.getId().equals(resource.getId())){
                check = false;
            }
        }
        if(check &&(inventory.size()<1)){
                inventory.add(resource);
                return true;
        }
        return false;
    }*/
}

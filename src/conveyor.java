import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class conveyor extends  BaseTower{
    gameConstants.DIRECTION direction;
    ArrayList<Resource> inventory = new ArrayList<>();
    Resource leavingResource;
    public conveyor(Pose pose, TowerTemplate template) {
        super(pose, template);
    }
    public void tick(double tickMultiplier) {
        int index = 0;
        ArrayList<Integer> indexesToRemove = new ArrayList<>();
        for(Resource resource:inventory){
            Rectangle2D.Double placement = new Rectangle2D.Double(resource.getPose().getX(),resource.getPose().getY(),resource.getWidth(),resource.getHeight());
            Map mapInstance = GameState.getInstance().getMapInstance();
            MapSlot slotToCheck = null;
            if(pose.getTheta() ==0){
                slotToCheck=mapInstance.getMapSection(new Rectangle2D.Double(pose.getX(),pose.getY()-1,0.5,0.5)).getFirst();
            }else if(pose.getTheta() == Math.PI/2){
                slotToCheck=mapInstance.getMapSection(new Rectangle2D.Double(pose.getX()+1,pose.getY(),0.5,0.5)).getFirst();
            }else if(pose.getTheta() ==Math.PI){
                slotToCheck=mapInstance.getMapSection(new Rectangle2D.Double(pose.getX(),pose.getY()+1,0.5,0.5)).getFirst();
            }else if(pose.getTheta() ==Math.PI*1.5){
                slotToCheck=mapInstance.getMapSection(new Rectangle2D.Double(pose.getX()-1,pose.getY(),0.5,0.5)).getFirst();
            }
            if(slotToCheck!=null){
                boolean containsCheck = new Rectangle2D.Double(slotToCheck.getX(),slotToCheck.getY(),1,1).contains(resource.getPose().getX(),resource.getPose().getY(),resource.getWidth(),resource.getHeight());
                if(containsCheck) {
                    if (slotToCheck.getTower() instanceof conveyor) {
                        if (((conveyor) slotToCheck.getTower()).addResource(resource)) {
                            indexesToRemove.add(index);
                        }
                    }
                }else {
                    Pose resourcePose = resource.getPose();
                    double xCentre = pose.getX()+(0.5);
                    double yCentre = pose.getY()+(0.5);
                    double bounds = (((double) gameConstants.gameTickRate)*speed) /1000;
                    boolean xBound = (xCentre-bounds<(resourcePose.getX()+(resource.getWidth()/2)))&&(xCentre+bounds>(resourcePose.getX()+(resource.getWidth()/2)));
                    boolean yBound = (yCentre-bounds<(resourcePose.getY()+(resource.getHeight()/2)))&&(yCentre+bounds>resourcePose.getY()+(resource.getHeight()/2));
                    if(((pose.getTheta()== Math.PI/2)||(pose.getTheta()== Math.PI*1.5))&&(yBound)){
                        resource.move(pose.getTheta(), speed * tickMultiplier);
                    }else if(((pose.getTheta()== 0)||(pose.getTheta()== Math.PI))&&(xBound)){
                        resource.move(pose.getTheta(), speed * tickMultiplier);
                    }else{
                        resource.move(resourcePose.getTheta(), speed * tickMultiplier);
                    }
                }
            }
            index+=1;
        }
        removeListOfIndexes(indexesToRemove);
    }
    private void removeListOfIndexes(ArrayList<Integer> indexesToRemove){
        for(int toRemoveIndex = indexesToRemove.size()-1;toRemoveIndex>=0;toRemoveIndex--){
            for(int bulletIndex = inventory.size()-1;bulletIndex>=0;bulletIndex--){
                if(indexesToRemove.get(toRemoveIndex)==bulletIndex){
                    inventory.remove(bulletIndex);
                    break;
                }
            }
        }
    }
    public void draw(Graphics g,int x, int y, int slotWidth ,int slotHeight,AssetManager assetManagerInstance){
        super.draw(g,x,y,slotWidth,slotHeight,assetManagerInstance);
        for(Resource resource:inventory){
            //resource.draw(g,x,y,slotWidth,slotHeight,assetManagerInstance);
            ResourceManager.getInstance().addToDrawList(resource);
        }
    }
    public boolean addResource(Resource resource){
        boolean check = true;
        for(Resource resource1:inventory){
            if(resource1.getId().equals(resource.getId())){
                check = false;
            }
        }
        if(check &&(inventory.isEmpty())){
                inventory.add(resource);
                return true;
        }
        return false;
    }
}

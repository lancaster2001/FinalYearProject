import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class BaseDrillTower extends BaseTower{
    private double actAccumulator = 0.0;
    private final double actAccumulatorLimit = 1.0;
    ArrayList<Resource> inventory = new ArrayList<>();
    public BaseDrillTower(Pose pose,TowerTemplate template){
        super(pose,template);
        imageLink = template.getImageLink();
    }
    public void act(BaseTile tile, ResourceManager resourceManagerInstance){
        resourceManagerInstance.queryInventory(tile.getResource()).add();
    }
    public void tick(BaseTile tile,double tickMultiplier){
        actAccumulator+=tickMultiplier*speed;
        if (actAccumulator>=actAccumulatorLimit){
            actAccumulator-=actAccumulatorLimit;
            inventory.add(ResourceManager.getInstance().getResource(tile.getResource()));
        }
        if (!inventory.isEmpty()) {
            boolean conveyerCheck = false;
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
                if (slotToCheck.getTower() instanceof conveyer) {
                    ((conveyer)slotToCheck.getTower()).addResource(ResourceManager.getInstance().getResource(inventory.get(0).getName(),new Pose(slotToCheck.getX(),slotToCheck.getY(),0)));
                }else{
                    ResourceManager.getInstance().queryInventory(tile.getResource()).add();
                }
            }
        }
    }
}

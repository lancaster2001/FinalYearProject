import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class conveyer extends  BaseTower{
    gameConstants.DIRECTION direction;
    ArrayList<Resource> inventory = new ArrayList<>();
    double speed;
    public conveyer(Pose pose, TowerTemplate template) {
        super(pose, template);
    }
    public void tick(double tickMultiplier) {
        for(Resource resource:inventory){
            resource.move(pose.getTheta(),speed*tickMultiplier);
            if(new Rectangle2D.Double(pose.getX(),pose.getY(),width,height).intersects(resource.getPose().getX(),resource.getPose().getY(),0.5,0.5)){
            //fix resource movement
                // remove the resource and pass it on
            }
        }
    }
    public void draw(Graphics g,int x, int y, int slotWidth ,int slotHeight,AssetManager assetManagerInstance){
        super.draw(g,x,y,slotWidth,slotHeight,assetManagerInstance);
        for(Resource resource:inventory){
            resource.draw(g,x,y,slotWidth,slotHeight,assetManagerInstance);
        }
    }
    public void addResource(Resource resource){
        inventory.add(resource);
    }
}

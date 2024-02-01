import java.awt.*;

public abstract class BaseTurretTower extends BaseTower{
    protected Pose pose;
    protected double range = 0.0;
    private double shootAccumulator = 0.0;
    private double shootAccumulatorLimit = 1.0;
    private EnemyManager enemyManagerInstance = EnemyManager.getInstance();
    private boolean checkForEnemies(){
        for(BaseEnemy enemy: enemyManagerInstance.getEnemyList()){
            if (range>= calculateDistantToTarget(enemy.getPose().getX(),enemy.getPose().getY())){
                calculateDirectionToTarget(enemy.getPose().getX(),enemy.getPose().getY());
                return true;
            }
        }
        return false;
    }
    private void calculateDirectionToTarget(double x, double y){
        double atan2_x =x-pose.getX();
        double atan2_y =y-pose.getY();
        double rot1 = Math.atan2(atan2_y,atan2_x);
        pose.setTheta(rot1);
    }
    private double calculateDistantToTarget(double x, double y){
        return (Math.sqrt((Math.pow(pose.getX() - x,2))+(Math.pow(pose.getY()-y,2))));
    }
    public void tick(double tickMultiplier){

        shootAccumulator+=tickMultiplier;
        if (shootAccumulator>=shootAccumulatorLimit){
            shootAccumulator=shootAccumulatorLimit;
            if(checkForEnemies()){
                shoot();
            }
        }
    }
    public void setup(int x,int y){
        pose = new Pose(x,y,0.0);
    }
}

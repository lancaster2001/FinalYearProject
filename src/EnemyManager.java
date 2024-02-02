import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class EnemyManager {
    //singleton------------------------------------------------------------------------
    private static EnemyManager instance;
    public static EnemyManager getInstance(){
        if (instance == null) {
            instance = new EnemyManager();
        }
        return instance;
    }
    private EnemyManager(){
        enemyList.add(new Enemy1(1,1));
    }
    //----------------------------------------------------------------------------------
    private final ArrayList<BaseEnemy> enemyList = new ArrayList<>();
    public void tick(double tickMultiplier){
        for(BaseEnemy enemy: enemyList){
            enemy.tick(tickMultiplier);
        }
    }
    public void drawEnemies(Graphics g){
        for(BaseEnemy enemy: enemyList){
            if(enemy.onScreenCheck()){
                enemy.draw(g);
            }
        }
    }
    public void randomEnemyChance(){
        Random rnd = new Random();
        if(rnd.nextInt(1,10)<11){
            int xSpawn;
            int ySpawn;
            if(rnd.nextInt(1,2)==1){
                xSpawn = rnd.nextInt(1,gameConstants.mapWidth);
                if(rnd.nextInt(1,2)==1){
                    ySpawn = 0;
                }else{
                    ySpawn = gameConstants.mapHeight;
                }
            }else{
                ySpawn = rnd.nextInt(1,gameConstants.mapHeight);
                if(rnd.nextInt(1,2)==1){
                    xSpawn = 0;
                }else{
                    xSpawn = gameConstants.mapWidth;
                }
            }
            enemyList.add(new Enemy1(xSpawn,ySpawn));
        }
    }

    public ArrayList<BaseEnemy> getEnemyList() {
        return enemyList;
    }
}

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class EnemyManager {
    //singleton------------------------------------------------------------------------
    private static EnemyManager instance = new EnemyManager();
    public static EnemyManager getInstance(){
        if (instance == null) {
            instance = new EnemyManager();
        }
        return instance;
    }
    private EnemyManager(){
    }
    //----------------------------------------------------------------------------------
    private final ArrayList<BaseEnemy> enemyList = new ArrayList<>();
    public void tick(double tickMultiplier){
        ArrayList<Integer> enemiesToRemove = new ArrayList<Integer>();
        for(Integer index = 0; index < enemyList.size();index++){
            if(!enemyList.get(index).getAlive()){
                enemiesToRemove.add(index);
            }
        }
        if(!enemiesToRemove.isEmpty()){
            removeListOfIndexes(enemiesToRemove);
        }
        for(Integer index = 0; index < enemyList.size();index++){
            enemyList.get(index).tick(tickMultiplier);
        }
        randomEnemyChance();
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
        if(rnd.nextInt(1,10)==11){
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
        if(rnd.nextInt(1,100)==50){
            enemyList.add(new Enemy1(1,1));
        }
    }
    private void removeListOfIndexes(ArrayList<Integer> indexesToRemove){
        for(int toRemoveIndex = indexesToRemove.size()-1;toRemoveIndex>=0;toRemoveIndex--){
            for(int index = enemyList.size()-1;index>=0;index--){
                if(indexesToRemove.get(toRemoveIndex)==index){
                    enemyList.remove(index);
                    break;
                }
            }
        }
    }

    public ArrayList<BaseEnemy> getEnemyList() {
        return enemyList;
    }
}

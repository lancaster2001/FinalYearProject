import java.io.ObjectInputStream;

public class MapSlot {
    private int x = -1;
    private int y = -1;
    private BaseTower tower;
    private BaseTile tile;

    public MapSlot(int x,int y){
        this.x = x;
        this.y = y;
        //tower = new TowerVariant1();
        tile = new GrassTile();
    }
    public void tick(double tickMultiplier){
        if(tower!=null){
            tower.tick(tile,tickMultiplier);
            tower.tick(tickMultiplier);
        }
    }
    public void setTower(BaseTower newTower){
        tower = newTower;
        tower.setup(x,y);
    }

    public int getX(){return x;}
    public int getY(){return y;}
    public BaseTile getTile() {return tile;}
    public BaseTower getTower() {return tower;}
}

import java.awt.*;

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
    protected void draw(Graphics g,Camera cameraInstance,AssetManager assetManagerInstance){
        int widthOfSlot = cameraInstance.getwidthOfSlot();
        int heightOfSlot = cameraInstance.getheightOfslot();
        int x = (this.x-cameraInstance.getX())*widthOfSlot;
        int y =(this.y-cameraInstance.getY())*heightOfSlot;
        int width = cameraInstance.getwidthOfSlot();
        int height = cameraInstance.getheightOfslot();
        if(tile!=null){
            tile.draw(g,x,y,width,height,assetManagerInstance);
        }
        if (tower != null) {
            tower.draw(g,x,y,width,height,assetManagerInstance);
        }
        g.setColor(Color.BLACK);
        g.drawRect(x, y, widthOfSlot, heightOfSlot);
        g.setColor(gameConstants.resourceMenuTitleColour);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        //g.drawString(this.x+", "+this.y, x, y + gameConstants.ResourcesMenuTitleSize);
    }
    public boolean onScreenCheck(Camera cameraInstance){
        return cameraInstance.isOnCamera(x,y);
    }
    public void setTower(TowerTemplate newTower){
        if(newTower.getType().equals("Turret")){
            tower = new BaseTurretTower(x,y,newTower);
        }else if(newTower.getType().equals("Drill")){
            tower = new BaseDrillTower(x,y,newTower);
        }
    }

    public int getX(){return x;}
    public int getY(){return y;}
    public BaseTile getTile() {return tile;}
    public BaseTower getTower() {return tower;}
}

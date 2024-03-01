import org.json.JSONObject;

import java.awt.*;

public class MapSlot {
    private int x = -1;
    private int y = -1;
    private BaseTower tower;
    private BaseTower tempTower;
    private BaseTile tile;

    public MapSlot(int x, int y, TileTemplate tile) {
        this.x = x;
        this.y = y;
        this.tile = new BaseTile(tile);
    }

    public MapSlot(JSONObject jsonObject) {

        JSONObject position = jsonObject.getJSONObject("Position");
        JSONObject tile = jsonObject.getJSONObject("Tile");
        JSONObject tower;
        try {
            tower = jsonObject.getJSONObject("Tower");
        } catch (Exception e) {
            tower = null;
        }
        x = position.getInt("X");
        y = position.getInt("Y");
        this.tile = new BaseTile(TileManager.getInstance().loadTileTemplate(tile));
        if (tower != null) {
            try {
                TowerTemplate template = TowerManager.getInstance().getTemplate(tower.getString("Name"));
                setTower(template, tower.getDouble("Theta"),false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void setTowerFromTempTower(){
        setTower(tempTower);
        tempTower = null;
    }

    public void tick(double tickMultiplier) {
        if(tower != null) {
            try {
                tower.tick(tile, tickMultiplier);
                tower.tick(tickMultiplier);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void damageTower(double damage) {
        if (tower != null) {
            tower.takeDamage(damage);
        }
        if (tower.getHealth() <= 0) {
            tower = null;
        }
    }

    protected void draw(Graphics g, int x, int y, int slotWidth, int slotHeight, AssetManager assetManagerInstance) {
        if (tile != null) {
            tile.draw(g, x, y, slotWidth, slotHeight, assetManagerInstance);
        }
        if (tower != null) {
            tower.draw(g, x, y, slotWidth, slotHeight, assetManagerInstance);
        }
        if (tempTower != null) {
            tempTower.draw(g, x, y, slotWidth, slotHeight, assetManagerInstance);
        }
        if(gameSettings.getInstance().isOutlineSlots()) {
            g.setColor(Color.BLACK);
            g.drawRect(x, y, slotWidth, slotHeight);
        }
        if(gameSettings.getInstance().isDebugging()) {
            g.setColor(Color.red);
            g.setFont(new Font("Arial", Font.BOLD, 10));
            g.drawString(this.x + ", " + this.y, x, y + 10);
        }
    }

    public boolean onScreenCheck(Camera cameraInstance) {
        return cameraInstance.isOnCamera(x, y);
    }

    public void clearTower() {
        if(tower!=null){
            ResourceManager.getInstance().queryInventory(tower.getTempalate().getCostResource()).add(tower.getTempalate().getCostQuantity());
        }
        tower = null;
    }
    public void clearTempTower() {
        tempTower = null;
    }

    public JSONObject getJsonObject() {
        JSONObject json = new JSONObject();
        JSONObject j = new JSONObject();
        j.put("X", x);
        j.put("Y", y);
        json.put("Position", j);
        if (tower != null) {
            json.put("Tower", tower.getJsonObject());
        } else {
            json.put("Tower", "null");
        }
        json.put("Tile", tile.getJsonObject());
        return json;
    }
    public void setTempTower(TowerTemplate newTower, double theta) {
        int cost = newTower.getCostQuantity();
        newTower.setCostQuantity(0);
        tempTower = getTowerFromTemplate(newTower,theta);
        newTower.setCostQuantity(cost);
    }
    private BaseTower getTowerFromTemplate(TowerTemplate newTower, double theta) {
        BaseTower returnTower;
        if (newTower.getType().equalsIgnoreCase("Turret")) {
            returnTower = new BaseTurretTower(new Pose(x, y, theta), newTower);
        } else if (newTower.getType().equalsIgnoreCase("Drill")) {
            returnTower = new BaseDrillTower(new Pose(x, y, theta), newTower, tile.getResource());
        } else if (newTower.getType().equalsIgnoreCase("Conveyors")) {
            returnTower = new conveyor(new Pose(x, y, theta), newTower);
        } else if (newTower.getType().equalsIgnoreCase("Router")) {
            returnTower = new Router(new Pose(x, y, theta), newTower);
        }else if (newTower.getType().equalsIgnoreCase("NonPlayer")){
            returnTower = new BaseTurretTower(new Pose(x, y, 0),newTower);
        }else if (newTower.getType().equalsIgnoreCase("Base")){
            returnTower = new BaseBaseTower(new Pose(x, y, 0),newTower);
        }else{
            return null;
        }
        return returnTower;
    }
    public void setTower(TowerTemplate newTower, double theta){
        setTower(newTower,theta,true);
    }
    public void setTower(TowerTemplate newTower, double theta,boolean charge) {
        setTower(getTowerFromTemplate(newTower,theta),charge);
    }
    public void setTower(BaseTower newTower){
        setTower(newTower,true);
    }
    public void setTower(BaseTower newTower,boolean charge) {

        if(charge){
            if(ResourceManager.getInstance().queryInventory(newTower.getTempalate().getCostResource()).remove(newTower.getTempalate().getCostQuantity())){
                clearTower();
                tower = newTower;
            }
        }else{
            clearTower();
            tower = newTower;
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public BaseTile getTile() {
        return tile;
    }

    public BaseTower getTower() {
        return tower;
    }
    public BaseTower getTempTower(){return  tempTower;}
}

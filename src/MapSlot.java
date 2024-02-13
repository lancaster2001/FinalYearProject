import org.json.JSONObject;

import java.awt.*;

public class MapSlot {
    private int x = -1;
    private int y = -1;
    private BaseTower tower;
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
                ResourceManager.getInstance().queryInventory(template.getCostResource()).add(template.getCostQuantity());
                setTower(template, tower.getDouble("Theta"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void tick(double tickMultiplier) {
        if (tower != null) {
            tower.tick(tile, tickMultiplier);
            tower.tick(tickMultiplier);
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
        g.setColor(Color.BLACK);
        //g.drawRect(x, y, slotWidth, slotHeight);
        g.setColor(Color.red);
        g.setFont(new Font("Arial", Font.BOLD, 10));
        //g.drawString(this.x+", "+this.y, x, y + 10);
    }

    public boolean onScreenCheck(Camera cameraInstance) {
        return cameraInstance.isOnCamera(x, y);
    }

    public void setTower(TowerTemplate newTower, double theta) {
        if (ResourceManager.getInstance().queryInventory(newTower.getCostResource()).remove(newTower.getCostQuantity())) {
            if (newTower.getType().equals("Turret")) {
                tower = new BaseTurretTower(new Pose(x, y, theta), newTower);
            } else if (newTower.getType().equals("Drill")) {
                tower = new BaseDrillTower(new Pose(x, y, theta), newTower);
            } else if (newTower.getType().equals("Conveyors")) {
                tower = new conveyor(new Pose(x, y, theta), newTower);
            } else if (newTower.getType().equals("Router")) {
                tower = new Router(new Pose(x, y, theta), newTower);
            }
        }
    }

    public void clearTower() {
        tower = null;
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

    public void setTower(BaseTower newTower) {
        tower = newTower;
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
}

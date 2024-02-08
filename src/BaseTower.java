import org.json.JSONObject;

import javax.naming.Name;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public abstract class BaseTower {
    protected Pose pose = new Pose();
    protected String name;
    protected String imageLink = "src/Towers/Base/irondrill.png";
    protected double width = 1.0;
    protected double height = 1.0;
    protected  double maxHealth;
    protected double health;
    protected String costResource = "Rock";
    protected int costQuantity = 5;
    private TowerTemplate tempalate;
    public void tick(BaseTile tile,double tickMultiplier){
    }
    public void tick(double tickMultiplier){}
    public BaseTower(int x, int y,TowerTemplate template){
        this.pose.setX(x);
        this.pose.setY(y);
        this.name = template.getName();
        this.costResource = template.getCostResource();
        this.costQuantity = template.getCostQuantity();
        this.imageLink = template.getImageLink();
        this.maxHealth = template.getMaxHealth();
        health = maxHealth;
        this.tempalate = template;
    }

    public void takeDamage(double damage){
        health-=damage;
    }
    protected String getImageLink(){return imageLink;}

    public Pose getPose() {
        return pose;
    }
    protected void draw(Graphics g,int x, int y, int slotWidth ,int slotHeight,AssetManager assetManagerInstance){
        int width = (int)(this.width * slotWidth);
        int height = (int)(this.height * slotHeight);
        Rectangle towerBox = new Rectangle(x, y, width, height);
        g.drawImage(AssetManager.getInstance().getImage("Towers",imageLink), towerBox.x, towerBox.y, towerBox.width, towerBox.height, null);
        drawHealthBar(g, towerBox);
    }
    protected void drawHealthBar(Graphics g, Rectangle hitbox){
        if(maxHealth > health){
            int height = hitbox.height/10;
            g.setColor(Color.white);
            g.drawRect(hitbox.x,hitbox.y-height,hitbox.width,height);
            g.setColor(Color.red);
            g.drawRect(hitbox.x,hitbox.y-height,(int)((health/maxHealth)*hitbox.width),height);
        }
    }
    protected JSONObject getJsonObject(){
        JSONObject j = new JSONObject();
        j.put("X", pose.getX());
        j.put("Y", pose.getY());
        j.put("Theta", pose.getTheta());
        j.put("Name", tempalate.getName());
        j.put("Type", tempalate.getType());
        return j;
    }
    protected String getCostResource(){
        return costResource;
    }
    protected int getCostQuantity(){
        return costQuantity;
    }

    public double getHealth() {
        return health;
    }

    public TowerTemplate getTempalate() {
        return tempalate;
    }

}

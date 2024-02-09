import java.awt.*;
import java.awt.image.BufferedImage;

public class Resource {
    String id = String.valueOf(Math.random());
    private int quantity = 0;
    double width = 0.5;
    double height = 0.5;
    Pose pose;
    private String name;
    private String tileImageLink;
    private String iconImageLink;

    public Resource(String name,String tileImageLink,String iconImageLink){
        this.name = name;
        this.tileImageLink = tileImageLink;
        this.iconImageLink = iconImageLink;
    }
    public Resource(String name,String tileImageLink,String iconImageLink,Pose pose){
        this.name = name;
        this.tileImageLink = tileImageLink;
        this.iconImageLink = iconImageLink;
        this.pose= pose;
    }
    public void draw(Graphics g, double x, double y, int slotWidth , int slotHeight, AssetManager assetManagerInstance) {
        int width = (int) (this.width * slotWidth);
        int height = (int) (this.height * slotHeight);
        int xToDraw = (int)((pose.getX()-x)*slotWidth);
        int yToDraw =(int)((pose.getY()-y)*slotHeight);
        Rectangle towerBox = new Rectangle(xToDraw,yToDraw, width, height);
        g.drawImage(AssetManager.getInstance().getImage("Towers",iconImageLink), towerBox.x, towerBox.y, towerBox.width, towerBox.height, null);

    }
    public void place(Pose pose) {
        this.pose = pose;
    }
    public void move(double Theta, double distance) {
        double targetX = (distance * Math.sin(Theta));
        double targetY = (distance * Math.cos(Theta));
        double x = pose.getX();
        double xtoset = x + targetX;
        pose.setX(xtoset);
        pose.setY(pose.getY() + targetY);
    }
    public boolean add(){
        quantity+=1;
        return true;
    };
    public boolean add(int quantity){
        this.quantity+=quantity;
        return true;
    };
    public boolean remove(){
        if(quantity==0){
            return false;
        }else{
            quantity-=1;
         return true;
        }
    }
    public boolean remove(int quantity){
        if((this.quantity-quantity)<0){
            return false;
        }else{
            this.quantity-=quantity;
            return true;
        }
    }

    public int getQuantity() {
        return quantity;
    }

    public String getName() {
        return name;
    }

    public String getIconImageLink() {
        return iconImageLink;
    }

    public String getTileImageLink() {
        return tileImageLink;
    }

    public Pose getPose() {
        return pose;
    }

    public String getId() {
        return id;
    }
}

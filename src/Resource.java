import java.awt.*;

public class Resource {
    String id = String.valueOf(Math.random());
    private int quantity = 0;
    double width = 0.5;
    double height = 0.5;
    Pose pose;
    private String name;
    private String tileImageLink;
    private String iconImageLink;

    public Resource(String name, String tileImageLink, String iconImageLink) {
        this.name = name;
        this.tileImageLink = tileImageLink;
        this.iconImageLink = iconImageLink;
    }

    public Resource(String name, String tileImageLink, String iconImageLink, Pose pose) {
        this.name = name;
        this.tileImageLink = tileImageLink;
        this.iconImageLink = iconImageLink;
        this.pose = pose;
    }

    public void draw(Graphics g, int x, int y, int slotWidth, int slotHeight, AssetManager assetManagerInstance) {
        int width = (int) (this.width * slotWidth);
        int height = (int) (this.height * slotHeight);
        g.drawImage(AssetManager.getInstance().getImage("Icons", iconImageLink), x, y, width, height, null);

    }

    public void move(double distance, double Theta) {
        double targetX = (distance * Math.sin(Theta));
        double targetY = (distance * Math.sin(Theta - (Math.PI / 2)));
        double x = pose.getX();
        double xtoset = x + targetX;
        pose.setX(xtoset);
        pose.setY(pose.getY() + targetY);
        pose.setTheta(Theta);
    }

    public void move(double distance) {
        double targetX = (distance * Math.sin(pose.getTheta()));
        double targetY = (distance * Math.sin(pose.getTheta() - (Math.PI / 2)));
        double x = pose.getX();
        double xtoset = x + targetX;
        pose.setX(xtoset);
        pose.setY(pose.getY() + targetY);
        pose.setTheta(pose.getTheta());
    }

    public boolean add() {
        quantity += 1;
        return true;
    }

    ;

    public boolean add(int quantity) {
        this.quantity += quantity;
        return true;
    }

    ;

    public boolean remove() {
        if (quantity == 0) {
            return false;
        } else {
            quantity -= 1;
            return true;
        }
    }

    public boolean remove(int quantity) {
        if ((this.quantity - quantity) < 0) {
            return false;
        } else {
            this.quantity -= quantity;
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

    public double getHeight() {
        return height;
    }

    public double getWidth() {
        return width;
    }

    public void setTheta(double theta) {
        pose.setTheta(theta);
    }

    public void setPose(Pose pose) {
        this.pose = pose;
    }
    public void setQuantity(int quantity){
        this.quantity = quantity;
    }
}

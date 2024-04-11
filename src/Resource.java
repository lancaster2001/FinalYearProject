import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public final class Resource {
    String id = String.valueOf(Math.random());
    private int quantity = 0;
    double width = 0.5;
    double height = 0.5;
    private Pose pose;
    private Color color;
    private String name;
    private String tileImageLink;
    private String iconImageLink;

    public Resource(String name, String tileImageLink, String iconImageLink) {
        this.name = name;
        this.tileImageLink = tileImageLink;
        this.iconImageLink = iconImageLink;
        color = getMostCommonColor(AssetManager.getInstance().getImage("Icons",iconImageLink));
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
    private static Color getMostCommonColor(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();

        // HashMap to store color counts
        HashMap<Integer, Integer> colorCounts = new HashMap<>();

        // Count occurrences of each color, ignoring transparent pixels
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = image.getRGB(x, y);
                // Check if the pixel is not transparent
                if ((rgb & 0xFF000000) != 0) {
                    colorCounts.put(rgb, colorCounts.getOrDefault(rgb, 0) + 1);
                }
            }
        }

        // Find the color with the highest occurrence
        int maxCount = 0;
        int mostCommonRGB = 0;
        for (int rgb : colorCounts.keySet()) {
            int count = colorCounts.get(rgb);
            if (count > maxCount) {
                maxCount = count;
                mostCommonRGB = rgb;
            }
        }

        // Convert RGB value to Color object
        Color mostCommonColor = new Color(mostCommonRGB);
        return mostCommonColor;
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
    public boolean collisionCheck(Resource resource){
        if (resource.getHitBox()!=null) {
            if (resource.getHitBox().intersects(getHitBox())) {
                return true;
            }
        }
        return false;
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
    public Rectangle2D.Double getHitBox(){
        if(pose!=null){
            return new Rectangle2D.Double(pose.getX()+width/4,pose.getY()+height/4,width/2,height/2);
        }
        return null;
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

    public Color getColor() {
        return color;
    }
}

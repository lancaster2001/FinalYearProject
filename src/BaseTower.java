import org.json.JSONObject;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public abstract class BaseTower {
    protected Pose pose = new Pose();
    protected String name;
    protected String imageLink = "src/Towers/Base/irondrill.png";
    protected double width = 1.0;
    protected double height = 1.0;
    protected double maxHealth;
    private double nextDirection;
    protected double health;
    protected double speed;
    protected String costResource = "Rock";
    protected int costQuantity = 5;
    protected int inventorySize = 0;
    protected ArrayList<Resource> inventory = new ArrayList<>();
    protected ArrayList<Double> outputDirections = new ArrayList<>();
    protected ArrayList<Double> inputDirections = new ArrayList<>();
    private TowerTemplate tempalate;

    public void tick(BaseTile tile, double tickMultiplier) {
    }

    public void tick(double tickMultiplier) {
    }

    public BaseTower(Pose pose, TowerTemplate template) {
        this.pose = pose;
        this.name = template.getName();
        this.costResource = template.getCostResource();
        this.costQuantity = template.getCostQuantity();
        this.imageLink = template.getImageLink();
        this.maxHealth = template.getMaxHealth();
        this.speed = template.getSpeed();
        health = maxHealth;
        this.tempalate = template;
    }

    public void takeDamage(double damage) {
        health -= damage;
    }

    protected String getImageLink() {
        return imageLink;
    }

    public Pose getPose() {
        return pose;
    }

    protected void draw(Graphics g, int x, int y, int slotWidth, int slotHeight, AssetManager assetManagerInstance) {
        int width = (int) (this.width * slotWidth);
        int height = (int) (this.height * slotHeight);
        Rectangle towerBox = new Rectangle(x, y, width, height);
        // Rotation information
        double rotationRequired = pose.getTheta();
        BufferedImage image = AssetManager.getInstance().getImage("Towers", imageLink);

        Graphics2D g2d = (Graphics2D) g;
        AffineTransform backup = g2d.getTransform();
        AffineTransform trans = new AffineTransform();
        trans.rotate(rotationRequired, (x + (width / 2)), (y + (height / 2))); // the points to rotate around (the center in my example, your left side for your problem)
        g2d.transform(trans);
        g2d.drawImage(image, x, y, width, height, null);  // the actual location of the sprite
        g2d.setTransform(backup); // restore previous transform
        //g.drawImage(AssetManager.getInstance().getImage("Towers",imageLink), towerBox.x, towerBox.y, towerBox.width, towerBox.height, null);
        drawHealthBar(g, towerBox);
    }
   /* protected void drawTemp(Graphics g, int x, int y, int slotWidth, int slotHeight, AssetManager assetManagerInstance) {
        int width = (int) (this.width * slotWidth);
        int height = (int) (this.height * slotHeight);
        Rectangle towerBox = new Rectangle(x, y, width, height);
        // Rotation information
        double rotationRequired = pose.getTheta();
        BufferedImage image = AssetManager.getInstance().getImage("Towers", imageLink);
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform backup = g2d.getTransform();
        BufferedImage srcImg = image.createGraphics().getDeviceConfiguration().createCompatibleImage(width, height, Transparency.TRANSLUCENT);
        AffineTransform trans = new AffineTransform();
        trans.rotate(rotationRequired, (x + (width / 2)), (y + (height / 2))); // the points to rotate around (the center in my example, your left side for your problem)
        g2d.transform(trans);

        g2d.drawImage(srcImg, x, y, width, height, null);  // the actual location of the sprite
        g2d.setTransform(backup); // restore previous transform
        //g.drawImage(AssetManager.getInstance().getImage("Towers",imageLink), towerBox.x, towerBox.y, towerBox.width, towerBox.height, null);
    }*/

    protected void drawHealthBar(Graphics g, Rectangle hitbox) {
        if (maxHealth > health) {
            int height = hitbox.height / 10;
            g.setColor(Color.white);
            g.drawRect(hitbox.x, hitbox.y - height, hitbox.width, height);
            g.setColor(Color.red);
            g.drawRect(hitbox.x, hitbox.y - height, (int) ((health / maxHealth) * hitbox.width), height);
        }
    }

    protected JSONObject getJsonObject() {
        JSONObject j = new JSONObject();
        j.put("X", pose.getX());
        j.put("Y", pose.getY());
        j.put("Theta", pose.getTheta());
        j.put("Name", tempalate.getName());
        j.put("Type", tempalate.getType());
        return j;
    }

    protected String getCostResource() {
        return costResource;
    }

    protected int getCostQuantity() {
        return costQuantity;
    }

    public double getHealth() {
        return health;
    }

    public TowerTemplate getTempalate() {
        return tempalate;
    }

    private Double getDirectionOfInput(int x, int y) {
        double atan2_x = (x) - pose.getX();
        double atan2_y = (y) - pose.getY();
        double rot1 = Math.atan2(atan2_y, atan2_x);
        return rot1 + (Math.PI / 2);
    }

    public boolean addToInventory(int x, int y, Resource resource) {
        double directionOfInput = getDirectionOfInput(x, y);
        boolean inputCheck = false;
        if (!inputDirections.isEmpty()) {
            for (Double direction : inputDirections) {
                if (direction.equals(directionOfInput)) {
                    inputCheck = true;
                    break;
                }
            }
        }
        if (inputCheck) {
            boolean check = true;
            for (Resource resource1 : inventory) {
                if (resource1.getId().equals(resource.getId())) {
                    check = false;
                }
            }
            if (check && (inventory.size() < inventorySize)) {
                inventory.add(resource);

                return true;
            }

        }
        return false;
    }

    protected boolean outputResource(Resource resource) {
        Map mapInstance = GameState.getInstance().getMapInstance();
        BaseTower currentTower;
        boolean existanceCheck = false;
        Integer outputIndex = null;
        int index = 0;
        for (Resource resource1 : inventory) {
            if (resource1.getId().equalsIgnoreCase(resource.getId())) {
                existanceCheck = true;
                outputIndex = index;
            }
            index += 1;
        }
        //todo change this to start at a different direction each time it outputs a resource
        //todo make this applicable for all output types
        if (existanceCheck) {
            currentTower = mapInstance.getMapSection(new Rectangle2D.Double(pose.getX(), pose.getY() - 1, 0.5, 0.5)).getFirst().getTower();
            if (currentTower != null) {
                resource.setPose(new Pose(pose.getX() + (0.5 - resource.getWidth() / 2), pose.getY() - 0.0000001, 0));
                if (nextDirection == 0 && currentTower.addToInventory((int) pose.getX(), (int) pose.getY(), resource)) {
                    nextDirection += Math.PI / 2;
                    while (nextDirection >= (Math.PI * 2) - 0.1) {
                        nextDirection -= Math.PI * 2;
                    }
                    removeListOfIndexes(outputIndex);
                    return true;
                }
            }
            currentTower = mapInstance.getMapSection(new Rectangle2D.Double(pose.getX() + 1, pose.getY(), 0.5, 0.5)).getFirst().getTower();
            if (currentTower != null) {
                resource.setPose(new Pose(pose.getX() + 1.0000001, pose.getY() + (0.5 - resource.getHeight() / 2), Math.PI / 2));
                if (nextDirection == Math.PI / 2 && currentTower.addToInventory((int) pose.getX(), (int) pose.getY(), resource)) {
                    nextDirection += Math.PI / 2;
                    while (nextDirection >= (Math.PI * 2) - 0.1) {
                        nextDirection -= Math.PI * 2;
                    }
                    removeListOfIndexes(outputIndex);
                    return true;
                }
            }
            currentTower = mapInstance.getMapSection(new Rectangle2D.Double(pose.getX(), pose.getY() + 1, 0.5, 0.5)).getFirst().getTower();
            if (currentTower != null) {
                resource.setPose(new Pose(pose.getX() + (0.5 - resource.getWidth() / 2), pose.getY() + 1.0000001, Math.PI));
                if (nextDirection == Math.PI && currentTower.addToInventory((int) pose.getX(), (int) pose.getY(), resource)) {
                    nextDirection += Math.PI / 2;
                    while (nextDirection >= (Math.PI * 2) - 0.1) {
                        nextDirection -= Math.PI * 2;
                    }
                    removeListOfIndexes(outputIndex);
                    return true;
                }
            }
            currentTower = mapInstance.getMapSection(new Rectangle2D.Double(pose.getX() - 1, pose.getY(), 0.5, 0.5)).getFirst().getTower();
            if (currentTower != null) {
                resource.setPose(new Pose(pose.getX() - 0.0000001, pose.getY() + (0.5 - resource.getHeight() / 2), Math.PI * 1.5));
                if (nextDirection == Math.PI * 1.5 && currentTower.addToInventory((int) pose.getX(), (int) pose.getY(), resource)) {
                    nextDirection += Math.PI / 2;
                    while (nextDirection >= (Math.PI * 2) - 0.1) {
                        nextDirection -= Math.PI * 2;
                    }
                    removeListOfIndexes(outputIndex);
                    return true;
                }
            }
            nextDirection += Math.PI / 2;
            while (nextDirection >= (Math.PI * 2) - 0.1) {
                nextDirection -= Math.PI * 2;
            }
        }
        return false;
    }

    protected void removeListOfIndexes(ArrayList<Integer> indexesToRemove) {
        for (int toRemoveIndex = indexesToRemove.size() - 1; toRemoveIndex >= 0; toRemoveIndex--) {
            for (int index = inventory.size() - 1; index >= 0; index--) {
                if (indexesToRemove.get(toRemoveIndex) == index) {

                    inventory.remove(index);
                    break;
                }
            }
        }
    }

    protected void removeListOfIndexes(Integer indexesToRemove) {
        ArrayList<Integer> index = new ArrayList<>();
        index.add(indexesToRemove);
        removeListOfIndexes(index);
    }

}

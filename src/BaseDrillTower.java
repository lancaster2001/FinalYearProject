import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class BaseDrillTower extends BaseTower {
    private double actAccumulator = 0.0;
    private final double actAccumulatorLimit = 1.0;
    private String spinnerLink;
    private String tileResource = null;

    public BaseDrillTower(Pose pose, TowerTemplate template,String tileResource) {
        super(pose, template);
        imageLink = template.getImageLink();
        spinnerLink = template.getSpinnerLink();
        inventorySize = 5;
        for (double index = 0; index < 4; index += 1.0) {
            outputDirections.add(index * (Math.PI / 2));
        }
        pose.setTheta(0);
        this.tileResource = tileResource;
    }
    public void draw(Graphics g, int x, int y, int slotWidth, int slotHeight, AssetManager assetManagerInstance) {
        super.draw(g,x,y,slotWidth,slotHeight,assetManagerInstance);
        if(tileResource!=null) {
            Color resourceColour = ResourceManager.getInstance().getResource(tileResource).getColor();
            BufferedImage spinner = changeImageColor(assetManagerInstance.getImage("Towers", spinnerLink), resourceColour);
            g.drawImage(spinner, x, y, slotWidth, slotHeight, null);
        }
    }
    public void act(BaseTile tile, ResourceManager resourceManagerInstance) {
        resourceManagerInstance.queryInventory(tile.getResource()).add();
    }

    public void tick(BaseTile tile, double tickMultiplier) {
        actAccumulator += tickMultiplier / speed;
        if (actAccumulator >= actAccumulatorLimit) {
            actAccumulator -= actAccumulatorLimit;
            Resource resource = ResourceManager.getInstance().getResource(tile.getResource());
            resource.add();
            if (inventory.size() < inventorySize) {
                inventory.add(resource);
            }
        }
        if (!inventory.isEmpty()) {
            boolean conveyerCheck = false;
            Map mapInstance = GameState.getInstance().getMapInstance();
            MapSlot slotToCheck = null;
            if (pose.getY() - 1 > 0) {
                slotToCheck = mapInstance.getMapSection(new Rectangle2D.Double(pose.getX(), pose.getY() - 1, 0.5, 0.5)).getFirst();
            }
            if (slotToCheck != null) {
                if(outputResource(inventory.getFirst())){

                }else{
                    if (pose.getX() + 1 <= GameState.getInstance().getMapInstance().getMapWidth()) {
                        slotToCheck = mapInstance.getMapSection(new Rectangle2D.Double(pose.getX() + 1, pose.getY(), 0.5, 0.5)).getFirst();
                    }
                    if(outputResource(inventory.getFirst())){

                    }else{
                        if (pose.getY() + 1 <= GameState.getInstance().getMapInstance().getMapHeight()) {
                            slotToCheck = mapInstance.getMapSection(new Rectangle2D.Double(pose.getX(), pose.getY() + 1, 0.5, 0.5)).getFirst();
                        }
                        if(outputResource(inventory.getFirst())){

                        }else{
                            if (pose.getX() - 1 > 0) {
                                slotToCheck = mapInstance.getMapSection(new Rectangle2D.Double(pose.getX() - 1, pose.getY(), 0.5, 0.5)).getFirst();
                            }
                        }
                    }
                }
            }
        }
    }
    private static BufferedImage changeImageColor(BufferedImage image, Color color) {
        // Create a new BufferedImage with the same dimensions and type as the original image
        BufferedImage modifiedImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());

        // Copy the pixels of the original image to the modified image while changing the color
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                int rgb = image.getRGB(x, y);
                if ((rgb & 0x00FFFFFF) != 0) { // Check if the pixel is not transparent
                    modifiedImage.setRGB(x, y, color.getRGB()); // Set the color of the pixel in the modified image
                } else {
                    modifiedImage.setRGB(x, y, rgb); // Copy transparent pixels as is
                }
            }
        }
        return modifiedImage;
    }
}

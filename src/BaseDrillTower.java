import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;

/*this class is used for constructing drill towers*/
public final class BaseDrillTower extends BaseTower {
    private double actAccumulator = 0.0;//accumulator for drilling
    private final double actAccumulatorLimit = 1.0;//how long it takes to drill a resource
    private String spinnerLink;// link for the image of the spinner on top of the drill (note: this does not spin due to concerns over performance)
    private String tileResource = null;//resource that the drill is mining

    //constructor
    public BaseDrillTower(Pose pose, TowerTemplate template,String tileResource) {
        super(pose, template);
        imageLink = template.getImageLink();
        spinnerLink = template.getSpinnerLink();
        inventorySize = 5;
        //makes all side output directions
        for (double index = 0; index < 4; index += 1.0) {
            outputDirections.add(index * (Math.PI / 2));
        }
        pose.setTheta(0);
        this.tileResource = tileResource;
    }
    //display drill
    public void draw(Graphics g, Rectangle rectangle, AssetManager assetManagerInstance) {
        super.draw(g,rectangle,assetManagerInstance);
        if(tileResource!=null) {
            Color resourceColour = ResourceManager.getInstance().getResource(tileResource).getColor();
            BufferedImage spinner = changeImageColor(assetManagerInstance.getImage("Towers", spinnerLink), resourceColour);
            g.drawImage(spinner,rectangle.x, rectangle.y, rectangle.width, rectangle.height, null);
        }
    }

    //method for what should happen each tick
    public void tick(BaseTile tile, double tickMultiplier) {
        super.tick(tickMultiplier);
        //add to cooldown
        actAccumulator += tickMultiplier / speed;
        //check if cooldown is over
        if (actAccumulator >= actAccumulatorLimit) {
            //reset cooldown
            actAccumulator -= actAccumulatorLimit;
            //generate resource
            Resource resource = ResourceManager.getInstance().getResource(tile.getResource());
            resource.add();
            //check inventory is not full and added resource to it if so
            if (inventory.size() < inventorySize) {
                inventory.add(resource);
            }
        }
        //attempt to output any items in inventory
        if (!inventory.isEmpty()) {
            outputResource(inventory.getFirst());
        }
    }
    private static BufferedImage changeImageColor(BufferedImage image, Color color) {
        // Create a copy of the buffered image for the resource colour
        BufferedImage modifiedImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());

        // copy each pixel of the original image and set it to the new image and change the colour of any non-transparent pixels
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                int rgb = image.getRGB(x, y);//get original pixel
                if ((rgb & 0x00FFFFFF) != 0) { // check for transparency
                    modifiedImage.setRGB(x, y, color.getRGB()); // copy coloured pixel and set it colour to the provided colour
                } else {
                    modifiedImage.setRGB(x, y, rgb); // Copy transparent pixel
                }
            }
        }
        //return change image
        return modifiedImage;
    }
}

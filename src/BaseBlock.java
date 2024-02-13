import java.awt.*;

public class BaseBlock extends BaseTower {

    BaseBlock(Pose pose, String imageLink) {
        super(pose, new TowerTemplate("Block", 1, 1, "rock", 0, imageLink, "Block", 0, "", 0));

    }

    public void tick(BaseTile tile, double tickMultiplier) {
    }

    public void tick(double tickMultiplier) {
    }

    public void takeDamage(double damage) {
    }

    protected void draw(Graphics g, int x, int y, int slotWidth, int slotHeight, AssetManager assetManagerInstance) {
        int width = (int) (this.width * slotWidth);
        int height = (int) (this.height * slotHeight);
        Rectangle towerBox = new Rectangle(x, y, width, height);
        g.drawImage(AssetManager.getInstance().getImage("Blocks", imageLink), towerBox.x, towerBox.y, towerBox.width, towerBox.height, null);
    }
}

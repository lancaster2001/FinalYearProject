import org.json.JSONObject;

import java.awt.*;

public final class BaseTile {
    private String imageLink;
    private String resource;

    public BaseTile(TileTemplate template) {
        imageLink = template.getImageLink();
        resource = template.getResource();
    }

    public void draw(Graphics g, Rectangle rectangle, AssetManager assetManagerInstance) {
        int x = rectangle.x;
        int y = rectangle.y;
        int width = rectangle.width;
        int height = rectangle.height;
        g.drawImage(assetManagerInstance.getImage("Tiles", imageLink), x, y, width, height, null);
        if (!resource.equalsIgnoreCase("Rock")) {
            g.drawImage(assetManagerInstance.getImage("Tiles", "ore-" + resource + ".png"), x, y, width, height, null);
        }
    }

    public JSONObject getJsonObject() {
        JSONObject j = new JSONObject();
        j.put("ImageLink", imageLink.replace(".png", ""));
        j.put("Resource", resource);
        return j;
    }

    public String getImageLink() {
        return imageLink;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }
}
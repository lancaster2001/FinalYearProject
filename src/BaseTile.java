import java.awt.*;

public class BaseTile {
    protected String imageLink;
    protected String resource;
    public BaseTile(TileTemplate template){
        imageLink = template.getImageLink();
        resource = template.getResource();
    }
    protected void draw(Graphics g, int x, int y, int width , int height,AssetManager assetManagerInstance){
        g.drawImage(assetManagerInstance.getImage("Tiles",imageLink), x, y, width, height, null);
        if(!resource.equalsIgnoreCase("Rock")) {
            g.drawImage(assetManagerInstance.getImage("Tiles", "ore-"+resource+".png"), x, y, width, height, null);
        }
    }
    public String getImageLink(){
        return imageLink;
    }

    public String getResource() {
        return resource;
    }
}
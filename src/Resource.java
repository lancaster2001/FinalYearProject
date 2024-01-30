import java.awt.image.BufferedImage;

public class Resource {
    private int quantity = 0;
    private String name;
    private String imageLink = "src/Resources/";
    private BufferedImage image;

    public Resource(String name){
        this.name = name;
        imageLink+=name+"/image.png";
        image = AssetManager.getInstance().getImage(imageLink);
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

    public BufferedImage getImage() {
        return image;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getName() {
        return name;
    }
}

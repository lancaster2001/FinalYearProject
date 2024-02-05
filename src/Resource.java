import java.awt.image.BufferedImage;

public class Resource {
    private int quantity = 0;
    private String name;
    private String tileImageLink;
    private String iconImageLink;

    public Resource(String name,String tileImageLink,String iconImageLink){
        this.name = name;
        this.tileImageLink = tileImageLink;
        this.iconImageLink = iconImageLink;
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
}

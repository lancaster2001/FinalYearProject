import java.util.ArrayList;
import java.util.Random;

public class TileTemplate {
    private String name;
    private ArrayList<String> imageLinks = new ArrayList<>();
    private String resource;
    TileTemplate(){}
    TileTemplate(String name,String resource, String imageLinks){
        this.name = name;
        this.resource = resource;
        this.imageLinks.add(imageLinks);
    }
    TileTemplate(String name,String resource, ArrayList<String> imageLinks){
        this.name = name;
        this.resource = resource;
        this.imageLinks = imageLinks;
    }

    public String getImageLink() {
        Random rnd = new Random();
        if(imageLinks.size()>1) {
            return imageLinks.get(rnd.nextInt(0, imageLinks.size() - 1));
        }else{
            return imageLinks.get(0);
        }
    }
    public String getImageLink(String imageLink) {
        for(String currentImageLink: imageLinks) {
            if(currentImageLink.equalsIgnoreCase(imageLink)){
                return currentImageLink;
            }
        }
        return "error.png";
    }

    public String getResource() {
        return resource;
    }

    public String getName() {
        return name;
    }
}

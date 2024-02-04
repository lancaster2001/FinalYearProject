public class TileTemplate {
    private String name;
    private String imageLink;
    private String resource;
    TileTemplate(){}
    TileTemplate(String name,String resource, String imageLink){
        this.name = name;
        this.resource = resource;
        this.imageLink = imageLink;
    }

    public String getImageLink() {
        return imageLink;
    }

    public String getResource() {
        return resource;
    }

    public String getName() {
        return name;
    }
}

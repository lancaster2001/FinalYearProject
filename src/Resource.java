public class Resource {
    String name;
    String link = "src/Resources/";

    public void resource(String name){
        this.name = name;
        link+=name;

    }

}

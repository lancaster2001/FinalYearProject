public class TowerTemplate {
    private String name;
    private double maxHealth;
    private String type;
    private String costResource;
    private int costQuantity;
    private String imageLink;
    private double width;
    private double height;
    private double range;
    private double speed;
    private double cooldown;
    private BulletTemplate bulletTemplate;
    private String BuildMenuList;

    public TowerTemplate(){}
    public TowerTemplate(String name,double width, double height, String costResource, int costQuantity, String imageLink,String type, double maxHealth,String BuildMenuList,double speed){
        this.name = name;
        this.costQuantity = costQuantity;
        this.costResource = costResource;
        this.imageLink = imageLink;
        this.width = width;
        this.height = height;
        this.type = type;
        this.maxHealth = maxHealth;
        this.BuildMenuList = BuildMenuList;
    }
    public void setupTurret(double range, BulletTemplate bulletTemplate){
        this.range = range;
        this.bulletTemplate = bulletTemplate;
    }
    public void setupDrill(double speed){
        this.speed = speed;
    }
    public void setupConveyer(double speed){
        this.speed = speed;
    }

    public String getName() {
        return name;
    }

    public int getCostQuantity() {
        return costQuantity;
    }

    public String getCostResource() {
        return costResource;
    }

    public String getImageLink() {
        return imageLink;
    }

    public double getHeight() {
        return height;
    }

    public double getWidth() {
        return width;
    }

    public BulletTemplate getBulletTemplate() {
        return bulletTemplate;
    }

    public double getCooldown() {
        return speed;
    }

    public double getRange() {
        return range;
    }

    public String getType() {
        return type;
    }

    public double getSpeed() {
        return speed;
    }

    public double getMaxHealth() {
        return maxHealth;
    }
    public void setImageLink(String imageLink){
        this.imageLink = imageLink;
    }

    public String getBuildMenuList() {
        return BuildMenuList;
    }
    public void setName(String name){
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }
}

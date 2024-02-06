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

    public TowerTemplate(){}
    public TowerTemplate(String name,double width, double height, String costResource, int costQuantity, String imageLink,String type, double maxHealth){
        this.name = name;
        this.costQuantity = costQuantity;
        this.costResource = costResource;
        this.imageLink = imageLink;
        this.width = width;
        this.height = height;
        this.type = type;
        this.maxHealth = maxHealth;
    }
    public void setupTurret(double range, double cooldown, BulletTemplate bulletTemplate){
        this.range = range;
        this.cooldown = cooldown;
        this.bulletTemplate = bulletTemplate;
    }
    public void setupDrill(double speed){
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
        return cooldown;
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

}

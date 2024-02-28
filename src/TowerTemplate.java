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
    private BulletTemplate bulletTemplate;
    private String BuildMenuList;
    private String BulletCostResource;
    private int BulletCostQuantity;
    private String spinnerLink;
    private int magSize;

    public TowerTemplate() {
    }

    public TowerTemplate(String name, double width, double height, String costResource, int costQuantity, String imageLink, String type, double maxHealth, String BuildMenuList, double speed) {
        this.name = name;
        this.costQuantity = costQuantity;
        this.costResource = costResource;
        this.imageLink = imageLink;
        this.width = width;
        this.height = height;
        this.type = type;
        this.maxHealth = maxHealth;
        this.BuildMenuList = BuildMenuList;
        this.speed = speed;
    }

    public void setupTurret(double range, BulletTemplate bulletTemplate, String BulletCostResource, int bulletCostQuantity,int magSize) {
        this.range = range;
        this.bulletTemplate = bulletTemplate;
        this.BulletCostResource = BulletCostResource;
        this.BulletCostQuantity = bulletCostQuantity;
        this.magSize = magSize;
    }
    public void setupDrill(String spinnerLink){
        this.spinnerLink = spinnerLink;
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

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getBuildMenuList() {
        return BuildMenuList;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getBulletCostQuantity() {
        return BulletCostQuantity;
    }

    public String getBulletCostResource() {
        return BulletCostResource;
    }

    public void setCostQuantity(int costQuantity) {
        this.costQuantity = costQuantity;
    }

    public String getSpinnerLink() {
        return spinnerLink;
    }

    public int getMagSize() {
        return magSize;
    }
}

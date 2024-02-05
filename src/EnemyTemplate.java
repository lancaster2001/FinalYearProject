public class EnemyTemplate {
    private String name;
    private double width;//relative to size of slot
    private double height;//relative to size of slot
    private double damage;
    private double maxHealth;
    private double moveSpeed;
    private String imageLink;

    EnemyTemplate(String name,double width, double height, double damage, double maxHealth, double moveSpeed, String imageLink){
        this.name = name;
        this.width = width;
        this.height = height;
        this.damage = damage;
        this.maxHealth = maxHealth;
        this.moveSpeed = moveSpeed;
        this.imageLink = imageLink;
    }

    public String getName() {
        return name;
    }

    public String getImageLink() {
        return imageLink;
    }

    public double getDamage() {
        return damage;
    }

    public double getMoveSpeed() {
        return moveSpeed;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public double getMaxHealth() {
        return maxHealth;
    }
}

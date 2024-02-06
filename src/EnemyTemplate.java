public class EnemyTemplate {
    private String name;
    private double width;//relative to size of slot
    private double height;//relative to size of slot
    private double damage;
    private double maxHealth;
    private double moveSpeed;
    private String imageLink;
    private double range;
    private double cooldown;
    private BulletTemplate bullet;


    EnemyTemplate(String name,double width, double height, double damage, double maxHealth, double moveSpeed, String imageLink,double range, double cooldown, BulletTemplate bullet){
        this.name = name;
        this.width = width;
        this.height = height;
        this.damage = damage;
        this.maxHealth = maxHealth;
        this.moveSpeed = moveSpeed;
        this.imageLink = imageLink;
        this.range = range;
        this.cooldown = cooldown;
        this.bullet = bullet;
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

    public double getRange() {
        return range;
    }

    public double getCooldown() {
        return cooldown;
    }

    public BulletTemplate getBullet() {
        return bullet;
    }
}

public final class BulletTemplate {
    private double moveSpeed = 3.0;
    private double damage = 1.0;
    private double width = 0.6;
    private double height = 0.6;
    private String imageLink = "src/Bullets/base.png";
    BulletTemplate(){}
    BulletTemplate(double moveSpeed, double damage, double width, double height, String imageLink) {
        //todo: make sure the data is clean
        this.moveSpeed = moveSpeed;
        this.damage = damage;
        this.width = width;
        this.height = height;
        this.imageLink = imageLink;
    }

    public double getHeight() {
        return height;
    }

    public String getImageLink() {
        return imageLink;
    }

    public double getWidth() {
        return width;
    }

    public double getMoveSpeed() {
        return moveSpeed;
    }

    public double getDamage() {
        return damage;
    }
}

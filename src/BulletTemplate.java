public class BulletTemplate {
    double moveSpeed = 3.0;
    double damage = 1.0;
    double width = 1.0;
    double height = 1.0;
    String imageLink = "src/Bullets/base.png";

    BulletTemplate() {
    }

    BulletTemplate(double moveSpeed, double damage, double width, double height, String imageLink) {
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

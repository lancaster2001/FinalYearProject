public class Bullet {
    Pose pose;
    double moveSpeed;
    double damage;
    String imageLink = "src/Towers/Tower1/image.png";
    Bullet(double x, double y, double theta, double moveSpeed, double damage) {
        pose = new Pose(x,y,theta);
        this.moveSpeed = moveSpeed;
        this.damage = damage;
    }

    public String getImageLink() {
        return imageLink;
    }
}

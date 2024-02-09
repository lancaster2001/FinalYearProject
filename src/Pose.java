public final class Pose {
    private double x;
    private double y;
    private double theta;
    //constructors-----------------------------------------------------------------------------

    public Pose() {
        this(0.0, 0.0, 0.0);
    }

    public Pose(double xpos, double ypos, double theta) {
        this.x = xpos;
        this.y = ypos;
        this.setTheta(theta);
    }

    //getters & setters------------------------------------------------------------------------
    //setters-----------------------------------------------------------------------------------
    public void setTheta(double theta) {
        // Ensure that theta is in the range -\pi..\pi
        if (theta > Math.PI) {
            this.theta = theta - (2 * Math.PI);
        } else if (theta < -Math.PI) {
            this.theta = (2 * Math.PI) + theta;
        } else {
            this.theta = theta;
        }
    }

    public void setPosition(double x, double y, double theta) {
        this.x = x;
        this.y = y;
        this.setTheta(theta);
    }

    public void setPosition(Pose p) {
        this.setPosition(p.getX(), p.getY(), p.getTheta());
    }
    public void setX(double x){
        this.x = x;
    }
    public void setY(double y){
        this.y = y;
    }

    //getters
    public double getX() {
        return x;
    }


    public double getY() {
        return y;
    }

    public double getTheta() {
        return theta;
    }

    // Find the difference in radians between some heading and the current pose
    public double getDeltaTheta(double theta) {
        double d = theta - this.theta;
        if (d > Math.PI)
            d = -(2 * Math.PI) + d;
        else if (d < -Math.PI)
            d = (2 * Math.PI) + d;
        return d;
    }
}


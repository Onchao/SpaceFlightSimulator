package utility;

public class Force {
    private double x;
    private double y;
    private double Fx;
    private double Fy;

    public Force (double x, double y, double Fx, double Fy) {
        this.x = x;
        this.y = y;
        this.Fx = Fx;
        this.Fy = Fy;
    }

    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    public double getFx() {
        return Fx;
    }
    public double getFy() {
        return Fy;
    }
}

package utility;

public class Point {
    private double x;
    private double y;

    public Point (double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Point (PolarPoint p) {
        x = p.getR() * Math.cos(p.getPhi());
        y = p.getR() * Math.sin(p.getPhi());
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}

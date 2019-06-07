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

    public Point rotate(double angle) {
        PolarPoint pp = new PolarPoint(this);
        pp.setPhi(pp.getPhi() + angle);
        return new Point(pp);
    }
}

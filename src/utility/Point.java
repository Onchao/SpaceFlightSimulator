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

    public double distFrom (Point other) {
        double dx = x - other.x;
        double dy = y - other.y;

        return Math.sqrt(dx*dx + dy*dy);
    }

    public double getModule(){
        return Math.sqrt(x*x+y*y);
    }

    //rad
    public double getAngle(){
        return Math.atan2(y,x);
    }

    public String toString () {
       return "( " + x + "  " + y + " )";
    }
}

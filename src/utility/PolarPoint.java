package utility;

public class PolarPoint {
    private double r;
    private double phi;

    public PolarPoint(double r, double phi) {
        this.r = r;
        this.phi = phi;
    }

    public PolarPoint(Point p) {
        r = Math.sqrt(p.getX()*p.getX() + p.getY()*p.getY());
        phi = Math.atan2(p.getY(), p.getX());
    }

    public double getR() {
        return r;
    }

    public void setR(double r) {
        this.r = r;
    }

    public double getPhi() {
        return phi;
    }

    public void setPhi(double phi) {
        this.phi = phi;
    }
}

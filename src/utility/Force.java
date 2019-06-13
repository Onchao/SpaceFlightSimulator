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

    public void moveOrigin(double mx, double my){
        x+=mx;
        y+=my;
    }

    public double getPointDist(){
        return Math.sqrt(x*x+y*y);
    }
    public double getVectorLength(){
        return Math.sqrt(Fx*Fx+Fy*Fy);
    }
    public double getPointAngle(){
        return Math.toDegrees(Math.atan2(y,x));
    }
    public double getVectorAngle(){
        return Math.toDegrees(Math.atan2(Fy,Fx));
    }

    public String toString () {
        return "(" + Fx + ", " + Fy + ")";
    }
}

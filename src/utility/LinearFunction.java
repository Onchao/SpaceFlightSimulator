package utility;

public class LinearFunction {
    // y=Ax+B
    private double A;
    private double B;

    // passing through 2 points
    public LinearFunction(Point P, Point Q){
        A = (Q.getY() - P.getY())/(Q.getX() - P.getX());
        B = P.getY() - A*P.getX();
    }

    // passing through 1 point with specified parameter
    public LinearFunction(Point P, double dx, double dy){
        A = dy/dx;
        B = P.getY() - A*P.getX();
    }

    // passing through 1 point with specified angle
    public LinearFunction(Point P, double angle){
        A = Math.tan(angle);
        B = P.getY() - A*P.getX();
    }

    public double getYforX(double x){
        return A*x + B;
    }

    public void print(){
        System.out.println("y = " + A + "x + " + B);
    }

    public double getXfromRadius(double r){
        return Math.abs(r * Math.cos(Math.atan(A)));
    }

    public Point getPointInRadius(double r, boolean positive){
        double tmp = getXfromRadius(r);
        if(!positive){
            tmp*=-1;
        }
        return new Point(tmp, A*tmp);
    }
}

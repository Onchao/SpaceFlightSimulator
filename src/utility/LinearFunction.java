package utility;

public class LinearFunction {
    // Ax + By + C = 0
    private double A;
    private double B;
    private double C;

    LinearFunction(Point P, Point Q){
        A = P.getX() - Q.getX();
        B = P.getY() - Q.getY();
        C = A * P.getX() + B * Q.getY();
    }


}

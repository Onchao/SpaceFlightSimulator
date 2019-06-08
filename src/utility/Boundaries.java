package utility;

public class Boundaries {
    private double upper;
    private double lower;
    private double left;
    private double right;

    public Boundaries (double upper, double lower, double left, double right) {
        this.upper = upper;
        this.lower = lower;
        this.left = left;
        this.right = right;
    }

    public double getUpper() {
        return upper;
    }

    public double getLower() {
        return lower;
    }

    public double getLeft() {
        return left;
    }

    public double getRight() {
        return right;
    }
}

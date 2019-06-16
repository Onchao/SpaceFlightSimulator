package utility;

import ship.Spaceship;

public class AeroComponent {
    private double coefficient;
    private double radius;
    private double area;
    private Point realPos;
    private double rotatedX;
    private double smallerY;
    private double biggerY;
    private Point velocity;
    private double angle;
    private Spaceship spaceship;

    public AeroComponent(double coefficient, double radius, double area, Point realPos, Spaceship spaceship){
        this.coefficient = coefficient;
        this.radius = radius;
        this.area = area;
        this.realPos = realPos;
        this.spaceship = spaceship;

        this.velocity = spaceship.getVelocityTakingWind();
        this.angle = velocity.getAngle();

        PolarPoint tmp1 = new PolarPoint(realPos);
        tmp1.setPhi(tmp1.getPhi() - angle);

        Point tmp2 = new Point(tmp1);
        rotatedX = tmp2.getX();
        biggerY = tmp2.getY() + radius;
        smallerY = tmp2.getY() - radius;
    }

    public double getRotatedX(){
        return rotatedX;
    }

    public double getSmallerY() {
        return smallerY;
    }

    public double getBiggerY() {
        return biggerY;
    }

    public void cut(double bot, double top){
        if(smallerY - 0.0001 < bot && bot < biggerY + 0.0001 ){
            biggerY = bot;
        }
        else  if(smallerY - 0.0001 < top && top < biggerY + 0.0001 ){
            smallerY = top;
        }
    }

    private double getInfluence(){
        return (biggerY - smallerY) / (radius * 2);
    }

    public boolean isAffectedByWind(){
        return biggerY - smallerY > 0.001;
    }

    public Force getForce(){
        double val = spaceship.getParent().getAtmDensity(spaceship.getAltitude())*
                velocity.getModule() * velocity.getModule() * coefficient * area / 2 * getInfluence();
        return new Force(realPos.getX(), realPos.getY(), - Math.cos(angle) * val, - Math.sin(angle) * val);
    }
}

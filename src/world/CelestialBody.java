package world;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import ship.Spaceship;
import utility.Point;

import static java.lang.Double.MAX_VALUE;
import static java.lang.StrictMath.exp;

public class CelestialBody {
    public final String name;
    public final CelestialBody parent;
    public final double orbitalRadius;   // [m]
    public final double orbitalPeriod;   // [s]
    public final double radius;          // [m]
    public final double mass;            // [kg]
    public final double rotationPeriod;  // [s]
    double year1angle;                   // [deg]
    boolean atmExist;
    double A;
    double B;
    public double getAtmDensity(double height){
        return A*exp(B*height);
    }
    public Circle planet;
    public Circle shade;

    public CelestialBody(String name,
                         CelestialBody parent,
                         double orbitalRadius,
                         double orbitalPeriod,
                         double radius,
                         double mass,
                         double rotationPeriod,
                         double year1angle,
                         boolean atmExist,
                         double A,
                         double B,
                         Color color
                         ){
        this.name = name;
        this.parent = parent;
        this.orbitalRadius = orbitalRadius * 1000;
        this.orbitalPeriod = orbitalPeriod * 24 * 3600;
        this.radius = radius * 1000;
        this.mass = mass;
        this.rotationPeriod = rotationPeriod * 24 * 3600;
        this.year1angle = year1angle;

        this.atmExist = atmExist;
        this.A = A;
        this.B = B;

        planet = new Circle(0,0,0, color);
        Color colorOpacity = new Color((float)color.getRed(), (float)color.getGreen(), (float)color.getBlue(), 0.2);

        shade = new Circle(0,0,0, colorOpacity);

        shade.setStroke(color);
        shade.setStrokeWidth(0.5);
    }

    public CelestialBody(String name,
                         CelestialBody parent,
                         double orbitalRadius,
                         double orbitalPeriod,
                         double radius,
                         double mass,
                         double rotationPeriod,
                         double year1angle,
                         Color color
    ){
        this(name, parent, orbitalRadius, orbitalPeriod, radius, mass, rotationPeriod, year1angle, false, 0, 0, color);
    }


    public Point getRelPos(){
        if(parent == null)
            return new Point(0,0);
        double orbits = Time.TIME/orbitalPeriod;
        double anglePos = orbits*360 + year1angle; // [deg]
        return new Point(Math.cos(Math.toRadians(anglePos))*orbitalRadius, Math.sin(Math.toRadians(anglePos))*orbitalRadius);
    }

    public Point getAbsPos(){
        if(parent == null)
            return new Point(0,0);
        Point relative = getRelPos();
        Point parentPos = parent.getAbsPos();
        return new Point(relative.getX() + parentPos.getX(), relative.getY() + parentPos.getY());
    }

    public double getOrbitalAngle(){
        double orbits = Time.TIME/orbitalPeriod;
        return orbits*360 + year1angle; // [deg]
    }

    // relative to planet center
    public double getAngleDif(){
        return 360 * Time.deltaTIME / rotationPeriod;
    }

    // relative to planet center
    public Point getShipPosFromAngle(double angle, double distToBottom){
        return new Point((radius+distToBottom)*Math.cos(Math.toRadians(angle)), radius*Math.sin(Math.toRadians(angle)));
    }

    public double getEscapeRadius(){
        if(parent == null)
            return MAX_VALUE;
        return mass/(6e+24)*1e+9;
    }

    public double getDistanceTo(double x, double y){
        return Math.sqrt((getAbsPos().getX()-x) * (getAbsPos().getX()-x) + (getAbsPos().getY()-y) * (getAbsPos().getY()-y));
    }



    public Point getPlanetVelocity(){
        double velocity = 2*orbitalRadius*Math.PI/(orbitalPeriod);
        double vel_x = velocity * Math.cos(Math.toRadians(getOrbitalAngle() + 90));
        double vel_y = velocity * Math.sin(Math.toRadians(getOrbitalAngle() + 90));
        return new Point(vel_x,vel_y);
    }

    public double getPlanetSurfaceSpeed(){
        return 2*radius*Math.PI/(rotationPeriod);
    }

    // sin <-> cos bcs of reductions
    public Point getPlanetSurfaceVelocity(Spaceship spaceship){
        double angle = Math.atan2(spaceship.gestPos().getY(), spaceship.gestPos().getX());
        double velocity = 2*radius*Math.PI/(rotationPeriod);
        double x = velocity * Math.sin(angle);
        double y = velocity * Math.cos(angle);
        return new Point(x,y);
    }

    public Point getWindVelocity(Spaceship spaceship){
        if(!atmExist){
            return new Point(0,0);
        }
        double dist = getDistanceTo(spaceship.gestPos().getX(), spaceship.gestPos().getY());
        double angle = Math.atan2(spaceship.gestPos().getY(), spaceship.gestPos().getX());
        double velocity = 2*dist*Math.PI/(rotationPeriod);
        double x = velocity * Math.sin(angle);
        double y = velocity * Math.cos(angle);
        return new Point(x,y);
    }
}

/*
https://www.math24.net/barometric-formula/

P(h) = weird thing
p = M*P(h)/R/T

On earth:
P(h) = 101.325 exp(-0.00012*h)[kPa] = 101325 exp(-0.00012*h) N/m^2
M = 0.02896 kg/mol
T = 288.15 K
R = 8.3143 N*m/mol/K

density = kg/m^3 -> OK

wolfram on earth:
1.224816*exp(-0.0012*h)

 */

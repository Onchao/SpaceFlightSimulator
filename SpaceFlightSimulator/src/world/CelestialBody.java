package world;

import javafx.geometry.Pos;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import static java.lang.StrictMath.exp;

public class CelestialBody {
    String name;
    CelestialBody parent;
    double orbitalRadius;   // km
    double orbitalPeriod;   // d
    public double radius;          // km
    double mass;            // kg
    double rotationPeriod;  // kg
    double year1angle;      // deg
    boolean atmExist;
    double A;
    double B;
    double atmDensity(double height){
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
        this.orbitalRadius = orbitalRadius;
        this.orbitalPeriod = orbitalPeriod;
        this.radius = radius;
        this.mass = mass;
        this.rotationPeriod = rotationPeriod;
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


    public Position getRelPos(){
        if(parent == null)
            return new Position(0,0);
        double orbits = Time.TIME/(orbitalPeriod*24*3600);
        double anglePos = orbits*360+year1angle; // deg
        return new Position(Math.cos(Math.toRadians(anglePos))*orbitalRadius, Math.sin(Math.toRadians(anglePos))*orbitalRadius);
    }

    public Position getAbsPos(){
        if(parent == null)
            return new Position(0,0);
        Position relative = getRelPos();
        Position parentPos = parent.getAbsPos();
        return new Position(relative.x + parentPos.x, relative.y + parentPos.y);
    }

    public double getAbsPos_x(){
        if(parent == null)
            return 0;
        Position relative = getRelPos();
        Position parentPos = parent.getAbsPos();
        return relative.x + parentPos.x;
    }

    public double getAbsPos_y(){
        if(parent == null)
            return 0;
        Position relative = getRelPos();
        Position parentPos = parent.getAbsPos();
        return relative.y + parentPos.y;
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
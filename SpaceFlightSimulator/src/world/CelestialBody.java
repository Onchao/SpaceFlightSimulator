package world;

import static java.lang.StrictMath.exp;

public class CelestialBody {
    String name;
    CelestialBody parent;
    double orbitalRadius;   // km
    double orbitalPeriod;   // d
    double radius;          // km
    double mass;            // kg
    double rotationPeriod;  // kg
    boolean atmExist;
    double A;
    double B;
    double atmDensity(double height){
        return A*exp(B*height);
    }

    public CelestialBody(String name,
                         CelestialBody parent,
                         double orbitalRadius,
                         double orbitalPeriod,
                         double radius,
                         double mass,
                         double rotationPeriod){
        this.name = name;
        this.parent = parent;
        this.orbitalRadius = orbitalRadius;
        this.orbitalPeriod = orbitalPeriod;
        this.radius = radius;
        this.mass = mass;
        this.rotationPeriod = rotationPeriod;
        this.atmExist = false;
        this.A = 0;
        this.B = 0;
    }

    public CelestialBody(String name,
                         CelestialBody parent,
                         double orbitalRadius,
                         double orbitalPeriod,
                         double radius,
                         double mass,
                         double rotationPeriod,
                         boolean atmExist,
                         double A,
                         double B ){
        this.name = name;
        this.parent = parent;
        this.orbitalRadius = orbitalRadius;
        this.orbitalPeriod = orbitalPeriod;
        this.radius = radius;
        this.mass = mass;
        this.rotationPeriod = rotationPeriod;
        this.atmExist = atmExist;
        this.A = A;
        this.B = B;
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
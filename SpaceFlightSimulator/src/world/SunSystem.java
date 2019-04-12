package world;

import java.util.ArrayList;


public class SunSystem {
    ArrayList<CelestialBody> bodies = new ArrayList<>();
    CelestialBody sun;
    CelestialBody earth;
    CelestialBody moon;
    double TIME;
    SunSystem(double TIME){
        this.TIME = TIME;

        sun = new CelestialBody("Sun",null,0,0,696342,1.9885e+30,0);
        earth = new CelestialBody("Earth", sun, 149598023, 365.256363004,6371, 5.97237e+24,1, true, 1.224816, -0.0012);
        moon = new CelestialBody("Moon", earth, 384399, 27.321661, 1737.1, 7.342e+22, 27.321661);

        bodies.add(sun);
        bodies.add(earth);
        bodies.add(moon);
    }
}

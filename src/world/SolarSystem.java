package world;

import javafx.scene.paint.Color;
import utility.Const;

import java.util.ArrayList;


public class SolarSystem {
    public ArrayList<CelestialBody> bodies = new ArrayList<>();
    public Const origin;

    CelestialBody sun;
    CelestialBody earth;
    CelestialBody moon;

    CelestialBody mars;

    public SolarSystem(){
        sun = new CelestialBody("Sun",null,0,0,696342.0,1.9885e+30,1, 0, Color.GOLD);
        earth = new CelestialBody("Earth", sun, 149598023.0, 365.256363004,6371, 5.97237e+24,1.0, 107, true, 1.224816, -0.0012, Color.DEEPSKYBLUE);
        moon = new CelestialBody("Moon", earth, 384399.0, 27.321661, 1737.1, 7.342e+22, 27.321661, 163, Color.DIMGRAY);

        mars = new CelestialBody("Mars", sun, 227939200, 686.971, 3389.5, 6.4171e+23, 1.025957, 51, true, 1000, 1000, Color.CORAL);


        bodies.add(sun);
        bodies.add(earth);
        bodies.add(moon);

        bodies.add(mars);
    }

    public Const getOrigin(){
        return origin;
    }
}
/*
mars 51
venus 48
mercury 305
ceres 8
 */
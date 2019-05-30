package world;


import ship.Spaceship;
import utility.Point;

import java.util.ArrayList;

public class Origin {
    public static int originIndex = 0;
    public static int originSize;
    private ArrayList<CelestialBody> bodies;
    private Spaceship ship;
    public static void cycleOrigin(){
        originIndex++;
        originIndex%=(originSize);
    }

    public Origin(ArrayList<CelestialBody> bodies, Spaceship ship){
        this.bodies = bodies;
        this.ship = ship;
        originSize = bodies.size() + 1;
    }

    public Point getOrigin(){
        if(originIndex == 0)
            return ship.getAbsPos();
        return bodies.get(originIndex - 1).getAbsPos();
    }
}
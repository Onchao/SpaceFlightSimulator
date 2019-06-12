package world;


import ship.Spaceship;
import utility.Point;

import java.util.ArrayList;

public class Origin {
    public static int originIndex = 0;
    private static int originSize;
    private static ArrayList<CelestialBody> bodies;
    private static Spaceship ship;
    public static void cycleOrigin(){
        originIndex++;
        originIndex%=(originSize);
    }

    public static void setup(ArrayList<CelestialBody> bodies, Spaceship ship){
        Origin.bodies = bodies;
        Origin.ship = ship;
        originSize = bodies.size() + 1;
    }

    public static Point getOrigin(){
        if(originIndex == 0)
            return ship.getAbsPos();
        return bodies.get(originIndex - 1).getAbsPos();
    }

    public static boolean shipFocused(){
        return originIndex == 0;
    }

    public static double convertAbsX(double x){
        x -= getOrigin().getX();
        x *= Scale.SCALE;
        return x + 400;
    }
    public static double convertAbsY(double y){
        y -= getOrigin().getY();
        y *= Scale.SCALE;
        return - y + 400;
    }
}
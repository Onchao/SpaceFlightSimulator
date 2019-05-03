package world;


import java.util.ArrayList;

public class Origin {
    public static int originIndex = 0;
    public static int originSize;
    private ArrayList<CelestialBody> bodies;
    private Ship ship;
    public static void cycleOrigin(){
        originIndex++;
        originIndex%=(originSize);
    }

    public Origin(ArrayList<CelestialBody> bodies, Ship ship){
        this.bodies = bodies;
        this.ship = ship;
        originSize = bodies.size() + 1;
    }

    public double getOrigin_x(){
        if(originIndex == 0)
            return ship.getAbsPos_x();
        return bodies.get(originIndex - 1).getAbsPos_x();
    }
    public double getOrigin_y(){
        if(originIndex == 0)
            return ship.getAbsPos_y();
        return bodies.get(originIndex - 1).getAbsPos_y();
    }
}
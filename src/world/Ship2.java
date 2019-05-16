package world;

import java.util.LinkedList;

public class Ship2 {
    private CelestialBody parent;
    double vel_x;
    double vel_y;
    private double rel_pos_x;
    private double rel_pos_y;
    public double getAbsPos_x(){
        return parent.getAbsPos_x() + rel_pos_x;
    }
    public double getAbsPos_y(){
        return parent.getAbsPos_y() + rel_pos_y;
    }
    public double angleOnPlanet;

    boolean landed = true;
    //LinkedList<Position> convexHull = new LinkedList<>();

    public Ship2(CelestialBody parent, double angleOnPlanet){
        //convexHull.add(new Position(0,12));
        //convexHull.add(new Position(1, 0));
        //convexHull.add(new Position(-1, 0));

        this.parent = parent;
        this.angleOnPlanet = angleOnPlanet;
        rel_pos_x = parent.getShipPosFromAngle_x(angleOnPlanet);
        rel_pos_y = parent.getShipPosFromAngle_y(angleOnPlanet);
    }
    public CelestialBody getParent(){
        return parent;
    }

    public void update(){
        if(landed){
            double angleDif = parent.getAngleDif();
            angleOnPlanet+=angleDif;
            rel_pos_x = parent.getShipPosFromAngle_x(angleOnPlanet);
            rel_pos_y = parent.getShipPosFromAngle_y(angleOnPlanet);
        }
    }



    void changeParent(){

    }
}
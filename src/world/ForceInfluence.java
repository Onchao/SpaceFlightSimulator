package world;

import ship.Spaceship;
import utility.Const;
import utility.Force;
import utility.Point;
import utility.PolarPoint;

import java.util.*;

import static java.lang.Math.atan2;
import static java.lang.Math.min;

public class ForceInfluence {
    private Spaceship spaceship;
    private SolarSystem solarSystem;

    public ForceInfluence(Spaceship spaceship, SolarSystem solarSystem){
        this.spaceship = spaceship;
        this.solarSystem = solarSystem;
    }


    public Force getCombinedForces(){
        LinkedList<Force> forces = new LinkedList<>();
        //forces.addAll(getPartialAeroForces());
        forces.add(getEngineInfluence());
        //forces.add(getFrictionInfluence());

        double momentum = 0;
        LinkedList<Force> centerForces = new LinkedList<>();
        for(Force f : forces){
            f.moveOrigin(-spaceship.getCenterOfMass().getX(), -spaceship.getCenterOfMass().getY());
            //System.out.println(spaceship.getCenterOfMass().getX());
            //System.out.println(spaceship.getThrustCenter().getY());

            //System.out.println(f.getPointDist());
            //System.out.println(f.getVectorLength());
            //System.out.println(f.getPointAngle());
            //System.out.println(f.getVectorAngle());
            //System.out.println(f.getPointAngle() - f.getVectorAngle());
            //System.out.println();

            momentum += f.getPointDist()*f.getVectorLength()*Math.sin(Math.toRadians(f.getPointAngle() + f.getVectorAngle()));
            //System.out.println(f.getX() + " " + f.getY() + " " + f.getFx() + " " + f.getFx());
            //System.out.println(Math.sin(Math.toRadians(f.getPointAngle() + f.getVectorAngle())));

            double val = f.getPointDist()*f.getVectorLength()*Math.cos(Math.toRadians(f.getPointAngle() + f.getVectorAngle()));
            Force F = new Force(0,0,
                    Math.cos(Math.toDegrees(f.getPointAngle()))*val,
                    Math.sin(Math.toDegrees(f.getPointAngle()))*val);
            centerForces.add(f);
        }
        //System.out.println(momentum);
        //spaceship.setForceMomentum(momentum);

        centerForces.add(getGravityInfluence());
        double totalForceX = 0;
        double totalForceY = 0;
        for (Force f :centerForces){
            totalForceX += f.getFx();
            totalForceY += f.getFy();
        }
        return new Force(0, 0, totalForceX, totalForceY);
    }



    private Force getGravityInfluence(){ // [m/s]
        double Fx = 0;
        double Fy = 0;
        for (CelestialBody B : solarSystem.bodies) {
            double r = B.getDistanceTo(spaceship.getAbsPos().getX(), spaceship.getAbsPos().getY());
            double F = Const.G * B.mass * Time.deltaTIME * spaceship.getTotalMass()/ r/r;
            double angle = Math.toDegrees(Math.atan2(spaceship.getAbsPos().getY() - B.getAbsPos().getY(),
                    spaceship.getAbsPos().getX() - B.getAbsPos().getX()));
            Fx -= F * Math.cos(Math.toRadians(angle));
            Fy -= F * Math.sin(Math.toRadians(angle));
        }
        return new Force(0,0, Fx, Fy);
    }

    private Force getEngineInfluence(){ // [m/s]
        double Fx = Math.cos(Math.toRadians(- spaceship.getRotate().getAngle() + 90)) * spaceship.getTotalThrust() * spaceship.getThrottle()/100;
        double Fy = Math.sin(Math.toRadians(- spaceship.getRotate().getAngle() + 90)) * spaceship.getTotalThrust() * spaceship.getThrottle()/100;
        return new Force(spaceship.getThrustCenter().getX(), spaceship.getThrustCenter().getY(), Fx, Fy);
    }

    private List<Force> getPartialAeroForces() {
        List<Spaceship.ComponentWithCenter> componentCenters = spaceship.getComponentCenters();
        List<Spaceship.ComponentWithCenter> componentCentersRotated = new ArrayList<>();
        double angle = Math.atan2(spaceship.getVel_y(), spaceship.getVel_x());

        for (Spaceship.ComponentWithCenter comp : componentCenters) {
            componentCentersRotated.add(new Spaceship.ComponentWithCenter(comp.getComponent(), comp.getCenter().rotate(-angle)));
        }

        componentCentersRotated.sort((o1, o2) -> {
            if (o1.getCenter().getX() == o2.getCenter().getX()) {
                if (o1.getCenter().getY() < o2.getCenter().getY()) return -1;
                else if (o1.getCenter().getY() > o2.getCenter().getY()) return 1;
                return 0;
            }
            if (o1.getCenter().getX() < o2.getCenter().getX()) return -1;
            else if (o1.getCenter().getX() > o2.getCenter().getX()) return 1;
            return 0;
        });

        class BroomElem {
            double coordinate;
            int type;

            public BroomElem (double coo, int t) {
                coordinate = coo;
                type = t;
            }
        }
        SortedSet<BroomElem> broom = new TreeSet<>((o1, o2) -> {
            if (o1.coordinate == o2.coordinate) return o1.hashCode() < o2.hashCode() ? -1 : 1;
            return o1.coordinate < o2.coordinate ? -1 : 1;
        });

        double vSq = spaceship.getVel_x()*spaceship.getVel_x() + spaceship.getVel_y()*spaceship.getVel_y();

        List<Force> ret = new ArrayList<>();

        for (Spaceship.ComponentWithCenter comp : componentCentersRotated) {
            double cb = comp.getCenter().getY() - comp.getComponent().getFrontAvgRadius();
            double ce = comp.getCenter().getY() + comp.getComponent().getFrontAvgRadius();

            double lastPos = cb;
            int numNested = 0;

            double totalSurface = 0.0;

            for (BroomElem e : broom) {
                if (e.type == 0 && numNested == 0 && e.coordinate > cb) {
                    totalSurface += min (e.coordinate, ce) - min (lastPos, ce);
                }
                if (e.type == 0) ++ numNested;
                else -- numNested;
                lastPos = e.coordinate;
            }

            double pd = spaceship.getParent().getAtmDensity(spaceship.getDistToBottom())*vSq/2;
            double forceVal = comp.getComponent().getDragCoefficient()*pd*totalSurface;
            PolarPoint fvp = new PolarPoint(forceVal, atan2(-spaceship.getVel_y(), -spaceship.getVel_x()));
            Point fvk = new Point(fvp);

            Force d = new Force(comp.getCenter().getX(), comp.getCenter().getY(), fvk.getX(), fvk.getY());
            ret.add(d);

            broom.add(new BroomElem(cb, 0));
            broom.add(new BroomElem(ce, 1));
        }

        return ret;
    }

    //TODO:
    private Force getFrictionInfluence(){
        return new Force(0,0,0,0);
    }
}

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
        for(Force f: getPartialAeroForces()) {
            forces.add(f);
            //System.out.println(f.getFx() + " " + f.getFy());
            System.out.println(f);
        }


        //forces.addAll(getPartialAeroForces());
        forces.add(getEngineInfluence());

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
            double deltaM = - f.getPointDist()*f.getVectorLength()*Math.sin(Math.toRadians(f.getPointAngle() + f.getVectorAngle()));
            //System.out.println(deltaM);
            momentum -= deltaM;

            //System.out.println(f.getX() + " " + f.getY() + " " + f.getFx() + " " + f.getFx());
            //System.out.println(Math.cos(Math.toRadians(f.getPointAngle() + f.getVectorAngle())));

            double val = f.getPointDist()*f.getVectorLength()*Math.cos(Math.toRadians(f.getPointAngle() + f.getVectorAngle()));
            //System.out.println(val);
            //System.out.println(val);

            Force F = new Force(0,0,
                    Math.cos(Math.toRadians(f.getPointAngle()))*val,
                    -Math.sin(Math.toRadians(f.getPointAngle()))*val);
            //System.out.println(F.getFx() + " " + F.getFy());
            centerForces.add(F);
        }
        //System.out.println(momentum);
        spaceship.setForceMomentum(momentum);

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
            double F = Const.G * B.mass * spaceship.getTotalMass()/ r/r;
            double angle = Math.toDegrees(Math.atan2(spaceship.getAbsPos().getY() - B.getAbsPos().getY(),
                    spaceship.getAbsPos().getX() - B.getAbsPos().getX()));
            Fx -= F * Math.cos(Math.toRadians(angle));
            Fy -= F * Math.sin(Math.toRadians(angle));
        }/*
        double r = spaceship.getParent().getDistanceTo(spaceship.getAbsPos().getX(), spaceship.getAbsPos().getY());
        double F = Const.G * spaceship.getParent().mass * spaceship.getTotalMass()/ r/r;
        double angle = Math.toDegrees(Math.atan2(spaceship.getAbsPos().getY() - spaceship.getParent().getAbsPos().getY(),
                spaceship.getAbsPos().getX() - spaceship.getParent().getAbsPos().getX()));
        double Fx = - F * Math.cos(Math.toRadians(angle));
        double Fy = - F * Math.sin(Math.toRadians(angle));
*/

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
        Point vel = spaceship.getVelocityTakingWind();
        double angle = Math.atan2(vel.getY(), vel.getX()) + Math.toRadians(-spaceship.getRotate().getAngle());

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

        System.out.println(componentCentersRotated);

        double vSq = vel.getX()*vel.getX() + vel.getY()*vel.getY();

        List<Force> ret = new ArrayList<>();

        for (Spaceship.ComponentWithCenter comp : componentCentersRotated) {
            double cb = comp.getCenter().getY() - comp.getComponent().getFrontAvgRadius();
            double ce = comp.getCenter().getY() + comp.getComponent().getFrontAvgRadius();

            double lastPos = cb;
            int numNested = 0;

            double totalSurface = 0.0;

            if (broom.isEmpty()) totalSurface = 2*comp.getComponent().getFrontAvgRadius();
            else {
                for (BroomElem e : broom) {
                    if (e.type == 0 && numNested == 0 && e.coordinate > cb) {
                        totalSurface += min(e.coordinate, ce) - min(lastPos, ce);
                    }
                    if (e.type == 0) ++numNested;
                    else --numNested;
                    lastPos = e.coordinate;
                }
            }

            //System.out.println(comp + " " + totalSurface);

            double pd = spaceship.getParent().getAtmDensity(spaceship.getAltitude())*vSq;
            pd /= 2;
            double surface = comp.getComponent().getFrontAvgSurface()*totalSurface/(2*comp.getComponent().getFrontAvgRadius());
            double forceVal = comp.getComponent().getDragCoefficient()*pd*surface;
            PolarPoint fvp = new PolarPoint(forceVal, atan2(-vel.getY(), -vel.getX()));
            Point fvk = new Point(fvp);

            Force d = new Force(comp.getCenter().rotate(angle).getX(), comp.getCenter().rotate(angle).getY(), fvk.getX(), fvk.getY());
            ret.add(d);

            broom.add(new BroomElem(cb, 0));
            broom.add(new BroomElem(ce, 1));
        }

        return ret;
    }
}
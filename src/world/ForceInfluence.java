package world;

import ship.Spaceship;
import utility.*;

import java.lang.reflect.Array;
import java.util.*;

import static java.lang.Math.atan2;
import static java.lang.Math.min;

public class ForceInfluence {
    private Spaceship spaceship;
    private SolarSystem solarSystem;

    public ForceInfluence(Spaceship spaceship, SolarSystem solarSystem) {
        this.spaceship = spaceship;
        this.solarSystem = solarSystem;
    }


    public Force getCombinedForces() {
        LinkedList<Force> forces = new LinkedList<>();

        for (Force f : getPartialAeroForces()) {
            forces.add(f);
            //System.out.println(f.toString());
        }
        forces.add(getEngineInfluence());
        //System.out.println(getEngineInfluence());
        //System.out.println();

        double momentum = 0;
        LinkedList<Force> centerForces = new LinkedList<>();
        for (Force f : forces) {
            double deltaM = f.getPointDist() * f.getVectorLength() * Math.sin(f.getVectorAngle() - f.getPointAngle());
            //System.out.println(deltaM);

            // unreal but fun in game
            momentum -= 10*deltaM;

            double val = f.getVectorLength() * Math.cos(f.getVectorAngle() - f.getPointAngle());

            Force F = new Force(0, 0,
                    Math.cos(f.getPointAngle()) * val,
                    Math.sin(f.getPointAngle()) * val);

            centerForces.add(F);
        }
        //System.out.println(momentum);
        spaceship.setForceMomentum(momentum);

        centerForces.add(getGravityInfluence());
        double totalForceX = 0;
        double totalForceY = 0;
        for (Force f : centerForces) {
            totalForceX += f.getFx();
            totalForceY += f.getFy();
        }
        //System.out.println(totalForceX);
        //System.out.println(totalForceY);
        return new Force(0, 0, totalForceX, totalForceY);
    }

    private Force getGravityInfluence() { // [m/s]
        double Fx = 0;
        double Fy = 0;

        for (CelestialBody B : solarSystem.bodies) {
            double r = B.getDistanceTo(spaceship.getAbsPos().getX(), spaceship.getAbsPos().getY());
            double F = Const.G * B.mass * spaceship.getTotalMass() / r / r;
            double angle = Math.toDegrees(Math.atan2(spaceship.getAbsPos().getY() - B.getAbsPos().getY(),
                    spaceship.getAbsPos().getX() - B.getAbsPos().getX()));
            Fx -= F * Math.cos(Math.toRadians(angle));
            Fy -= F * Math.sin(Math.toRadians(angle));
        }

        return new Force(0, 0, Fx, Fy);
    }

    private Force getEngineInfluence() { // [m/s]
        double Fx = Math.cos(Math.toRadians(-spaceship.getRotate().getAngle() + 90)) * spaceship.getTotalThrust() * spaceship.getThrottle() / 100;
        double Fy = Math.sin(Math.toRadians(-spaceship.getRotate().getAngle() + 90)) * spaceship.getTotalThrust() * spaceship.getThrottle() / 100;

        return new Force(spaceship.getThrustCenter().getX(), spaceship.getThrustCenter().getY(), Fx, Fy);
    }


    private ArrayList<Force> getPartialAeroForces() {
        ArrayList<Force> result = new ArrayList<>();
        ArrayList<AeroComponent> components1 = spaceship.getAeroComponents();
        ArrayList<AeroComponent> components2 = new ArrayList<>();

        components1.sort(new Comparator<AeroComponent>() {
            @Override
            public int compare(AeroComponent a, AeroComponent b) {
                double x = a.getRotatedX();
                double y = b.getRotatedX();
                return Double.compare(x, y);
            }
        });

        while (components1.size() != 0) {
            AeroComponent tmp = components1.get(components1.size() - 1);
            for (int i = 0; i < components1.size() - 1; i++) {
                components1.get(i).cut(tmp.getSmallerY(), tmp.getBiggerY());
                if (components1.get(i).isAffectedByWind()) {
                    components2.add(components1.get(i));
                }
            }
            result.add(tmp.getForce());
            components1 = components2;
            components2 = new ArrayList<>();
        }
        for(AeroComponent a : spaceship.getAeroParachute()) {
            result.add(a.getForce());
        }

        return result;
    }
}
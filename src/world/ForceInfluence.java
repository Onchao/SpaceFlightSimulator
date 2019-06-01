package world;

import ship.Spaceship;
import ship.SpaceshipComponent;
import utility.Const;
import utility.Force;
import utility.Point;

import java.util.List;

public class ForceInfluence {
    Spaceship spaceship;
    SolarSystem solarSystem;

    public ForceInfluence(Spaceship spaceship, SolarSystem solarSystem){
        this.spaceship = spaceship;
        this.solarSystem = solarSystem;
    }


    public Force getCombinedForces(){
        //TODO: add momentum
        Force gravity = getGravityInfluence();
        Force engine = getEngineInfluence();
        Force aero = genAeroInfluence();
        Force combined = new Force(0,0,
                gravity.getFx() + engine.getFx() + aero.getFx(),
                gravity.getFy() + engine.getFy() + aero.getFy()
                );
        return combined;
    }

    private Force getGravityInfluence(){ // [m/s]
        double x = 0;
        double y = 0;
        for (CelestialBody B : solarSystem.bodies) {
            double r = B.getDistanceTo(spaceship.getAbsPos().getX(), spaceship.getAbsPos().getY());
            double F = Const.G * B.mass * Time.deltaTIME * spaceship.getTotalMass()/ r/r;
            double angle = Math.toDegrees(Math.atan2(spaceship.getAbsPos().getY() - B.getAbsPos().getY(),
                    spaceship.getAbsPos().getX() - B.getAbsPos().getX()));
            //if(B.name.equals("Earth")) System.out.println(angle);
            x -= F * Math.cos(Math.toRadians(angle));
            y -= F * Math.sin(Math.toRadians(angle));
        }
        return new Force(0,0, x, y);
    }

    private Force getEngineInfluence(){ // [m/s]
        double Fx = Math.cos(Math.toRadians(- spaceship.getRotate().getAngle() + 90)) * spaceship.getTotalThrust();
        double Fy = Math.sin(Math.toRadians(- spaceship.getRotate().getAngle() + 90)) * spaceship.getTotalThrust();
        return new Force(0, 0, Fx, Fy);
    }

    private  Force genAeroInfluence() {
        double Dx = 0;
        double Dy = 0;
        for (List<SpaceshipComponent> stage : spaceship.getStages()) {
            for (SpaceshipComponent comp : stage) {
                Dx-=(0.5*0.75* spaceship.getParent().getAtmDensity(spaceship.getDistToBottom())*4*Math.PI*spaceship.getVel_x()*spaceship.getVel_x());
                Dy-=(0.5*0.75* spaceship.getParent().getAtmDensity(spaceship.getDistToBottom())*4*Math.PI*spaceship.getVel_y()*spaceship.getVel_y());
            }
        }
        return new Force(0, 0, Dx, Dy);
    }
}

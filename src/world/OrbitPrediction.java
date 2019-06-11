package world;

import ship.Spaceship;
import utility.Const;
import utility.Force;

public class OrbitPrediction {
    private Spaceship spaceship;

    public OrbitPrediction(Spaceship spaceship){
        this.spaceship = spaceship;
    }

    private Force getGravityInfluence(){ // [m/s]
        double r = spaceship.getParent().getDistanceTo(spaceship.getAbsPos().getX(), spaceship.getAbsPos().getY());
        double F = Const.G * spaceship.getParent().mass * Time.deltaTIME * spaceship.getTotalMass()/ r/r;
        double angle = Math.toDegrees(Math.atan2(spaceship.getAbsPos().getY() - spaceship.getParent().getAbsPos().getY(),
                spaceship.getAbsPos().getX() - spaceship.getParent().getAbsPos().getX()));
        double Fx = - F * Math.cos(Math.toRadians(angle));
        double Fy = - F * Math.sin(Math.toRadians(angle));

        return new Force(0,0, Fx, Fy);
    }




}

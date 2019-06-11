package world;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import main_package.Fly;
import ship.Spaceship;
import utility.Const;
import utility.Force;
import utility.LinearFunction;
import utility.Point;

import java.util.LinkedList;

public class OrbitPrediction {
    private Spaceship spaceship;
    private Origin origin;
    private Fly fly;

    Line ship_planetL = new Line(0,0,0,0);

    public OrbitPrediction(Spaceship spaceship, Origin origin, Fly fly){
        this.spaceship = spaceship;
        this.origin = origin;
        this.fly = fly;
        fly.getRoot().getChildren().addAll(ship_planetL);
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

    public void getLines(){
        LinearFunction ship_planet = new LinearFunction(spaceship.getAbsPos(), spaceship.getParent().getAbsPos());
        ship_planetL.setStartX(convertAbsX(spaceship.getAbsPos().getX()));
        ship_planetL.setStartY(convertAbsY(ship_planet.getYforX(spaceship.getAbsPos().getX())));
        ship_planetL.setEndX(convertAbsX(spaceship.getParent().getAbsPos().getX()));
        ship_planetL.setEndY(convertAbsY(ship_planet.getYforX(spaceship.getParent().getAbsPos().getX())));
        ship_planetL.setStroke(Color.WHITE);

        System.out.println(ship_planetL.getStartX() + " " + ship_planetL.getStartY());
        System.out.println(ship_planetL.getEndX() + " " + ship_planetL.getEndY());
    }

    public double convertAbsX(double x){
        x -= origin.getOrigin().getX();
        x *= Scale.SCALE;
        return x + 400;
    }
    public double convertAbsY(double y){
        y -= origin.getOrigin().getY();
        y *= Scale.SCALE;
        return - y + 400;
    }
}

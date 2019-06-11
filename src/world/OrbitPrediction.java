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

    private Line ship_planetL = new Line(0,0,0,0);
    private Line shipDirection1L = new Line(0,0,0,0);
    private Line shipDirection2L = new Line(0,0,0,0);

    public OrbitPrediction(Spaceship spaceship, Origin origin, Fly fly){
        this.spaceship = spaceship;
        this.origin = origin;
        this.fly = fly;
        fly.getRoot().getChildren().addAll(ship_planetL, shipDirection1L, shipDirection2L);
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

        LinearFunction shipDirection = new LinearFunction(spaceship.getAbsPos(), spaceship.getVel_x(), spaceship.getVel_y());
        shipDirection1L.setStartX(convertAbsX(spaceship.getAbsPos().getX()));
        shipDirection1L.setStartY(convertAbsY(shipDirection.getYforX(spaceship.getAbsPos().getX())));
        shipDirection1L.setEndX(convertAbsX(spaceship.getAbsPos().getX() - shipDirection.getXfromRadius(4e7)));
        shipDirection1L.setEndY(convertAbsY(shipDirection.getYforX(spaceship.getAbsPos().getX() - shipDirection.getXfromRadius(4e7))));
        shipDirection1L.setStroke(Color.YELLOW);

        shipDirection2L.setStartX(convertAbsX(spaceship.getAbsPos().getX()));
        shipDirection2L.setStartY(convertAbsY(shipDirection.getYforX(spaceship.getAbsPos().getX())));
        shipDirection2L.setEndX(convertAbsX(spaceship.getAbsPos().getX() + shipDirection.getXfromRadius(4e7)));
        shipDirection2L.setEndY(convertAbsY(shipDirection.getYforX(spaceship.getAbsPos().getX() + shipDirection.getXfromRadius(4e7))));
        shipDirection2L.setStroke(Color.YELLOW);

        if(spaceship.getVel_x() > 0){
            shipDirection1L.setStrokeWidth(1);
            shipDirection2L.setStrokeWidth(2);
        }
        else{
            shipDirection1L.setStrokeWidth(2);
            shipDirection2L.setStrokeWidth(1);
        }

        shipDirection.print();

        //System.out.println(shipDirectionL.getStartX() + " " + shipDirectionL.getStartY());
        //System.out.println(shipDirectionL.getEndX() + " " + shipDirectionL.getEndY());
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

package world;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.transform.Rotate;
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
    private Line ship_focusL = new Line(0,0,0,0);
    private Circle c1 = new Circle();
    private Circle c2 = new Circle();
    private Circle centerC = new Circle();
    private Ellipse orbit = new Ellipse();
    private Rotate orbitRotate = new Rotate(45);

    public OrbitPrediction(Spaceship spaceship, Origin origin, Fly fly){
        this.spaceship = spaceship;
        this.origin = origin;
        this.fly = fly;
        fly.getRoot().getChildren().addAll(ship_planetL, shipDirection1L, shipDirection2L, ship_focusL, c1, c2, orbit, centerC);
        orbit.getTransforms().add(orbitRotate);
    }

    // yellow = bisector of white and red
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

        double angleYellow = Math.toDegrees(Math.atan2(spaceship.getVel_y(), spaceship.getVel_x()));
        double angleWhite = Math.toDegrees(Math.atan2(spaceship.getAbsPos().getY() - spaceship.getParent().getAbsPos().getY() , spaceship.getAbsPos().getX() - spaceship.getParent().getAbsPos().getX()));
        //System.out.println(angleYellow + " " + angleWhite);
        LinearFunction ship_focus = new LinearFunction(spaceship.getAbsPos(),Math.toRadians(2*angleYellow - angleWhite));

        ship_focusL.setStartX(convertAbsX(spaceship.getAbsPos().getX() - ship_focus.getXfromRadius(4e7)));
        ship_focusL.setStartY(convertAbsY(ship_focus.getYforX(spaceship.getAbsPos().getX() - ship_focus.getXfromRadius(4e7))));
        ship_focusL.setEndX(convertAbsX(spaceship.getAbsPos().getX() + ship_focus.getXfromRadius(4e7)));
        ship_focusL.setEndY(convertAbsY(ship_focus.getYforX(spaceship.getAbsPos().getX() + ship_focus.getXfromRadius(4e7))));
        ship_focusL.setStroke(Color.RED);

        double r1 = spaceship.getParent().getDistanceTo(spaceship.getAbsPos().getX(), spaceship.getAbsPos().getY());
        double Etotal = spaceship.getTotalMass() * spaceship.getSpeed() * spaceship.getSpeed() / 2
                - Const.G * spaceship.getParent().mass * spaceship.getTotalMass() / r1;

        double a = - Const.G * spaceship.getTotalMass() * spaceship.getParent().mass / Etotal / 2;
        double r2 = 2*a - r1;

        double dx = ship_focus.getXfromRadius(r2);

        Point p1 = new Point(spaceship.getAbsPos().getX() - dx, ship_focus.getYforX(spaceship.getAbsPos().getX() - dx));
        Point p2 = new Point(spaceship.getAbsPos().getX() + dx, ship_focus.getYforX(spaceship.getAbsPos().getX() + dx));
        c1.setRadius(4);
        c2.setRadius(4);
        c1.setCenterX(convertAbsX(p1.getX()));
        c1.setCenterY(convertAbsY(p1.getY()));
        c2.setCenterX(convertAbsX(p2.getX()));
        c2.setCenterY(convertAbsY(p2.getY()));
        c1.setFill(Color.RED);
        c2.setFill(Color.RED);

        double v1 = shipDirection.getYforX(spaceship.getParent().getAbsPos().getX());
        double s1 = spaceship.getParent().getAbsPos().getY() - v1;
        double v2 = shipDirection.getYforX(p1.getX());
        double s2 = p1.getY() - v2;
        Point foci2;
        if(s1 * s2 > 0){
            foci2 = new Point(p1.getX(), p1.getY());
        }
        else {
            foci2 = new Point(p2.getX(), p2.getY());
        }

        Point center = new Point(
                (spaceship.getParent().getAbsPos().getX() + foci2.getX())/2 ,
                (spaceship.getParent().getAbsPos().getY() + foci2.getY())/2);
        centerC.setCenterX(convertAbsX(center.getX()));
        centerC.setCenterY(convertAbsY(center.getY()));
        centerC.setFill(Color.GREEN);
        centerC.setRadius(4);

        double fociDist = spaceship.getParent().getDistanceTo(foci2.getX(), foci2.getY());
        double b = Math.sqrt(a*a - fociDist*fociDist/2/2);

        orbitRotate.setAngle(-Math.toDegrees(Math.atan2(
            spaceship.getParent().getAbsPos().getY() - foci2.getY(),
            spaceship.getParent().getAbsPos().getX() - foci2.getX())));
        orbitRotate.setPivotX(convertAbsX(center.getX()));
        orbitRotate.setPivotY(convertAbsY(center.getY()));

        orbit.setCenterX(convertAbsX(center.getX()));
        orbit.setCenterY(convertAbsY(center.getY()));
        orbit.setRadiusX(a * Scale.SCALE);
        orbit.setRadiusY(b * Scale.SCALE);

        orbit.setFill(Color.rgb(0,0,0,0));
        orbit.setStrokeWidth(3);
        orbit.setStroke(Color.SPRINGGREEN);
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


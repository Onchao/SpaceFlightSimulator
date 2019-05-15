package main_package;

import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import world.*;

public class Fly implements CustomScene{
    private Pane root = new Pane();
    private SolarSystem solarSystem = new SolarSystem();
    private Time time = new Time(0*3600*24);
    private Rectangle background;
    private Ship ship = new Ship(solarSystem.bodies.get(1), 0);
    Origin origin = new Origin(solarSystem.bodies, ship);

    Polygon polygon;
    Circle redCircle;
    Polygon flatPlanet;


    Fly(){
        root.setPrefSize(800, 800);
        background = new Rectangle(0,0,3840, 2160);
        background.setFill(Color.rgb(8, 8, 32));
        root.getChildren().add(background);

        for ( CelestialBody B: solarSystem.bodies){
            root.getChildren().add(B.shade);
            root.getChildren().add(B.planet);
        }

        flatPlanet = new Polygon();
        flatPlanet.setFill(Color.LIME);
        root.getChildren().add(flatPlanet);

        //PROTOTYPE SHIP
        polygon = new Polygon();
        polygon.setFill(Color.WHITE);
        redCircle = new Circle();
        redCircle.setFill(Color.RED);
        redCircle.setRadius(4);
        root.getChildren().add(polygon);
        root.getChildren().add(redCircle);



        update();
    }

    @Override
    public Pane getRoot(){
        return root;
    }
    @Override
    public void update() {
        Time.updateTime();
        flatPlanet.getPoints().clear();

        if(Const.SCALE > 0.01) {
            for (CelestialBody B : solarSystem.bodies) {
                double x = B.getAbsPos_x() / Const.SCALE;
                double y = B.getAbsPos_y() / Const.SCALE;

                x -= origin.getOrigin_x() / Const.SCALE;
                y -= origin.getOrigin_y() / Const.SCALE;

                y *= -1;
                double rShade = Math.pow(B.radius / (Math.pow(Const.SCALE, 0.3)), 1.0 / 4) * 2;
                double rPlanet = B.radius / Const.SCALE;

                x += 400;
                y += 400;

                B.shade.setCenterX(x);
                B.shade.setCenterY(y);
                B.shade.setRadius(rShade);
                B.planet.setCenterX(x);
                B.planet.setCenterY(y);
                B.planet.setRadius(rPlanet);

                Circle path = new Circle(x, y, 0.5, Color.GREEN);
                root.getChildren().add(path);
            }
        }
        else{
            for (CelestialBody B : solarSystem.bodies) {
                B.shade.setRadius(0);
                B.planet.setRadius(0);
            }

            double u = (ship.getAbsPos_x() - origin.getOrigin_x())/Const.SCALE;
            double v = (ship.getAbsPos_y() - origin.getOrigin_y())/Const.SCALE;
            flatPlanet.getPoints().addAll(
                    400 + u - 2000*Math.cos(Math.toRadians(ship.angleOnPlanet)),
                    400 + (v - 2000*Math.sin(Math.toRadians(ship.angleOnPlanet)))*-1,
                    400 + u - 600*Math.cos(Math.toRadians(ship.angleOnPlanet + 90)),
                    400 + (v - 600*Math.sin(Math.toRadians(ship.angleOnPlanet + 90)))*-1,
                    400 + u + 600*Math.cos(Math.toRadians(ship.angleOnPlanet + 90)),
                    400 + (v + 600*Math.sin(Math.toRadians(ship.angleOnPlanet + 90)))*-1
            );
        }


        ship.update();
        // PROTOTYPE SHIP
        polygon.getPoints().clear();

        double u = (ship.getAbsPos_x() - origin.getOrigin_x())/Const.SCALE;
        double v = (ship.getAbsPos_y() - origin.getOrigin_y())/Const.SCALE;

        polygon.getPoints().addAll(
                400 + u + 20*Math.cos(Math.toRadians(ship.angleOnPlanet)),
                400 + (v + 20*Math.sin(Math.toRadians(ship.angleOnPlanet)))*-1,
                400 + u - 5*Math.cos(Math.toRadians(ship.angleOnPlanet + 90)),
                400 + (v - 5*Math.sin(Math.toRadians(ship.angleOnPlanet + 90)))*-1,
                400 + u + 5*Math.cos(Math.toRadians(ship.angleOnPlanet + 90)),
                400 + (v + 5*Math.sin(Math.toRadians(ship.angleOnPlanet + 90)))*-1
        );
        redCircle.setCenterX(u+400);
        redCircle.setCenterY(v+400);
        redCircle.setRadius(4/Const.SCALE/1000);


    }
}

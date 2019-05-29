package main_package;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import ship.Spaceship;
import utility.Const;
import world.*;

public class Fly implements CustomScene{
    private Pane root = new Pane();
    private SolarSystem solarSystem = new SolarSystem();
    private Time time = new Time(0*3600*24);
    private Rectangle background;
    //private Ship ship = new Ship(solarSystem.bodies.get(1), 0);
    Spaceship spaceship;
    Origin origin;

    Polygon polygon;
    Circle redCircle;
    Polygon flatPlanet;


    Fly(Spaceship spaceship){
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

        this.spaceship = spaceship;
        spaceship.placeSpaceship(solarSystem.bodies.get(1), 0);
        root.getChildren().add(spaceship.getDrawable());
        origin = new Origin(solarSystem.bodies, spaceship);

        root.getChildren().add(spaceship.img);

        this.spaceship.setPrintPosition(400,400);
        System.out.println(this.spaceship.getThrustCenter().getX() +  " " + this.spaceship.getThrustCenter().getY());


/*
        //PROTOTYPE SHIP
        polygon = new Polygon();
        polygon.setFill(Color.WHITE);
        redCircle = new Circle();
        redCircle.setFill(Color.RED);
        redCircle.setRadius(4);
        root.getChildren().add(polygon);
        root.getChildren().add(redCircle);
*/
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

        if (Origin.originIndex == 0 && Scale.SCALE > 0.1){
            for (CelestialBody B : solarSystem.bodies) {
                B.shade.setRadius(0);
                B.planet.setRadius(0);
            }

            double u = (spaceship.getAbsPos_x() - origin.getOrigin_x()) * Scale.SCALE;
            double v = (spaceship.getAbsPos_y() - origin.getOrigin_y()) * Scale.SCALE;
            flatPlanet.getPoints().addAll(
                    400 + u - 6000*Math.cos(Math.toRadians(spaceship.angleOnPlanet)),
                    400 + (v - 6000*Math.sin(Math.toRadians(spaceship.angleOnPlanet)))*-1,
                    400 + u - 2600*Math.cos(Math.toRadians(spaceship.angleOnPlanet + 90)),
                    400 + (v - 2600*Math.sin(Math.toRadians(spaceship.angleOnPlanet + 90)))*-1,
                    400 + u + 2600*Math.cos(Math.toRadians(spaceship.angleOnPlanet + 90)),
                    400 + (v + 2600*Math.sin(Math.toRadians(spaceship.angleOnPlanet + 90)))*-1
            );
        }
        else{
            for (CelestialBody B : solarSystem.bodies) {
                double x = B.getAbsPos_x() * Scale.SCALE;
                double y = B.getAbsPos_y() * Scale.SCALE;

                x -= origin.getOrigin_x() * Scale.SCALE;
                y -= origin.getOrigin_y() * Scale.SCALE;

                y *= -1;
                double rShade = Math.pow(B.radius * (Math.pow(Scale.SCALE, 0.3)), 1.0 / 4) * 2;
                double rPlanet = B.radius * Scale.SCALE;

                x += 400;
                y += 400;

                B.shade.setCenterX(x);
                B.shade.setCenterY(y);
                B.shade.setRadius(rShade);
                B.planet.setCenterX(x);
                B.planet.setCenterY(y);
                B.planet.setRadius(rPlanet);

                //Circle path = new Circle(x, y, 0.5, Color.GREEN);
                //root.getChildren().add(path);
            }
        }

        spaceship.update();
        double u = (spaceship.getAbsPos_x() - origin.getOrigin_x()) * Scale.SCALE;
        double v = (spaceship.getAbsPos_y() - origin.getOrigin_y()) * Scale.SCALE;
        spaceship.setPrintPosition(400 + u, 400 + -v);

        if(Scale.SCALE < 1){
            spaceship.img.setVisible(true);
            spaceship.getDrawable().setVisible(false);
        }
        else {
            spaceship.img.setVisible(false);
            spaceship.getDrawable().setVisible(true);
        }
    }
}

package main_package;

import javafx.scene.control.ProgressBar;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import ship.Spaceship;
import ship.SpaceshipBuilder;
import utility.CustomWidgets;
import world.*;

public class Fly implements CustomScene{
    private Pane root = new Pane();
    private SolarSystem solarSystem = new SolarSystem();
    private Time time = new Time(0*3600*24);
    private Rectangle background;
    Spaceship spaceship;
    private OrbitPrediction orbitPrediction;

    Polygon polygon;
    Circle redCircle;
    Polygon flatPlanet;

    ProgressBar throttleState;
    ProgressBar fuelState;
    double fuelOnLiftoff;

    Fly(SpaceshipBuilder builder){
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

        spaceship = builder.build(solarSystem.bodies.get(1), 0, solarSystem);
        //spaceship.placeSpaceship();
        root.getChildren().add(spaceship.getDrawable());
        Origin.setup(solarSystem.bodies, spaceship);
        root.getChildren().add(spaceship.img);
        System.out.println(this.spaceship.getThrustCenter().getX() +  " " + this.spaceship.getThrustCenter().getY());

        VBox throttleWidget = new VBox();
        throttleState = CustomWidgets.customProgressBar("red");
        throttleState.setProgress(0);
        throttleWidget.setLayoutX(700);
        throttleWidget.setLayoutY(700);
        throttleWidget.getChildren().add(CustomWidgets.customLabel("Throttle:", 15));
        throttleWidget.getChildren().add(throttleState);
        root.getChildren().add(throttleWidget);

        fuelOnLiftoff = spaceship.getFuelTotal();

        VBox fuelWidget = new VBox();
        fuelState = CustomWidgets.customProgressBar("green");
        fuelState.setProgress(1);
        fuelWidget.setLayoutX(700);
        fuelWidget.setLayoutY(750);
        fuelWidget.getChildren().add(CustomWidgets.customLabel("Fuel:", 15));
        fuelWidget.getChildren().add(fuelState);
        root.getChildren().add(fuelWidget);

        this.orbitPrediction = new OrbitPrediction(spaceship,this);
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

        throttleState.setProgress((double)spaceship.getThrottle()/100);
        fuelState.setProgress(spaceship.getFuelTotal()/fuelOnLiftoff);

        if (Origin.originIndex == 0 && Scale.SCALE > 0.1){
            for (CelestialBody B : solarSystem.bodies) {
                B.shade.setRadius(0);
                B.planet.setRadius(0);
            }

            double u = (spaceship.getAbsPos().getX() - Origin.getOrigin().getX() - spaceship.getDistToBottom()*Math.cos(Math.toRadians(spaceship.getAngleOnPlanet()))) * Scale.SCALE;
            double v = (spaceship.getAbsPos().getY() - Origin.getOrigin().getY() - spaceship.getDistToBottom()*Math.sin(Math.toRadians(spaceship.getAngleOnPlanet()))) * Scale.SCALE;
            flatPlanet.getPoints().addAll(
                    400 + u - 6000*Math.cos(Math.toRadians(spaceship.getAngleOnPlanet())),
                    400 + (v - 6000*Math.sin(Math.toRadians(spaceship.getAngleOnPlanet())))*-1,
                    400 + u - 2600*Math.cos(Math.toRadians(spaceship.getAngleOnPlanet() + 90)),
                    400 + (v - 2600*Math.sin(Math.toRadians(spaceship.getAngleOnPlanet() + 90)))*-1,
                    400 + u + 2600*Math.cos(Math.toRadians(spaceship.getAngleOnPlanet() + 90)),
                    400 + (v + 2600*Math.sin(Math.toRadians(spaceship.getAngleOnPlanet() + 90)))*-1
            );
        }
        else{
            for (CelestialBody B : solarSystem.bodies) {
                double x = Origin.convertAbsX(B.getAbsPos().getX());
                double y = Origin.convertAbsY(B.getAbsPos().getY());

                double rShade = Math.pow(B.radius * (Math.pow(Scale.SCALE, 0.3)), 1.0 / 4) * 2;
                double rPlanet = B.radius * Scale.SCALE;

                B.shade.setCenterX(x);
                B.shade.setCenterY(y);
                B.shade.setRadius(rShade);
                B.planet.setCenterX(x);
                B.planet.setCenterY(y);
                B.planet.setRadius(rPlanet);
            }
        }

        spaceship.update();
        //TODO: recalculate MINIATURE origin only
        spaceship.recalculateOrigin(Origin.convertAbsX(spaceship.getAbsPos().getX()),
                Origin.convertAbsY(spaceship.getAbsPos().getY()));

        if(Scale.SCALE < 1){
            spaceship.img.setVisible(true);
            spaceship.getDrawable().setVisible(false);
        }
        else {
            spaceship.img.setVisible(false);
            spaceship.getDrawable().setVisible(true);
        }

        orbitPrediction.drawHelpersSimple();
    }
}
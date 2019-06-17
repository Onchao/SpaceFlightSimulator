package main_package;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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

    private ProgressBar throttleState;
    private ProgressBar fuelState;
    private double fuelOnLiftoff;
    private Label altitudeWidget;
    private Label orbitalSpeedWidget;
    private Label verticalSpeedToSurfaceWidget;
    private Label horizontalSpeedToSurfaceWidget;

    private boolean paused = false;
    private boolean crashScreenDisplayed = false;
    private Group pauseScreen;
    private Group crashScreen;

    private Circle projection = new Circle(4, Color.RED);

    Fly(SpaceshipBuilder builder){
        root.setPrefSize(800, 600);
        background = new Rectangle(0,0,3840, 2160);
        background.setFill(Color.rgb(8, 8, 32));
        root.getChildren().add(background);

        for ( CelestialBody B: solarSystem.bodies){
            root.getChildren().add(B.shade);
            root.getChildren().add(B.planet);
        }


        spaceship = builder.build(solarSystem.bodies.get(1), 0, solarSystem);
        root.getChildren().add(spaceship.getDrawable());
        Origin.setup(solarSystem.bodies, spaceship);
        root.getChildren().add(spaceship.img);
        System.out.println(this.spaceship.getThrustCenter().getX() +  " " + this.spaceship.getThrustCenter().getY());

        flatPlanet = new Polygon();
        flatPlanet.setFill(spaceship.getParent().color);
        root.getChildren().add(flatPlanet);

        altitudeWidget = CustomWidgets.customLabel("Altitude: " + (int)spaceship.getAltitude() + "m", 15);
        altitudeWidget.setLayoutX(500);
        altitudeWidget.setLayoutY(410);
        root.getChildren().add(altitudeWidget);

        orbitalSpeedWidget = CustomWidgets.customLabel("Orbital speed: " + (int)spaceship.getOrbitalSpeed() + "m/s", 15);
        orbitalSpeedWidget.setLayoutX(500);
        orbitalSpeedWidget.setLayoutY(430);
        root.getChildren().add(orbitalSpeedWidget);

        verticalSpeedToSurfaceWidget = CustomWidgets.customLabel("Vertical speed to surface: " + (int)spaceship.getVerticalOrbitalSpeed() + "m/s", 15);
        verticalSpeedToSurfaceWidget.setLayoutX(500);
        verticalSpeedToSurfaceWidget.setLayoutY(450);
        root.getChildren().add(verticalSpeedToSurfaceWidget);

        horizontalSpeedToSurfaceWidget = CustomWidgets.customLabel("Horizontal speed to surface: " + (int)spaceship.getHorizontalOrbitalSpeed() + "m/s", 15);
        horizontalSpeedToSurfaceWidget.setLayoutX(500);
        horizontalSpeedToSurfaceWidget.setLayoutY(470);
        root.getChildren().add(horizontalSpeedToSurfaceWidget);

        VBox throttleWidget = new VBox();
        throttleState = CustomWidgets.customProgressBar("red");
        throttleState.setProgress(0);
        throttleWidget.setLayoutX(500);
        throttleWidget.setLayoutY(500);
        throttleWidget.getChildren().add(CustomWidgets.customLabel("Throttle:", 15));
        throttleWidget.getChildren().add(throttleState);
        root.getChildren().add(throttleWidget);

        fuelOnLiftoff = spaceship.getFuelTotal();

        VBox fuelWidget = new VBox();
        fuelState = CustomWidgets.customProgressBar("green");
        fuelState.setProgress(1);
        fuelWidget.setLayoutX(500);
        fuelWidget.setLayoutY(550);
        fuelWidget.getChildren().add(CustomWidgets.customLabel("Fuel:", 15));
        fuelWidget.getChildren().add(fuelState);
        root.getChildren().add(fuelWidget);

        pauseScreen = new Group();
        Rectangle screenBg = new Rectangle(500, 500);
        screenBg.setFill(Color.rgb(8, 8, 32, 0.8));
        screenBg.setArcHeight(10);
        screenBg.setArcWidth(10);
        VBox pauseMenu = new VBox();
        pauseMenu.setAlignment(Pos.CENTER);
        Button continueButton = CustomWidgets.customButton("Continue");
        Button backButton = CustomWidgets.customButton("Back to menu");
        pauseMenu.getChildren().add(continueButton);
        pauseMenu.getChildren().add(backButton);
        pauseMenu.setLayoutX(200);
        pauseMenu.setLayoutY(200);
        pauseScreen.getChildren().addAll(screenBg, pauseMenu);
        pauseScreen.setLayoutX(150);
        pauseScreen.setLayoutY(150);
        continueButton.setOnAction(actionEvent -> {
            paused = false;
            root.getChildren().remove(pauseScreen);
        });

        backButton.setOnAction(actionEvent -> GamestateController.changeScene(Gamestate.gs.MENU, null));

        crashScreen = new Group();
        VBox crashMenu = new VBox();
        crashMenu.setAlignment(Pos.CENTER);
        crashMenu.setSpacing(15);
        crashMenu.getChildren().add(CustomWidgets.customLabel("You crashed!!!", 25));
        crashMenu.getChildren().add(backButton);
        crashMenu.setLayoutX(200);
        crashMenu.setLayoutY(200);
        crashScreen.getChildren().addAll(screenBg, crashMenu);
        crashScreen.setLayoutX(150);
        crashScreen.setLayoutY(150);

        this.orbitPrediction = new OrbitPrediction(spaceship,this);
        //root.getChildren().add(projection);

        update();

    }

    @Override
    public Pane getRoot(){
        return root;
    }
    @Override
    public void update() {
        if (paused) return;

        if (spaceship.isCrashed()) {
            if (!crashScreenDisplayed) {
                root.getChildren().add(crashScreen);
                crashScreenDisplayed = true;
            }
            return;
        }

        Time.updateTime();
        flatPlanet.getPoints().clear();

        throttleState.setProgress((double)spaceship.getThrottle()/100);
        fuelState.setProgress(spaceship.getFuelTotal()/fuelOnLiftoff);
        altitudeWidget.setText("Altitude: " + (int)spaceship.getAltitude() + "m");
        orbitalSpeedWidget.setText("Orbital speed: " + (int)spaceship.getOrbitalSpeed() + "m/s");
        verticalSpeedToSurfaceWidget.setText("Vertical speed to surface: " + (int)spaceship.getVerticalOrbitalSpeed() + "m/s");
        horizontalSpeedToSurfaceWidget.setText("Horizontal speed to surface: " + (int)spaceship.getHorizontalSurfaceSpeed() + "m/s");

        if (Origin.originIndex == 0 && Scale.SCALE > 0.1){
            for (CelestialBody B : solarSystem.bodies) {
                B.shade.setRadius(0);
                B.planet.setRadius(0);
            }

            double u = Origin.convertAbsX(spaceship.getParent().getAbsSpaceshipProjection(spaceship).getX());
            double v = Origin.convertAbsY(spaceship.getParent().getAbsSpaceshipProjection(spaceship).getY());
            projection.setCenterX(u);
            projection.setCenterY(v);

            //double u = (spaceship.getAbsPos().getX() - Origin.getOrigin().getX() -
             //       (spaceship.getAltitude() + spaceship.getLowestPointDist())*Math.cos(Math.toRadians(spaceship.getAngleOnPlanet()))) * Scale.SCALE;
            //double v = (spaceship.getAbsPos().getY() - Origin.getOrigin().getY() -
             //      (spaceship.getAltitude() + spaceship.getLowestPointDist())*Math.sin(Math.toRadians(spaceship.getAngleOnPlanet()))) * Scale.SCALE;

            flatPlanet.getPoints().addAll(
                     u - 6000*Math.cos(Math.toRadians(spaceship.getAngleOnPlanet())),
                    v +( - 6000*Math.sin(Math.toRadians(spaceship.getAngleOnPlanet())))*-1,
                    u - 2600*Math.cos(Math.toRadians(spaceship.getAngleOnPlanet() + 90)),
                    v + ( - 2600*Math.sin(Math.toRadians(spaceship.getAngleOnPlanet() + 90)))*-1,
                    u + 2600*Math.cos(Math.toRadians(spaceship.getAngleOnPlanet() + 90)),
                    v + (2600*Math.sin(Math.toRadians(spaceship.getAngleOnPlanet() + 90)))*-1
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
        if(!spaceship.isLanded())
            orbitPrediction.drawHelpersSimple();

    }

    public void pauseGame() {
        paused = true;
        root.getChildren().add(pauseScreen);
    }
}
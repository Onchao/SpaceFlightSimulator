package main_package;

import javafx.scene.control.ColorPicker;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import world.CelestialBody;
import world.Const;
import world.SolarSystem;
import world.Time;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Random;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class Fly implements CustomScene{
    private Pane root = new Pane();
    private SolarSystem solarSystem = new SolarSystem();
    private Time time = new Time(0*3600*24);
    private Rectangle background;

    Fly(){
        root.setPrefSize(800, 800);
        background = new Rectangle(0,0,3840, 2160);
        background.setFill(Color.rgb(8, 8, 32));
        root.getChildren().add(background);

        for ( CelestialBody B: solarSystem.bodies){
            root.getChildren().add(B.shade);
            root.getChildren().add(B.planet);
        }
        update();
    }

    @Override
    public Pane getRoot(){
        return root;
    }
    @Override
    public void update() {
        Time.updateTime();

        for ( CelestialBody B: solarSystem.bodies){
            double x = B.getAbsPos_x()/Const.SCALE;
            double y = B.getAbsPos_y()/Const.SCALE;
            x-=solarSystem.bodies.get(Const.originIndex).getAbsPos_x()/Const.SCALE;
            y-=solarSystem.bodies.get(Const.originIndex).getAbsPos_y()/Const.SCALE;
            y*=-1;
            double rShade = Math.pow(B.radius/(Math.pow(Const.SCALE,0.3)), 1.0/2.5)*2;
            double rPlanet = B.radius/Const.SCALE;

            x+=400;
            y+=400;

            B.shade.setCenterX(x);
            B.shade.setCenterY(y);
            B.shade.setRadius(rShade);
            B.planet.setCenterX(x);
            B.planet.setCenterY(y);
            B.planet.setRadius(rPlanet);
        }
    }
}

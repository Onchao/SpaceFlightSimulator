package main_package;

import javafx.scene.control.ColorPicker;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
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

    Fly(){
        root.setPrefSize(800, 800);

        for ( CelestialBody B: solarSystem.bodies){
            double x = B.getAbsPos_x()/500000;
            double y = B.getAbsPos_y()/500000;
            y*=-1;
            //System.out.println(x);
            //System.out.println(y);

            double r = B.radius/5000;
            x+=400;
            y+=400;
            Random rand = new Random();
            root.getChildren().add(new Circle(x,y,r));
            //root.getChildren().add(new Circle(x,y,r, new Color(rand.nextDouble(), rand.nextDouble(), rand.nextDouble(), 1.0)));
        }
    }

    @Override
    public Pane getRoot(){
        return root;
    }
    @Override
    public void update() {
        // TODO: is it optimal ???
        root.getChildren().clear();

        Time.updateTime();

        for ( CelestialBody B: solarSystem.bodies){
            double x = B.getAbsPos_x()/500000;
            double y = B.getAbsPos_y()/500000;
            y*=-1;
            //System.out.println(x);
            //System.out.println(y);

            double r = B.radius/5000;
            x+=400;
            y+=400;
            Random rand = new Random();
            root.getChildren().add(new Circle(x,y,r));
        }

    }
}

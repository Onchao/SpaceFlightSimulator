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
        update();
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

        // VISUAL HELP
        for ( CelestialBody B: solarSystem.bodies){
            double x = B.getAbsPos_x()/Const.SCALE;
            double y = B.getAbsPos_y()/Const.SCALE;
            x-=solarSystem.bodies.get(Const.originIndex).getAbsPos_x()/Const.SCALE;
            y-=solarSystem.bodies.get(Const.originIndex).getAbsPos_y()/Const.SCALE;


            y*=-1;
            //System.out.println(x);
            //System.out.println(y);

            // log_a(b) = log_x(b) / log_x(a)
            //double r = Math.log(B.radius)/Math.log(1.0001)/1000;
            double r = Math.pow(B.radius/(Math.pow(Const.SCALE,0.3)), 1.0/2.5)*2;
            x+=400;
            y+=400;


            Random rand = new Random();
            root.getChildren().add(new Circle(x,y,r, new Color(0,0,0, 0.3)));
        }

        // PLANET
        for ( CelestialBody B: solarSystem.bodies){
            double x = B.getAbsPos_x()/Const.SCALE;
            double y = B.getAbsPos_y()/Const.SCALE;
            x-=solarSystem.bodies.get(Const.originIndex).getAbsPos_x()/Const.SCALE;
            y-=solarSystem.bodies.get(Const.originIndex).getAbsPos_y()/Const.SCALE;

            y*=-1;
            //System.out.println(x);
            //System.out.println(y);

            double r = B.radius/Const.SCALE;
            x+=400;
            y+=400;
            Random rand = new Random();
            root.getChildren().add(new Circle(x,y,r));
        }
    }
}

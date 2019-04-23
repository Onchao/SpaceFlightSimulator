package main_package;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import world.CelestialBody;
import world.Const;
import world.SolarSystem;
import java.util.Random;


public class Fly implements CustomScene{
    private Pane root = new Pane();


    Circle orbitmoon = new Circle(16, Color.SLATEGRAY);
    double rad = 0;

    Fly(){
        root.setPrefSize(800, 800);

        SolarSystem solarSystem = new SolarSystem();
        Const.TIME = 0;
        for ( CelestialBody B: solarSystem.bodies){
            double x = B.getAbsPos_x()/500000;
            double y = B.getAbsPos_y()/500000;
            y*=-1;
            System.out.println(x);
            System.out.println(y);

            double r = B.radius/5000;
            x+=400;
            y+=400;
            Random rand = new Random();
            root.getChildren().add(new Circle(x,y,r, new Color(rand.nextDouble(), rand.nextDouble(), rand.nextDouble(), 1.0)));
        }
    }

    @Override
    public Pane getRoot(){
        return root;
    }
    @Override
    public void update() {
        orbitmoon.setCenterX(Math.cos(rad)*100 + 450);
        orbitmoon.setCenterY(Math.sin(rad)*100 + 150);
        rad += 0.04;

    }
}

package main_package;

import java.lang.Math;


import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;



public class Menu {
    private Pane root;
    Pane getRoot(){ return root; }
    Circle orbitmoon = new Circle(16, Color.SLATEGRAY);
    double rad = 0;

    Menu(){
        root = new Pane();
        root.setPrefSize(600, 600);
        root.getChildren().add(new Circle(450, 150, 40, Color.LIMEGREEN));
        root.getChildren().add(orbitmoon);

        Button buttonNewGame = new Button("New game");
        Button buttonContinueGame = new Button("Continue journey");
        buttonContinueGame.setTranslateY(30);
        root.getChildren().add(buttonNewGame);
        root.getChildren().add(buttonContinueGame);

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
            }
        };
        timer.start();
    }

    private void update() {
        orbitmoon.setCenterX(Math.cos(rad)*100 + 450);
        orbitmoon.setCenterY(Math.sin(rad)*100 + 150);
        rad += 0.04;

    }
}

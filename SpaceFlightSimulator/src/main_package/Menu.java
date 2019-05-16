package main_package;

import java.lang.Math;


import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;


public class Menu implements CustomScene {
    private Pane root = new Pane();


    Circle orbitmoon = new Circle(16, Color.SLATEGRAY);
    double rad = 0;

    Menu(){
        root.setPrefSize(600, 600);

        root.getChildren().add(new Circle(450, 150, 40, Color.LIMEGREEN));
        root.getChildren().add(orbitmoon);

        Button buttonNewGame = new Button("New game");
        root.getChildren().add(buttonNewGame);

        Button buttonTestFlight = new Button("Test flight");
        root.getChildren().add(buttonTestFlight);
        buttonTestFlight.setTranslateY(30);

        //Button buttonContinueGame = new Button("Continue journey");
        //buttonContinueGame.setTranslateY(30);
        //root.getChildren().add(buttonContinueGame);

        EventHandler<ActionEvent> eventNewGame = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                GamestateController.changeScene(Gamestate.gs.BUILD, null);
            }
        };
        buttonNewGame.setOnAction(eventNewGame);

        EventHandler<ActionEvent> eventTestFlight = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                GamestateController.changeScene(Gamestate.gs.FLY, null);
            }
        };
        buttonTestFlight.setOnAction(eventTestFlight);
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

package main_package;

import java.lang.Math;


import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.shape.Rectangle;
import utility.ControlBox;


public class Menu implements CustomScene {
    private Pane root = new Pane();


    Circle orbitmoon = new Circle(16, Color.SLATEGRAY);
    double rad = 0;

    Menu(){
        root.setPrefSize(800, 800);

        Rectangle background = new Rectangle(0, 0, 3840, 2160);
        background.setFill(Color.rgb(8, 8, 32));
        root.getChildren().add(background);


        root.getChildren().add(new Circle(450, 150, 40, Color.LIMEGREEN));
        root.getChildren().add(orbitmoon);

        Button buttonNewGame = new Button("New game");
        root.getChildren().add(buttonNewGame);

        Button buttonHelp = new Button("Help");
        buttonHelp.setTranslateY(30);
        root.getChildren().add(buttonHelp);

        Button buttonControls = new Button("Controls");
        buttonControls.setTranslateY(60);
        root.getChildren().add(buttonControls);

        EventHandler<ActionEvent> eventNewGame = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                GamestateController.changeScene(Gamestate.gs.BUILD, null);
            }
        };
        buttonNewGame.setOnAction(eventNewGame);

        EventHandler<ActionEvent> eventHelp = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                GamestateController.changeScene(Gamestate.gs.HELP, null);
            }
        };
        buttonHelp.setOnAction(eventHelp);

        EventHandler<ActionEvent> eventControls = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                GamestateController.changeScene(Gamestate.gs.CONTROLS, null);
            }
        };
        buttonControls.setOnAction(eventControls);
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

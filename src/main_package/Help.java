package main_package;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import utility.ControlBox;


public class Help implements CustomScene {
    private Pane root = new Pane();


    Circle orbitmoon = new Circle(16, Color.SLATEGRAY);
    double rad = 0;

    Help(){
        root.setPrefSize(800, 800);

        Rectangle background = new Rectangle(0, 0, 3840, 2160);
        background.setFill(Color.rgb(8, 8, 32));
        root.getChildren().add(background);


        root.getChildren().add(new Circle(450, 150, 40, Color.LIMEGREEN));
        root.getChildren().add(orbitmoon);

        Button buttonNewGame = new Button("New game");
        root.getChildren().add(buttonNewGame);

        Button buttonTestFlight = new Button("Test flight");
        root.getChildren().add(buttonTestFlight);
        buttonTestFlight.setTranslateY(30);

        VBox controls = ControlBox.createControlBox();
        controls.setTranslateY(60);
        root.getChildren().add(controls);

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

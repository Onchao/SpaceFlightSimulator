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


public class Controls implements CustomScene {
    private Pane root = new Pane();

    Controls(){
        root.setPrefSize(800, 800);

        Rectangle background = new Rectangle(0, 0, 3840, 2160);
        background.setFill(Color.rgb(8, 8, 32));
        root.getChildren().add(background);


        Button buttonBack = new Button("<- back");
        root.getChildren().add(buttonBack);

        VBox controls = ControlBox.createControlBox();
        controls.setTranslateY(60);
        root.getChildren().add(controls);

        //Button buttonContinueGame = new Button("Continue journey");
        //buttonContinueGame.setTranslateY(30);
        //root.getChildren().add(buttonContinueGame);

        EventHandler<ActionEvent> eventBack= new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                GamestateController.changeScene(Gamestate.gs.BUILD, null);
            }
        };
        buttonBack.setOnAction(eventBack);
    }


    @Override
    public Pane getRoot(){
        return root;
    }
    @Override
    public void update() {

    }
}

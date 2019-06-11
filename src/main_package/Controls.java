package main_package;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import utility.ControlBox;


public class Controls implements CustomScene {
    private Pane root = new Pane();

    Controls(){
        root.setPrefSize(800, 800);

        Rectangle background = new Rectangle(0, 0, 3840, 2160);
        background.setFill(Color.rgb(8, 8, 32));
        root.getChildren().add(background);

        Button buttonBack = new Button(" <-- back  ");
        String iddleButton = "-fx-padding: 7;" + "-fx-border-style: solid inside;"
                + "-fx-border-width: 2;" + "-fx-border-insets: 4;"
                + "-fx-border-radius: 5;" + "-fx-border-color: rgba(0, 255, 255, 0.8);"
                + "-fx-background-color: rgba(0, 255, 255, 0.3);"
                + "-fx-background-radius: 5;" + "-fx-background-insets: 4;"
                + "-fx-font-size: 16px;";
        String hoveredButton = "-fx-padding: 7;" + "-fx-border-style: solid inside;"
                + "-fx-border-width: 2;" + "-fx-border-insets: 4;"
                + "-fx-border-radius: 5;" + "-fx-border-color: rgba(0, 255, 255, 0.8);"
                + "-fx-background-color: rgba(0, 255, 255, 0.5);"
                + "-fx-background-radius: 5;" + "-fx-background-insets: 4;"
                + "-fx-font-size: 16px;";
        buttonBack.setStyle(iddleButton);
        buttonBack.setOnMouseEntered(e -> buttonBack.setStyle(hoveredButton));
        buttonBack.setOnMouseExited(e -> buttonBack.setStyle(iddleButton));
        buttonBack.setTextFill(Color.rgb(0,255,255,0.8));
        buttonBack.setTranslateX(600);
        buttonBack.setTranslateY(700);
        root.getChildren().add(buttonBack);

        VBox controls = ControlBox.createControlBox();
        controls.setTranslateX(100);
        controls.setTranslateY(50);

        buttonBack.setTranslateX(600);
        buttonBack.setTranslateY(700);


        root.getChildren().add(controls);

        EventHandler<ActionEvent> eventBack= new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                GamestateController.changeScene(Gamestate.gs.MENU, null);
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

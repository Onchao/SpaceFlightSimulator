package main_package;

import java.lang.Math;


import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.shape.Rectangle;

public class Menu implements CustomScene {
    private Pane root = new Pane();

    //TODO: set title of game
    Menu(){
        root.setPrefSize(800, 800);

        Rectangle background = new Rectangle(0, 0, 3840, 2160);
        background.setFill(Color.rgb(8, 8, 32));
        root.getChildren().add(background);

        Button buttonNewGame = customButton("New game");
        Button buttonHelp = customButton("Tips and tricks");
        Button buttonControls = customButton("Controls");
        Button buttonExit = customButton("Exit");

        VBox box = new VBox();
        box.getChildren().addAll(buttonNewGame, buttonHelp, buttonControls, buttonExit);
        box.setAlignment(Pos.CENTER);
        VBox.setVgrow(box, Priority.ALWAYS);
        box.prefWidthProperty().bind(root.widthProperty());
        box.prefHeightProperty().bind(root.heightProperty());
        root.getChildren().add(box);


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

        EventHandler<ActionEvent> eventExit = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                GamestateController.changeScene(Gamestate.gs.EXIT, null);
            }
        };
        buttonExit.setOnAction(eventExit);
    }

    @Override
    public Pane getRoot(){
        return root;
    }
    @Override
    public void update() {
    }

    private Button customButton(String name){
        Button button = new Button(name);
        String iddleButton = "-fx-padding: 7;" + "-fx-border-style: solid inside;"
                + "-fx-border-width: 2;" + "-fx-border-insets: 10;"
                + "-fx-border-radius: 5;" + "-fx-border-color: rgba(0, 255, 255, 0.8);"
                + "-fx-background-color: rgba(0, 255, 255, 0.3);"
                + "-fx-background-radius: 5;" + "-fx-background-insets: 10;"
                + "-fx-font-size: 24px;" + "-fx-font-weight: bold;";
        String hoveredButton = "-fx-padding: 7;" + "-fx-border-style: solid inside;"
                + "-fx-border-width: 2;" + "-fx-border-insets: 10;"
                + "-fx-border-radius: 5;" + "-fx-border-color: rgba(0, 255, 255, 0.8);"
                + "-fx-background-color: rgba(0, 255, 255, 0.5);"
                + "-fx-background-radius: 5;" + "-fx-background-insets: 10;"
                + "-fx-font-size: 24px;" + "-fx-font-weight: bold;";

        button.setStyle(iddleButton);
        button.setOnMouseEntered(e -> button.setStyle(hoveredButton));
        button.setOnMouseExited(e -> button.setStyle(iddleButton));
        button.setTextFill(Color.rgb(0,255,255,0.8));

        return button;
    }
}

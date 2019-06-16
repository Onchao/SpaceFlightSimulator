package main_package;


import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.shape.Rectangle;
import utility.CustomWidgets;

public class Menu implements CustomScene {
    private Pane root = new Pane();

    //TODO: set title of game
    Menu(){
        root.setPrefSize(800, 600);

        Rectangle background = new Rectangle(0, 0, 3840, 2160);
        background.setFill(Color.rgb(8, 8, 32));
        root.getChildren().add(background);

        Button buttonNewGame = CustomWidgets.customButton("New game");
        Button buttonHelp = CustomWidgets.customButton("Tips and tricks");
        Button buttonControls = CustomWidgets.customButton("Controls");
        Button buttonExit = CustomWidgets.customButton("Exit");

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

}

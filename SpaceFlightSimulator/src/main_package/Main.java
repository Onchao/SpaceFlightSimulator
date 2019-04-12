package main_package;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;



public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Scene sc = new Scene(GamestateController.createScene());
        stage.setScene(sc);
        stage.show();
    }

    public static void main(String[] args) {
        Gamestate.setGamestateMENU();
        launch(args);
    }
}
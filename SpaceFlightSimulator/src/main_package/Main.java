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



public class Main extends Application {
    enum gamestate{
        MENU,
        BUILD,
        FLY,
        // FAIL, // ???
        // SUCCEED // ???
    }
    static gamestate GS;

    private Parent createMenu() {
        return new Menu().getRoot();
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(new Scene(createMenu()));

        stage.show();
    }

    public static void main(String[] args) {
        GS = gamestate.MENU;
        launch(args);
    }
}
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

public class GamestateController {
    static Stage stage;
    static CustomScene currentScene;
    static Scene SC;

    GamestateController(Stage stage){
        this.stage = stage;

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                currentScene.update();
            }
        };
        timer.start();
    }

    static void changeScene(Gamestate.gs newGS){
        if(newGS == Gamestate.gs.MENU){
            Gamestate.setGamestateMENU();
            currentScene = new Menu();
            SC = new Scene(currentScene.getRoot());
        }
        else if(newGS == Gamestate.gs.BUILD){
            Gamestate.setGamestateBUILD();
            //currentScene = new                                                    SOMETHING NICE();
            Pane test = new Pane();
            test.setPrefSize(600,600);
            test.getChildren().add(new Rectangle(600,600,Color.SEAGREEN));
            SC = new Scene(test);
        }
        else //if(newGS == Gamestate.gs.FLY)
        {

        }
        stage.setScene(SC);
        stage.show();
    }
}

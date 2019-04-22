package main_package;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

            ObservableList <SpaceshipComponentFactory> components = FXCollections.observableArrayList(    // TODO: find a good place to put this list
                    new SpaceshipComponentFactory<>(CrewCapsuleComponent.class),
                    new SpaceshipComponentFactory<>(FuelTankComponent.class),
                    new SpaceshipComponentFactory<>(PowerfulAtmosphericEngineComponent.class),
                    new SpaceshipComponentFactory<>(HighEfficiencyVaccumEngineComponent.class),
                    new SpaceshipComponentFactory<>(AerodynamicNoseConeComponent.class),
                    new SpaceshipComponentFactory<>(ParachuteComponent.class),
                    new SpaceshipComponentFactory<>(CircularDecouplerComponent.class),
                    new SpaceshipComponentFactory<>(RadialDecouplerComponent.class),
                    new SpaceshipComponentFactory<>(LandingStrutsComponent.class));

            currentScene = new Build(components);
            SC = new Scene(currentScene.getRoot());
        }
        else //if(newGS == Gamestate.gs.FLY)
        {

        }
        stage.setScene(SC);
        stage.show();
    }
}

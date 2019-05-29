package main_package;

import javafx.animation.AnimationTimer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import ship.*;
import utility.Const;
import world.Origin;
import world.Time;

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

    static void changeScene(Gamestate.gs newGS, Spaceship spaceship){
        if(newGS == Gamestate.gs.MENU){
            Gamestate.setGamestateMENU();
            currentScene = new Menu();
            SC = new Scene(currentScene.getRoot());
            stage.setScene(SC);
            stage.show();
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
            stage.setScene(SC);
            stage.show();
        }
        else //if(newGS == Gamestate.gs.FLY)
        {
            Gamestate.setGamestateFLY();
            currentScene = new Fly(spaceship);
            SC = new Scene(currentScene.getRoot());

            SC.setOnKeyPressed(e -> {
                if (e.getCode() == KeyCode.ADD || e.getCode() == KeyCode.EQUALS) {
                    Time.faster();
                    System.out.println(Time.timeWarp + "x");

                }
                if (e.getCode() == KeyCode.SUBTRACT || e.getCode() == KeyCode.MINUS) {
                    Time.slower();
                    System.out.println(Time.timeWarp + "x");
                }

                if(e.getCode() == KeyCode.TAB){
                    Origin.cycleOrigin();
                }

                if (e.getCode() == KeyCode.M) {
                    if(Const.SCALE < 0.1)
                         Const.SCALE = 5000;
                    else
                        Const.SCALE = 0.01;
                }

                if (e.getCode() == KeyCode.Z) {
                    spaceship.maxThrottle();
                }

                if (e.getCode() == KeyCode.X) {
                    spaceship.zeroThrottle();
                }

                if (e.getCode() == KeyCode.W || e.getCode() == KeyCode.SHIFT) {
                    spaceship.setThrottleModifier(10);
                }
                else if (e.getCode() == KeyCode.S || e.getCode() == KeyCode.CONTROL) {
                    spaceship.setThrottleModifier(-10);
                }

                // shift = w, ctrl = s, a, d
            });

            SC.setOnKeyReleased(e-> {
                if (e.getCode() == KeyCode.W || e.getCode() == KeyCode.SHIFT
                    || e.getCode() == KeyCode.S || e.getCode() == KeyCode.CONTROL) {
                    spaceship.setThrottleModifier(0);
                }
            });

            SC.setOnScroll(e -> {
                double y = e.getDeltaY();
                if (y > 0) {
                    Const.SCALE*=0.9;
                } else if (y<0){
                    Const.SCALE*=1.1;
                }
                System.out.println(Const.SCALE);
            });

            stage.setScene(SC);
            stage.show();
        }
    }
}

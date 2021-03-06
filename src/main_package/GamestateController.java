package main_package;

import javafx.animation.AnimationTimer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import ship.*;
import ship.components.*;
import world.Origin;
import world.Scale;
import world.Time;

public class GamestateController {
    static Stage stage;
    static CustomScene currentScene;
    static Scene SC;

    public static void setup(Stage stage){
        GamestateController.stage = stage;

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                currentScene.update();
            }
        };
        timer.start();
    }


    static void changeScene(Gamestate.gs newGS, SpaceshipBuilder builder){
        if(newGS == Gamestate.gs.MENU){
            Gamestate.setGamestateMENU();
            currentScene = new Menu();
            SC = new Scene(currentScene.getRoot());
            stage.setScene(SC);
            stage.show();
        }
        else if(newGS == Gamestate.gs.BUILD){
            Gamestate.setGamestateBUILD();

            ObservableList <SpaceshipComponentFactory> components = FXCollections.observableArrayList(
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
        else if(newGS == Gamestate.gs.FLY)
        {
            Gamestate.setGamestateFLY();
            currentScene = new Fly(builder);
            SC = new Scene(currentScene.getRoot());
            Spaceship spaceship = builder.getSpaceship();

            SC.setOnKeyPressed(e -> {
                if (e.getCode() == KeyCode.ADD || e.getCode() == KeyCode.EQUALS) {
                    Time.faster();
                    System.out.println(Time.getTimeWarp() + "x");

                }
                if (e.getCode() == KeyCode.SUBTRACT || e.getCode() == KeyCode.MINUS) {
                    Time.slower();
                    System.out.println(Time.getTimeWarp() + "x");
                }

                if(e.getCode() == KeyCode.TAB){
                    Origin.cycleOrigin();
                }

                if (e.getCode() == KeyCode.M) {
                    Scale.cycleScale();
                }

                if (e.getCode() == KeyCode.Z) {
                    spaceship.maxThrottle();
                }

                if (e.getCode() == KeyCode.X) {
                    spaceship.zeroThrottle();
                }

                if (e.getCode() == KeyCode.W || e.getCode() == KeyCode.SHIFT) {
                    spaceship.setThrottleModifier(1);
                }
                else if (e.getCode() == KeyCode.S || e.getCode() == KeyCode.CONTROL) {
                    spaceship.setThrottleModifier(-1);
                }

                if (e.getCode() == KeyCode.A) {
                    spaceship.setTurnMomentum(-5e+7);
                }
                else if (e.getCode() == KeyCode.D) {
                    spaceship.setTurnMomentum(5e7);
                }

                if (e.getCode() == KeyCode.SPACE) {
                    spaceship.activateNext();
                }

                if (e.getCode() == KeyCode.ESCAPE) {
                    if (currentScene instanceof Fly) {
                        ((Fly) currentScene).pauseGame();
                    }
                }

                // INFO
                if (e.getCode() == KeyCode.I) {
                    spaceship.info();
                }
            });

            SC.setOnKeyReleased(e-> {
                if (e.getCode() == KeyCode.W || e.getCode() == KeyCode.SHIFT
                    || e.getCode() == KeyCode.S || e.getCode() == KeyCode.CONTROL) {
                    spaceship.setThrottleModifier(0);
                }

                if (e.getCode() == KeyCode.A || e.getCode() == KeyCode.D) {
                    spaceship.setTurnMomentum(0);
                }
            });

            SC.setOnScroll(e -> {
                double y = e.getDeltaY();
                Scale.zoom(y);
                //System.out.println(Scale.SCALE);
            });

            stage.setScene(SC);
            stage.show();
        }
        else if(newGS == Gamestate.gs.HELP) {
            Gamestate.setGamestateHELP();
            currentScene = new Help();
            SC = new Scene(currentScene.getRoot());

            stage.setScene(SC);
            stage.show();
        }
        else if(newGS == Gamestate.gs.CONTROLS)
        {
            Gamestate.setGamestateCONTROLS();
            currentScene = new Controls();
            SC = new Scene(currentScene.getRoot());

            stage.setScene(SC);
            stage.show();
        }
        else{ // exit
            stage.close();
        }
    }
}

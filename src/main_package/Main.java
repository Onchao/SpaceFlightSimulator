package main_package;

import javafx.application.Application;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import utility.Const;


public class Main extends Application {
    public Stage pubStage;
    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Space Flight Simulator");
        GamestateController.setup(stage);
        GamestateController.changeScene(Gamestate.gs.MENU, null);
    }

    public static void main(String[] args) {
        Gamestate.setGamestateMENU();
        launch(args);
    }
}
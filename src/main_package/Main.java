package main_package;

import javafx.application.Application;
import javafx.stage.Stage;


public class Main extends Application {
    public Stage pubStage;
    @Override
    public void start(Stage stage) throws Exception {
        //TODO: set title
        GamestateController.setup(stage);
        GamestateController.changeScene(Gamestate.gs.MENU, null);
    }

    public static void main(String[] args) {
        Gamestate.setGamestateMENU();
        launch(args);
    }
}
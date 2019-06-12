package main_package;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Help implements CustomScene {
    private Pane root = new Pane();

    Help(){
        root.setPrefSize(800, 800);

        Rectangle background = new Rectangle(0, 0, 3840, 2160);
        background.setFill(Color.rgb(8, 8, 32));
        root.getChildren().add(background);

        VBox box = new VBox();

        Label nameL = new Label("  Tips and tricks");
        nameL.setStyle("-fx-font-size: 24px;" + "-fx-font-weight: bold;");
        nameL.setPadding(new Insets(0, 0, 10, 0));
        nameL.setTextFill(Color.rgb(0,255,255,0.8));
        box.getChildren().add(nameL);

        box.getChildren().addAll(
                customHBox("Don't lift dead mass. Single stage to orbit (SSTO) rockets are very inefficient. " +
                        "Take advantage of decouplers and design multistage vehicles."),
                customHBox("Remember to assign proper stage numbers in building mode " +
                        "or you may accidentally open your parachute on lift off."),
                customHBox("Different engines works better on different pressures of atmosphere."),
                customHBox("Aerodynamic design will save you tons of fuel."),
                customHBox("Landing struts can withstand higher stress than other parts of rocket."),
                customHBox("The most efficient way to reach stable orbit is to first quickly boost through the atmosphere " +
                        "and shut down the engines when the highest point of your orbit (apoapsis) is at the desired altitude. " +
                        "Around that point turn on engines again to rise lowest side of your orbit (periapsis).")
        );
        box.setTranslateX(100);
        box.setTranslateY(50);
        root.getChildren().add(box);

        Button buttonBack = new Button(" <-- back  ");
        String iddleButton = "-fx-padding: 7;" + "-fx-border-style: solid inside;"
                + "-fx-border-width: 2;" + "-fx-border-insets: 4;"
                + "-fx-border-radius: 5;" + "-fx-border-color: rgba(0, 255, 255, 0.8);"
                + "-fx-background-color: rgba(0, 255, 255, 0.3);"
                + "-fx-background-radius: 5;" + "-fx-background-insets: 4;"
                + "-fx-font-size: 16px;";
        String hoveredButton = "-fx-padding: 7;" + "-fx-border-style: solid inside;"
                + "-fx-border-width: 2;" + "-fx-border-insets: 4;"
                + "-fx-border-radius: 5;" + "-fx-border-color: rgba(0, 255, 255, 0.8);"
                + "-fx-background-color: rgba(0, 255, 255, 0.5);"
                + "-fx-background-radius: 5;" + "-fx-background-insets: 4;"
                + "-fx-font-size: 16px;";
        buttonBack.setStyle(iddleButton);
        buttonBack.setOnMouseEntered(e -> buttonBack.setStyle(hoveredButton));
        buttonBack.setOnMouseExited(e -> buttonBack.setStyle(iddleButton));
        buttonBack.setTextFill(Color.rgb(0,255,255,0.8));
        buttonBack.setTranslateX(600);
        buttonBack.setTranslateY(700);
        root.getChildren().add(buttonBack);

        EventHandler<ActionEvent> eventBack= new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                GamestateController.changeScene(Gamestate.gs.MENU, null);
            }
        };
        buttonBack.setOnAction(eventBack);
    }

    private HBox customHBox(String text){
        HBox box = new HBox();
        box.setStyle("-fx-padding: 7;" + "-fx-border-style: solid inside;"
                + "-fx-border-width: 2;" + "-fx-border-insets: 10 0 10 0;"
                + "-fx-border-radius: 5;" + "-fx-border-color: rgba(0, 255, 255, 0.8);"
                + "-fx-background-color: rgba(0, 255, 255, 0.2);"
                + "-fx-background-radius: 5;" + "-fx-background-insets: 10 0 10 0;"
        );

        Label textL = new Label(text);
        textL.setWrapText(true);
        textL.setTextFill(Color.rgb(0,255,255,0.8));
        box.getChildren().add(textL);
        box.setMaxWidth(600);
        return box;
    }


    @Override
    public Pane getRoot(){
        return root;
    }
    @Override
    public void update() {

    }
}

package main_package;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

public class Build implements CustomScene {
    private Pane root = new Pane();
    private ListView<SpaceshipComponent> componentList;

    private int nextComponentPosition;
    private Rectangle nextComponent;

    Build (ObservableList<SpaceshipComponent> availableComponents) {
        componentList = new ListView<>();
        componentList.setItems(availableComponents);

        componentList.setLayoutX(600);

        root.setPrefSize(850, 850);
        nextComponentPosition = 800;
        root.getChildren().add(componentList);

        Button flyButton = new Button("Let's fly!");
        // TODO: event handler for this one
        root.getChildren().add(flyButton);

        nextComponent = new Rectangle(150, nextComponentPosition - 50, 200, 50);
        nextComponent.setArcHeight(10);
        nextComponent.setArcWidth(10);
        nextComponent.setFill(Color.LIMEGREEN);

        EventHandler<MouseEvent> eventInsertComponent = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent actionEvent) {
                SpaceshipComponent chosenComponent = componentList.getSelectionModel().getSelectedItem();

                if (chosenComponent == null) return;

                ImageView componentImage = new ImageView(chosenComponent.getImage());
                componentImage.setX(210 - chosenComponent.getWidth()/2);
                componentImage.setY(nextComponentPosition - chosenComponent.getHeight());
                root.getChildren().add(componentImage);

                nextComponentPosition -= chosenComponent.getHeight();
                nextComponent.setY(nextComponentPosition - nextComponent.getHeight());
            }
        };

        nextComponent.setOnMouseClicked(eventInsertComponent);

        root.getChildren().add(nextComponent);
    }

    @Override
    public void update() {

    }

    @Override
    public Pane getRoot() {
        return root;
    }
}

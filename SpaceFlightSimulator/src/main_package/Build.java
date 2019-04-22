package main_package;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

public class Build implements CustomScene {
    private Pane root = new Pane();
    private ScrollPane scroller = new ScrollPane();
    private ListView<SpaceshipComponentFactory> componentList;

    private int nextComponentPosition;
    private Rectangle nextComponent;

    Build (ObservableList<SpaceshipComponentFactory> availableComponents) {
        root.getChildren().add(scroller);
        Pane spaceshipView = new Pane();
        scroller.setContent(spaceshipView);
        scroller.setVmax(850);
        scroller.setPrefSize(600, 850);
        scroller.setPannable(true);
        scroller.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroller.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        componentList = new ListView<>();
        componentList.setItems(availableComponents);

        componentList.setLayoutX(600);

        root.setPrefSize(850, 850);
        nextComponentPosition = 800;
        root.getChildren().add(componentList);

        Button flyButton = new Button("Let's fly!");
        // TODO: event handler for this one
        root.getChildren().add(flyButton);

        nextComponent = new Rectangle( 200, 50);
        nextComponent.setArcHeight(10);
        nextComponent.setArcWidth(10);
        nextComponent.setFill(Color.LIMEGREEN);

        nextComponent.setLayoutX(150);
        nextComponent.setLayoutY(nextComponentPosition - nextComponent.getHeight());

        EventHandler<MouseEvent> eventInsertComponent = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent actionEvent) {
                SpaceshipComponentFactory chosenComponentF = componentList.getSelectionModel().getSelectedItem();

                SpaceshipComponent chosenComponent = null;
                try {
                    chosenComponent = chosenComponentF.getInstance();             // TODO: check left/right
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                }

                if (chosenComponent == null) return;

                ImageView componentImage = new ImageView(chosenComponent.getImage());
                componentImage.setLayoutX(210 - chosenComponent.getWidth()/2);
                componentImage.setLayoutY(nextComponentPosition - chosenComponent.getHeight());
                spaceshipView.getChildren().add(componentImage);

                nextComponentPosition -= chosenComponent.getHeight();
                if (nextComponentPosition - nextComponent.getHeight() <= 10) {
                    nextComponentPosition += chosenComponent.getHeight();
                    for (Node n : spaceshipView.getChildren()) {
                        n.setLayoutY(n.getLayoutY() + chosenComponent.getHeight());
                    }
                }
                nextComponent.setLayoutY(nextComponentPosition - nextComponent.getHeight());
            }
        };

        nextComponent.setOnMouseClicked(eventInsertComponent);

        spaceshipView.getChildren().add(nextComponent);
    }

    @Override
    public void update() {

    }

    @Override
    public Pane getRoot() {
        return root;
    }
}

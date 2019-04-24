package main_package;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Build implements CustomScene {
    private Pane root = new Pane();
    private ScrollPane scroller = new ScrollPane();
    private ListView<SpaceshipComponentFactory> componentList;

    private int nextComponentPosition;
    private Rectangle nextComponent;
    private Rectangle base;

    private SpaceshipBuilder builder;

    private MountImg getMountPointImg(Mount m) {
        MountImg ret = new MountImg(m.getWidth(), m.getHeight());
        double x = m.getPositionX();
        double y = m.getPositionY();

        ret.setArcWidth(10);
        ret.setArcHeight(10);
        ret.setFill(Color.LIMEGREEN);
        ret.setLayoutX(x);
        ret.setLayoutY(y);

        return ret;
    }

    Build (ObservableList<SpaceshipComponentFactory> availableComponents) {
        builder = new SpaceshipBuilder();

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

        base = new Rectangle(600, 20);
        base.setLayoutX(0);
        base.setLayoutY(820);

        nextComponent = new Rectangle( 200, 50);
        nextComponent.setArcHeight(10);
        nextComponent.setArcWidth(10);
        nextComponent.setFill(Color.LIMEGREEN);

        nextComponent.setLayoutX(150);
        nextComponent.setLayoutY(nextComponentPosition - nextComponent.getHeight());

        componentList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                ArrayList<Node> toRemove = new ArrayList<>();
                for (Node n : spaceshipView.getChildren()) {
                    if (n instanceof MountImg) {
                        toRemove.add(n);
                    }
                }
                spaceshipView.getChildren().removeAll(toRemove.toArray(new Node[toRemove.size()]));

                SpaceshipComponentFactory factory = componentList.getSelectionModel().getSelectedItem();
                System.out.println("Selected " + factory);

                List<Mount> possibleMountPoints = builder.getMountPoints(factory);

                for (Mount m : possibleMountPoints) {
                    MountImg img = getMountPointImg(m);
                    spaceshipView.getChildren().add(img);

                    img.setOnMouseClicked(new MountClickHandler(spaceshipView, builder, factory, m));
                }
            }
        });

        /*
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

                ImageView componentImage = chosenComponent.getImage();
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
        */
        spaceshipView.getChildren().add(base);
    }

    @Override
    public void update() {

    }

    @Override
    public Pane getRoot() {
        return root;
    }

    private static class MountClickHandler implements EventHandler<MouseEvent> {
        SpaceshipComponentFactory chosenComponentF;
        Mount myMount;
        Pane spaceshipView;
        SpaceshipBuilder builder;

        public MountClickHandler(Pane p, SpaceshipBuilder b, SpaceshipComponentFactory factory, Mount m) {
            chosenComponentF = factory;
            myMount = m;
            spaceshipView = p;
            builder = b;
        }

        @Override
            public void handle(MouseEvent actionEvent) {
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

                ImageView componentImage = chosenComponent.getImage();
                componentImage.setLayoutX(myMount.getPositionX() + (myMount.getWidth() - chosenComponent.getWidth())/2);
                componentImage.setLayoutY(myMount.getPositionY() - chosenComponent.getHeight() + 60);
                spaceshipView.getChildren().add(componentImage);

                if (myMount.getPositionY() - chosenComponent.getHeight() - myMount.getHeight() <= 10) {
                    for (Node n : spaceshipView.getChildren()) {
                        n.setLayoutY(n.getLayoutY() + chosenComponent.getHeight());
                    }
                }

                ArrayList<Node> toRemove = new ArrayList<>();
                for (Node n : spaceshipView.getChildren()) {
                    if (n instanceof MountImg) {
                        toRemove.add(n);
                    }
                }
                spaceshipView.getChildren().removeAll(toRemove.toArray(new Node[toRemove.size()]));

                myMount.setUsed(true);
                switch (myMount.getDirection()) {
                    case LOWER:
                        chosenComponent.getUpperMount().setUsed(true);
                        break;
                    case UPPER:
                        chosenComponent.getLowerMount().setUsed(true);
                        break;
                    case LEFT:
                        chosenComponent.getRightMount().setUsed(true);
                        break;
                    case RIGHT:
                        chosenComponent.getLeftMount().setUsed(true);
                }

                builder.addComponent(chosenComponent);
            }
    }
}

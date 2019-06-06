package main_package;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import ship.*;
import utility.Direction;
import utility.Mount;
import utility.MountImg;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class Build implements CustomScene {
    private Pane root = new Pane();
    private ScrollPane scroller = new ScrollPane();
    private ListView<SpaceshipComponentFactory> componentList;
    private Label componentDesc;

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

        componentDesc = new Label();
        componentDesc.setText("");
        componentDesc.setLayoutX(610);
        componentDesc.setLayoutY(400);

        componentList = new ListView<>();
        componentList.setItems(availableComponents);

        componentList.setLayoutX(600);

        root.setPrefSize(850, 850);
        nextComponentPosition = 800;
        root.getChildren().addAll(componentList, componentDesc);

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

        componentList.setOnMouseClicked(mouseEvent -> {
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
                spaceshipView.getChildren().add(0, img);

                img.setOnMouseClicked(new MountClickHandler(spaceshipView, componentDesc, builder, factory, m));
            }
        });

        EventHandler<ActionEvent> eventFly = e -> GamestateController.changeScene(Gamestate.gs.FLY, builder);
        flyButton.setOnAction(eventFly);

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
        Label componentDesc;
        SpaceshipBuilder builder;

        public MountClickHandler(Pane p, Label l, SpaceshipBuilder b, SpaceshipComponentFactory factory, Mount m) {
            chosenComponentF = factory;
            myMount = m;
            spaceshipView = p;
            componentDesc = l;
            builder = b;
        }

        @Override
        public void handle(MouseEvent actionEvent) {
            SpaceshipComponent chosenComponent = null;
            try {
                if (chosenComponentF.getComponentClass() == LandingStrutsComponent.class || chosenComponentF.getComponentClass() == RadialDecouplerComponent.class) {
                    Direction dir;
                    if (myMount.getDirection() == Mount.Direction.RIGHT)
                        dir = Direction.RIGHT;
                    else
                        dir = Direction.LEFT;
                    chosenComponent = chosenComponentF.getInstance(dir);
                } else {
                    chosenComponent = chosenComponentF.getInstance();
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }

            if (chosenComponent == null) {
                System.out.println("Something's wrong...");
                return;
            }

            ImageView componentImage = chosenComponent.getImage();
            positionComponent(chosenComponent, componentImage);
            spaceshipView.getChildren().add(componentImage);

            final SpaceshipComponent chosenComponentCpy = chosenComponent;

            componentImage.setOnMouseClicked(mouseEvent -> componentDesc.setText(chosenComponentCpy.getDescription()));
            componentDesc.setText(chosenComponent.getDescription());

            shiftView(chosenComponent);

            ArrayList<Node> toRemove = new ArrayList<>();
            for (Node n : spaceshipView.getChildren()) {
                if (n instanceof MountImg) {
                    toRemove.add(n);
                }
            }
            spaceshipView.getChildren().removeAll(toRemove.toArray(new Node[toRemove.size()]));

            fillMounts(chosenComponent);

            builder.addComponent(chosenComponent, myMount);
        }

        private void fillMounts(SpaceshipComponent chosenComponent) {
            myMount.setUsed(true, chosenComponent);
            switch (myMount.getDirection()) {
                case LOWER:
                    chosenComponent.getUpperMount().setUsed(true, myMount.getParent());
                    break;
                case UPPER:
                    chosenComponent.getLowerMount().setUsed(true, myMount.getParent());
                    break;
                case LEFT:
                    chosenComponent.getRightMount().setUsed(true, myMount.getParent());
                    break;
                case RIGHT:
                    chosenComponent.getLeftMount().setUsed(true, myMount.getParent());
            }
        }

        private void shiftView(SpaceshipComponent chosenComponent) {
            if (myMount.getPositionY() - chosenComponent.getHeight() - myMount.getHeight() <= 10) {
                for (Node n : spaceshipView.getChildren()) {
                    n.setLayoutY(n.getLayoutY() + chosenComponent.getHeight());
                }
            }

            if ((myMount.getDirection() == Mount.Direction.RIGHT || myMount.getDirection() == Mount.Direction.LEFT)
                    && myMount.getPositionX() - chosenComponent.getWidth() - myMount.getWidth() <= 10) {
                for (Node n : spaceshipView.getChildren()) {
                    n.setLayoutX(n.getLayoutX() + chosenComponent.getWidth());
                }
            }
        }

        private void positionComponent(SpaceshipComponent chosenComponent, ImageView componentImage) {
            if (myMount.getDirection() == Mount.Direction.UPPER) {
                componentImage.setLayoutX(myMount.getPositionX() + (myMount.getWidth() - chosenComponent.getWidth())/2 - 40);
                componentImage.setLayoutY(myMount.getPositionY() - chosenComponent.getHeight() + 60 -40);
            } else if (myMount.getDirection() == Mount.Direction.LEFT) {
                componentImage.setLayoutX(myMount.getPositionX() - chosenComponent.getWidth() + 60 - 40);
                componentImage.setLayoutY(myMount.getPositionY() - 40);
            } else if (myMount.getDirection() == Mount.Direction.RIGHT) {
                componentImage.setLayoutX(myMount.getPositionX() - 10 - 40);
                componentImage.setLayoutY(myMount.getPositionY() - 40);
            } else {
                componentImage.setLayoutX(myMount.getPositionX() + (myMount.getWidth() - chosenComponent.getWidth())/2 - 40);
                componentImage.setLayoutY(myMount.getPositionY() - 10 - 40);
            }

            if (chosenComponentF.getComponentClass() == LandingStrutsComponent.class) {
                componentImage.setLayoutY(componentImage.getLayoutY() + 100);
            }
        }
    }
}

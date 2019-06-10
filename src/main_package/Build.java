package main_package;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import ship.*;
import ship.components.*;
import utility.Boundaries;
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
    private VBox componentProperties;
    private Label componentDesc;

    private int nextComponentPosition;
    private Rectangle nextComponent;

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

        componentProperties = new VBox();
        componentProperties.setLayoutX(610);
        componentProperties.setLayoutY(400);
        componentDesc = new Label();
        componentDesc.setText("");
        componentDesc.setLayoutX(610);
        componentDesc.setLayoutY(400);

        componentList = new ListView<>();
        componentList.setItems(availableComponents);

        componentList.setLayoutX(600);

        root.setPrefSize(850, 850);
        nextComponentPosition = 800;
        root.getChildren().addAll(componentList, componentProperties);

        Button flyButton = new Button("Let's fly!");
        // TODO: event handler for this one

        root.getChildren().add(flyButton);

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

                img.setOnMouseClicked(new MountClickHandler(spaceshipView, componentProperties, builder, factory, m));
            }
        });

        EventHandler<ActionEvent> eventFly = e -> GamestateController.changeScene(Gamestate.gs.FLY, builder);
        flyButton.setOnAction(eventFly);
    }

    @Override
    public void update() {

    }

    @Override
    public Pane getRoot() {
        return root;
    }

    private static class MountClickHandler implements EventHandler<MouseEvent> {

        static class InvalidPositionException extends Exception {  }

        SpaceshipComponentFactory chosenComponentF;
        Mount myMount;
        Pane spaceshipView;
        VBox componentProperties;
        SpaceshipBuilder builder;

        public MountClickHandler(Pane p, VBox properties, SpaceshipBuilder b, SpaceshipComponentFactory factory, Mount m) {
            chosenComponentF = factory;
            myMount = m;
            spaceshipView = p;
            componentProperties = properties;
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
            } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
                e.printStackTrace();
            }

            if (chosenComponent == null) {
                System.out.println("Something's wrong...");
                return;
            }

            ImageView componentImage = chosenComponent.getImage();
            positionComponent(chosenComponent, componentImage);

            final SpaceshipComponent chosenComponentCpy = chosenComponent;

            componentImage.setOnMouseClicked(mouseEvent -> {
                componentProperties.getChildren().clear();
                componentProperties.getChildren().add(new Label(chosenComponentCpy.getDescription()));
                if (chosenComponentCpy instanceof ActiveComponent) {
                    TextField numInput =
                            new TextField(Integer.toString(((ActiveComponent) chosenComponentCpy).getActivationNumber()));
                    componentProperties.getChildren().add(new HBox(new Label("Activation number: "), numInput));
                    Button saveProperties = new Button("Save");
                    componentProperties.getChildren().add(saveProperties);
                    saveProperties.setOnAction(actionEvent1 ->
                            ((ActiveComponent) chosenComponentCpy).setActivationNumber(Integer.parseInt(numInput.getCharacters().toString())));
                }
                Button deleteComponent = new Button("Delete component");
                componentProperties.getChildren().add(deleteComponent);
                deleteComponent.setOnAction(e -> {
                    if (chosenComponentCpy.getUpperMount() != null && chosenComponentCpy.getUpperMount().isUsed()) {
                        chosenComponentCpy.getUpperMount().getAttached().getLowerMount().setUsed(false, null);
                    }
                    if (chosenComponentCpy.getLowerMount() != null && chosenComponentCpy.getLowerMount().isUsed()) {
                        chosenComponentCpy.getLowerMount().getAttached().getUpperMount().setUsed(false, null);
                    }
                    if (chosenComponentCpy.getRightMount() != null && chosenComponentCpy.getRightMount().isUsed()) {
                        chosenComponentCpy.getRightMount().getAttached().getLeftMount().setUsed(false, null);
                    }
                    if (chosenComponentCpy.getLeftMount() != null && chosenComponentCpy.getLeftMount().isUsed()) {
                        chosenComponentCpy.getLeftMount().getAttached().getRightMount().setUsed(false, null);
                    }

                    spaceshipView.getChildren().remove(componentImage);
                    builder.removeComponent(chosenComponentCpy);
                    componentProperties.getChildren().clear();
                });
            });

            ArrayList<Node> toRemove = new ArrayList<>();
            for (Node n : spaceshipView.getChildren()) {
                if (n instanceof MountImg) {
                    toRemove.add(n);
                }
            }
            spaceshipView.getChildren().removeAll(toRemove.toArray(new Node[toRemove.size()]));

            try {
                fillMounts(chosenComponent);
            } catch (InvalidPositionException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Invalid component position");
                alert.showAndWait();
                return;
            }

            spaceshipView.getChildren().add(componentImage);
            shiftView(chosenComponent);

            builder.addComponent(chosenComponent);
        }

        private void fillMounts(SpaceshipComponent chosenComponent) throws InvalidPositionException {
            SpaceshipComponent componentsToAttatch [] = { null, null, null, null };
            Boundaries myBoundaries = chosenComponent.getBoundaries();

            for (SpaceshipComponent comp : builder.getComponents()) {
                Boundaries compBoundaries = comp.getBoundaries();
                boolean intersectsVertically =
                        (myBoundaries.getUpper() < compBoundaries.getLower() && myBoundaries.getUpper() > compBoundaries.getUpper())
                        || (myBoundaries.getLower() < compBoundaries.getLower() && myBoundaries.getLower() > compBoundaries.getUpper())
                        || (compBoundaries.getUpper() < myBoundaries.getLower() && compBoundaries.getUpper() > myBoundaries.getUpper())
                        || (compBoundaries.getLower() < myBoundaries.getLower() && compBoundaries.getLower() > myBoundaries.getUpper());
                boolean intersectsHorizontally =
                        (myBoundaries.getLeft() < compBoundaries.getRight() && myBoundaries.getLeft() > compBoundaries.getLeft())
                        || (myBoundaries.getRight() < compBoundaries.getRight() && myBoundaries.getRight() > compBoundaries.getLeft())
                        || (compBoundaries.getRight() < myBoundaries.getRight() && compBoundaries.getRight() > myBoundaries.getLeft())
                        || (compBoundaries.getLeft() < myBoundaries.getRight() && compBoundaries.getLeft() > myBoundaries.getLeft());
                boolean fitsVertically = myBoundaries.getUpper() == compBoundaries.getUpper()
                        && myBoundaries.getLower() == compBoundaries.getLower();
                if (chosenComponent instanceof LandingStrutsComponent) {
                    fitsVertically = myBoundaries.getUpper() - 100 == compBoundaries.getUpper()
                            && myBoundaries.getLower() - 100 == compBoundaries.getLower();
                }
                boolean fitsHorizontally = (myBoundaries.getLeft()+myBoundaries.getRight())/2 == (compBoundaries.getLeft()+compBoundaries.getRight())/2;

                if ((intersectsHorizontally && intersectsVertically)
                        || (fitsHorizontally && intersectsVertically)
                        || (intersectsHorizontally && fitsVertically))
                    throw new InvalidPositionException();

                if (fitsVertically && myBoundaries.getLeft() == compBoundaries.getRight()
                        && chosenComponent.getLeftMount() != null && comp.getRightMount() != null)
                    componentsToAttatch [1] = comp;
                else if (fitsVertically && myBoundaries.getRight() == compBoundaries.getLeft()
                        && chosenComponent.getRightMount() != null && comp.getLeftMount() != null)
                    componentsToAttatch [3] = comp;
                else if (fitsHorizontally && myBoundaries.getUpper() == compBoundaries.getLower()
                        && chosenComponent.getUpperMount() != null && chosenComponent.getLowerMount() != null)
                    componentsToAttatch [0] = comp;
                else if (fitsHorizontally && myBoundaries.getLower() == compBoundaries.getUpper()
                        && chosenComponent.getLowerMount() != null && comp.getUpperMount() != null)
                    componentsToAttatch [2] = comp;
            }

            if (componentsToAttatch [0] != null) {
                componentsToAttatch [0].getLowerMount().setUsed(true, chosenComponent);
                chosenComponent.getUpperMount().setUsed(true, componentsToAttatch [0]);
            }
            if (componentsToAttatch [1] != null) {
                componentsToAttatch [1].getRightMount().setUsed(true, chosenComponent);
                chosenComponent.getLeftMount().setUsed(true, componentsToAttatch [1]);
            }
            if (componentsToAttatch [2] != null) {
                componentsToAttatch [2].getUpperMount().setUsed(true, chosenComponent);
                chosenComponent.getLowerMount().setUsed(true, componentsToAttatch [2]);
            }
            if (componentsToAttatch [3] != null) {
                componentsToAttatch [3].getLeftMount().setUsed(true, chosenComponent);
                chosenComponent.getRightMount().setUsed(true, componentsToAttatch [3]);
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

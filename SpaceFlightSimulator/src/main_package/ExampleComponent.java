package main_package;

import javafx.scene.image.Image;

public class ExampleComponent implements SpaceshipComponent {
    private String name;
    private Image img;

    public ExampleComponent(String name) {
        this.name = name;
        img = new Image("file:images/example_component.png");
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getHeight() {
        return 50;
    }

    @Override
    public int getMass() {
        return 150;
    }

    @Override
    public Image getImage() {
        return img;
    }

    @Override
    public String toString() {
        return getName();
    }
}

package main_package;

import javafx.scene.image.Image;

public class CircularDecouplerComponent implements SpaceshipComponent {
    private Image img;

    public CircularDecouplerComponent() {
        img = new Image("file:images/circularDec.png");
    }

    @Override
    public String getName() {
        return "Circular Decoupler";
    }

    @Override
    public int getHeight() {
        return 40;
    }

    @Override
    public int getWidth() {
        return 200;
    }

    @Override
    public int getMass() {
        return 0;
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

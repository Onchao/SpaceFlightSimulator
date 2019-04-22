package main_package;

import javafx.scene.image.Image;

public class RadialDecouplerComponent implements SpaceshipComponent {
    public enum Direction {
        RIGHT, LEFT
    }

    private Image img;
    private Direction direction;

    public RadialDecouplerComponent(Direction dir) {
        img = new Image("file:images/radialDec.png");
        direction = dir;
    }

    public static String getName() {
        return "Radial Decoupler";
    }

    @Override
    public int getHeight() {
        return 200;
    }

    @Override
    public int getWidth() {
        return 40;
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

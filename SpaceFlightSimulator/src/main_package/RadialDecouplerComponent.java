package main_package;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class RadialDecouplerComponent extends SpaceshipComponent {
    public enum Direction {
        RIGHT, LEFT
    }

    private ImageView img;
    private Direction direction;

    public RadialDecouplerComponent(Direction dir) {
        direction = dir;

        img = new ImageView(new Image("file:images/radialDec.png")); // TODO: flip image if necessary

        super.leftMount = new Mount(-60, 0, 200, 50, Mount.Direction.LEFT, this);
        super.rightMount = new Mount(100, 0, 200, 50, Mount.Direction.RIGHT, this);
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
    public ImageView getImage() {
        return img;
    }

    @Override
    public String toString() {
        return getName();
    }
}

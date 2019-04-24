package main_package;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class LandingStrutsComponent extends SpaceshipComponent {

    enum Direction {
        LEFT, RIGHT
    }

    private ImageView img;
    private Direction direction;

    public LandingStrutsComponent(Direction dir) {
        direction = dir;

        img = new ImageView(new Image("file:images/strut.png")); // TODO: flip image if necessary

        if (direction == Direction.LEFT)
            super.rightMount = new Mount(90, 0, 200, 50, Mount.Direction.RIGHT, this);
        else
            super.leftMount = new Mount(-60, 0, 200, 50, Mount.Direction.RIGHT, this);
    }

    public static String getName() {
        return "Landing Strut";
    }

    @Override
    public int getHeight() {
        return 200;
    }

    @Override
    public int getWidth() {
        return 80;
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

package main_package;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class LandingStrutsComponent extends SpaceshipComponent {
    private ImageView img;
    private Direction direction;

    public LandingStrutsComponent(Direction dir) {
        direction = dir;

        if (direction == Direction.LEFT) {
            img = new ImageView(new Image("file:images/strut.png"));
            super.rightMount = new Mount(130, 40, 200, 50, Mount.Direction.RIGHT, this);
        } else {
            img = new ImageView(new Image("file:images/strut_r.png"));
            super.leftMount = new Mount(-20, 40, 200, 50, Mount.Direction.RIGHT, this);
        }
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

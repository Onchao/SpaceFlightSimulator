package main_package;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class CircularDecouplerComponent extends SpaceshipComponent {
    private ImageView img;

    public CircularDecouplerComponent() {
        img = new ImageView(new Image("file:images/circularDec.png"));

        super.lowerMount = new Mount(40, 140, 50, 200, Mount.Direction.LOWER, this);
        super.upperMount = new Mount(40, -20, 50, 200, Mount.Direction.UPPER, this);
    }

    public static String getName() {
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
        return 1;
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

package main_package;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class FuelTankComponent extends SpaceshipComponent {
    private ImageView img;

    public FuelTankComponent() {
        img = new ImageView(new Image("file:images/tank.png"));

        super.leftMount = new Mount(-60, 0, 200, 50, Mount.Direction.LEFT, this);
        super.rightMount = new Mount(210, 0, 200, 50, Mount.Direction.RIGHT, this);
        super.upperMount = new Mount(0, -60, 50, 200, Mount.Direction.UPPER, this);
        super.lowerMount = new Mount(0, 210, 50, 200, Mount.Direction.LOWER, this);
    }

    public static String getName() {
        return "Fuel Tank";
    }

    @Override
    public int getHeight() {
        return 200;
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
    public ImageView getImage() {
        return img;
    }

    @Override
    public String toString() {
        return getName();
    }
}

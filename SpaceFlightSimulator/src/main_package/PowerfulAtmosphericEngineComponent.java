package main_package;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class PowerfulAtmosphericEngineComponent extends SpaceshipComponent {
    private ImageView img;

    public PowerfulAtmosphericEngineComponent() {
        img = new ImageView(new Image("file:images/atmEngine.png"));

        super.upperMount = new Mount(0, -60, 50, 200, Mount.Direction.UPPER, this);
        super.lowerMount = new Mount(0, 210, 50, 200, Mount.Direction.LOWER, this);
    }

    public static String getName() {
        return "Powerful Atmospheric Engine";
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

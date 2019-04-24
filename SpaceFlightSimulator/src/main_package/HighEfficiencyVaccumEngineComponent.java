package main_package;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class HighEfficiencyVaccumEngineComponent extends SpaceshipComponent {
    private ImageView img;

    public HighEfficiencyVaccumEngineComponent() {
        img = new ImageView(new Image("file:images/vacEngine.png"));

        super.upperMount = new Mount(0, -60, 50, 200, Mount.Direction.UPPER, this);
        super.lowerMount = new Mount(0, 90, 50, 200, Mount.Direction.LOWER, this);
    }

    public static String getName() {
        return "High Efficiency Vaccum Engine";
    }

    @Override
    public int getHeight() {
        return 80;
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

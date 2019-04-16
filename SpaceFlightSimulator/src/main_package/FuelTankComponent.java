package main_package;

import javafx.scene.image.Image;

public class FuelTankComponent implements SpaceshipComponent {
    private Image img;

    public FuelTankComponent() {
        img = new Image("file:images/tank.png");
    }

    @Override
    public String getName() {
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
    public Image getImage() {
        return img;
    }

    @Override
    public String toString() {
        return getName();
    }
}

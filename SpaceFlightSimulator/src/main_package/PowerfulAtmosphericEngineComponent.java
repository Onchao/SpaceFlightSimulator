package main_package;

import javafx.scene.image.Image;

public class PowerfulAtmosphericEngineComponent implements SpaceshipComponent {
    private Image img;

    public PowerfulAtmosphericEngineComponent() {
        img = new Image("file:images/atmEngine.png");
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
    public Image getImage() {
        return img;
    }

    @Override
    public String toString() {
        return getName();
    }
}

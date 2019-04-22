package main_package;

import javafx.scene.image.Image;

public class HighEfficiencyVaccumEngineComponent implements SpaceshipComponent {
    private Image img;

    public HighEfficiencyVaccumEngineComponent() {
        img = new Image("file:images/vacEngine.png");
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
    public Image getImage() {
        return img;
    }

    @Override
    public String toString() {
        return getName();
    }
}

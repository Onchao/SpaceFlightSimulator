package main_package;

import javafx.scene.image.Image;

public class LandingStrutsComponent implements SpaceshipComponent {
    private Image img;

    public LandingStrutsComponent() {
        img = new Image("file:images/strut.png");
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
    public Image getImage() {
        return img;
    }

    @Override
    public String toString() {
        return getName();
    }
}

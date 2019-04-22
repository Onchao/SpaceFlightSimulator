package main_package;

import javafx.scene.image.Image;

public class ParachuteComponent implements SpaceshipComponent {
    private Image img;

    public ParachuteComponent() {
        img = new Image("file:images/parachute.png");
    }

    public static String getName() {
        return "Parachute";
    }

    @Override
    public int getHeight() {
        return 40;
    }

    @Override
    public int getWidth() {
        return 40;
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

package main_package;

import javafx.scene.image.Image;

public class CrewCapsuleComponent implements SpaceshipComponent {
    private Image img;

    public CrewCapsuleComponent() {
        img = new Image("file:images/capsule.png");
    }

    @Override
    public String getName() {
        return "Crew Capsule";
    }

    @Override
    public int getHeight() {
        return 160;
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

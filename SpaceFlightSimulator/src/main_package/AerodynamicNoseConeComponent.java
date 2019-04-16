package main_package;

import javafx.scene.image.Image;

public class AerodynamicNoseConeComponent implements SpaceshipComponent {
    private Image img;

    public AerodynamicNoseConeComponent() {
        img = new Image("file:images/cone.png");
    }

    @Override
    public String getName() {
        return "Aerodynamic Nose Cone";
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

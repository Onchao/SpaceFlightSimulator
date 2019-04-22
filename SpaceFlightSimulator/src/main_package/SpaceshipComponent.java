package main_package;

import javafx.scene.image.Image;

public interface SpaceshipComponent {
    int getHeight();
    int getWidth();
    int getMass();

    default Mount getUpperMount() {
        return null;
    }

    default Mount getLowerMount() {
        return null;
    }

    default Mount getRightMount() {
        return null;
    }

    default Mount getLeftMount() {
        return null;
    }

    Image getImage();
}

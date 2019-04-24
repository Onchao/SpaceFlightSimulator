package main_package;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ParachuteComponent extends SpaceshipComponent {
    private ImageView img;

    public ParachuteComponent() {
        img = new ImageView(new Image("file:images/parachute.png"));

        super.lowerMount = new Mount(0, -50, 50, 200, Mount.Direction.LOWER, this);
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
    public ImageView getImage() {
        return img;
    }

    @Override
    public String toString() {
        return getName();
    }
}

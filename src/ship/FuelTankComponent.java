package ship;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import utility.Mount;

public class FuelTankComponent extends SpaceshipComponent {
    private ImageView img;

    public FuelTankComponent() {
        img = new ImageView(new Image("file:images/tank.png"));

        super.leftMount = new Mount(-20, 40, 200, 50, Mount.Direction.LEFT, this);
        super.rightMount = new Mount(250, 40, 200, 50, Mount.Direction.RIGHT, this);
        super.upperMount = new Mount(40, -20, 50, 200, Mount.Direction.UPPER, this);
        super.lowerMount = new Mount(40, 250, 50, 200, Mount.Direction.LOWER, this);
    }

    public static String getName() {
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
        return 1;
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

package ship;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import utility.Direction;
import utility.Mount;

public class RadialDecouplerComponent extends SpaceshipComponent {
    private ImageView img;
    private Direction direction;

    public RadialDecouplerComponent(Direction dir) {
        direction = dir;

        if (direction == Direction.LEFT)
            img = new ImageView(new Image("file:images/radialDec.png"));
        else
            img = new ImageView(new Image("file:images/radialDec_r.png"));

        super.leftMount = new Mount(-20, 40, 200, 50, Mount.Direction.LEFT, this);
        super.rightMount = new Mount(90, 40, 200, 50, Mount.Direction.RIGHT, this);
    }

    public static String getName() {
        return "Radial Decoupler";
    }

    @Override
    public int getHeight() {
        return 200;
    }

    @Override
    public int getWidth() {
        return 40;
    }

    @Override
    public int getMass() {
        return 2000;
    }

    @Override
    public ImageView getImage() {
        return img;
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public double getDragCoefficient() {
        return 1.0;
    }

    @Override
    public double getFrontAvgSurface(){
        return 2*(0.8*1.6 + 0.8*4 + 1.6*4)/4;
    }

    public Direction getDirection() {
        return direction;
    }
}

package ship;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import utility.Mount;

public class CircularDecouplerComponent extends SpaceshipComponent {
    private ImageView img;

    public CircularDecouplerComponent() {
        img = new ImageView(new Image("file:images/circularDec.png"));

        super.lowerMount = new Mount(40, 140, 50, 200, Mount.Direction.LOWER, this);
        super.upperMount = new Mount(40, -20, 50, 200, Mount.Direction.UPPER, this);
    }

    public static String getName() {
        return "Circular Decoupler";
    }

    @Override
    public int getHeight() {
        return 40;
    }

    @Override
    public int getWidth() {
        return 200;
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
        return 2*Math.PI*2*(2+0.8)/4;
    }
}

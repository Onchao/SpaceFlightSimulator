package ship.components;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import utility.Mount;

public class AerodynamicNoseConeComponent extends SpaceshipComponent {
    private ImageView img;

    public AerodynamicNoseConeComponent() {
        img = new ImageView(new Image(getClass().getResourceAsStream("/images/cone.png")));
        super.lowerMount = new Mount(40, -20, 50, 200, Mount.Direction.LOWER, this);
    }

    public static String getName() {
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
        return 0.5;
    }

    @Override
    public double getFrontAvgSurface(){
        return Math.PI*2*2;
    }
}

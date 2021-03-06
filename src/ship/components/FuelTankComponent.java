package ship.components;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import utility.Mount;

public class FuelTankComponent extends SpaceshipComponent {
    private ImageView img;
    private double fuelState = 100*1000;

    public FuelTankComponent() {
        img = new ImageView(new Image(getClass().getResourceAsStream("/images/tank.png")));

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

    // fuel 100*1000 kg
    @Override
    public int getMass() {
        return 10*1000 + (int) fuelState; // [kg]
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
        return Math.PI*2*2;
    }

    public double getFuelState() {
        return fuelState;
    }

    public void setFuelState(double newState) {
        fuelState = newState;
    }
}

package main_package;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class PowerfulAtmosphericEngineComponent extends SpaceshipComponent implements Engine {
    private ImageView img;

    private double thrust = 8000*1000; // N
    private double maxThrust;

    public PowerfulAtmosphericEngineComponent() {
        img = new ImageView(new Image("file:images/atmEngine.png"));

        super.upperMount = new Mount(40, -20, 50, 200, Mount.Direction.UPPER, this);
        super.lowerMount = new Mount(40, 250, 50, 200, Mount.Direction.LOWER, this);
    }

    public static String getName() {
        return "Powerful Atmospheric Engine";
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

    @Override
    public double getThrust() {
        return thrust;
    }

    @Override
    public double maxThrust() {
        return maxThrust;
    }

    @Override
    public void setThrust(double val) {
        if (val > maxThrust) val = maxThrust;
        thrust = val;
    }
}

package ship;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import utility.Mount;

public class PowerfulAtmosphericEngineComponent extends SpaceshipComponent implements Engine {
    private ImageView img;

    private double maxThrust = 20000.0*1000; // N;

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
        return 70*1000;
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
        return maxThrust;
    }

    @Override
    public double maxThrust() {
        return maxThrust;
    }

    @Override
    public void setThrust(double val) {
        maxThrust = val;
    }

    @Override
    public double getDragCoefficient() {
        return 1.0;
    }

    @Override
    public double getFrontAvgSurface(){
        return 2*Math.PI*1.2*(1.2+4)/4;
    }
}
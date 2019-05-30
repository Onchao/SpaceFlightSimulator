package ship;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import utility.Mount;

public class HighEfficiencyVaccumEngineComponent extends SpaceshipComponent implements Engine {
    private ImageView img;

    private double maxThrust = 1000.0*1000*1000;

    public HighEfficiencyVaccumEngineComponent() {
        img = new ImageView(new Image("file:images/vacEngine.png"));

        super.upperMount = new Mount(40, -20, 50, 200, Mount.Direction.UPPER, this);
        super.lowerMount = new Mount(40, 130, 50, 200, Mount.Direction.LOWER, this);
    }

    public static String getName() {
        return "High Efficiency Vaccum Engine";
    }

    @Override
    public int getHeight() {
        return 80;
    }

    @Override
    public int getWidth() {
        return 200;
    }

    @Override
    public int getMass() {
        return 20*1000;
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
}

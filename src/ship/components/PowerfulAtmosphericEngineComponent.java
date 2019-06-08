package ship.components;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import utility.Mount;

import java.util.List;

public class PowerfulAtmosphericEngineComponent extends SpaceshipComponent implements Engine, ActiveComponent {
    private ImageView img;

    private double maxThrust = 20000.0*1000; // N;

    private final double fuelConsumption = 1600;  // kg/s
    private boolean isActive;
    private int activationNumber = 0;

    private List<List<FuelTankComponent>> availableTanks;

    private void detectTanks() {
        //TODO
    }

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

    @Override
    public double getFuelConsumption() {
        return fuelConsumption;
    }

    @Override
    public double burnFuel(double amount) {
        //TODO
        return amount;
    }

    @Override
    public ComponentAction activate() {
        isActive = true;
        detectTanks();
        return new ComponentAction(ComponentAction.ActionType.ACTIVATE_ENGINE, stageNumber);
    }

    @Override
    public boolean isActive() {
        return isActive;
    }

    @Override
    public void setActivationNumber(int num) {
        activationNumber = num;
    }

    @Override
    public int getActivationNumber() {
        return activationNumber;
    }
}
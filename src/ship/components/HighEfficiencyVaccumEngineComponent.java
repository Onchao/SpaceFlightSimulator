package ship.components;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import utility.Mount;

public class HighEfficiencyVaccumEngineComponent extends SpaceshipComponent implements Engine, ActiveComponent {
    private ImageView img;

    private final double maxThrust = 6000.0*1000;
    private double curThrust = 0.0;
    private final double maxFuelConsumption = 1600.0;
    private boolean isActive;
    private int activationNumber = 0;

    private void detectTanks() {
        //TODO
    }

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
        return curThrust;
    }

    @Override
    public double maxThrust() {
        return maxThrust;
    }

    @Override
    public void setThrust(double val) {
        if (val > maxThrust) return;
        curThrust = val;
    }

    @Override
    public double getFuelConsumption() {
        return maxFuelConsumption * (getThrust()/maxThrust());
    }

    @Override
    public double burnFuel(double amount) {
        //TODO
        return amount;
    }

    @Override
    public double getDragCoefficient() {
        return 1.0;
    }

    @Override
    public double getFrontAvgSurface(){
        return 2*Math.PI*1.2*(1.2+1.6)/4;
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

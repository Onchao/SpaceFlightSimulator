package ship.components;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import utility.Mount;

public class CircularDecouplerComponent extends SpaceshipComponent implements ActiveComponent {
    private ImageView img;
    private boolean isActive = false;
    private int activationNumber = 0;

    public CircularDecouplerComponent() {
        img = new ImageView(new Image(getClass().getResourceAsStream("/images/circularDec.png")));

        super.lowerMount = new Mount(40, 90, 50, 200, Mount.Direction.LOWER, this);
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

    @Override
    public ComponentAction activate() {
        isActive = true;
        return new ComponentAction(ComponentAction.ActionType.DETACH_STAGE, stageNumber);
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

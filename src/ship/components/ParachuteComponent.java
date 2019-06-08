package ship.components;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import utility.Mount;

public class ParachuteComponent extends SpaceshipComponent implements ActiveComponent {
    private ImageView img;
    private boolean isActive;
    private int activationNumber = 0;

    public ParachuteComponent() {
        img = new ImageView(new Image("file:images/parachute.png"));

        super.lowerMount = new Mount(40, -10, 50, 200, Mount.Direction.LOWER, this);
    }

    public static String getName() {
        return "Parachute";
    }

    @Override
    public int getHeight() {
        return 40;
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
        return 0;
    }

    @Override
    public double getFrontAvgSurface(){
        return 0;
    }

    @Override
    public ComponentAction activate() {
        isActive = true;
        // TODO
        return new ComponentAction(ComponentAction.ActionType.OPEN_PARACHUTE, stageNumber);
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

package ship.components;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import utility.Mount;

public class ParachuteComponent extends SpaceshipComponent implements ActiveComponent {
    private ImageView img;
    private boolean isActive;
    private int activationNumber = 0;
    private int height = 40;
    private int width = 40;

    public ParachuteComponent() {
        img = new ImageView(new Image(getClass().getResourceAsStream("/images/parachute.png")));

        super.lowerMount = new Mount(40, -10, 50, 200, Mount.Direction.LOWER, this);
    }

    public static String getName() {
        return "Parachute";
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public int getWidth() {
        return width;
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
        if (isActive) return 1.75;
        return 0;
    }

    @Override
    public double getFrontAvgSurface(){
        if (isActive) return Math.PI*8*8;
        return 0.1;
    }

    @Override
    public ComponentAction activate() {
        isActive = true;
        double oldX = getImage().getLayoutX();
        double oldY = getImage().getLayoutY();
        img = null;
        img = new ImageView(new Image(getClass().getResourceAsStream("/images/parachute2.png")));
        img.setLayoutX(oldX - 360);
        img.setLayoutY(oldY - 740);
        height = 260;
        width = 720;
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

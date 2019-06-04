package ship;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import utility.Mount;

public class CrewCapsuleComponent extends SpaceshipComponent {
    private ImageView img;

    public CrewCapsuleComponent() {
        img = new ImageView(new Image("file:images/capsule.png"));

        super.upperMount = new Mount(40, -20, 50, 200, Mount.Direction.UPPER, this);
        super.lowerMount = new Mount(40, 210, 50, 200, Mount.Direction.LOWER, this);
    }

    public static String getName() {
        return "Crew Capsule";
    }

    @Override
    public int getHeight() {
        return 160;
    }

    @Override
    public int getWidth() {
        return 200;
    }

    @Override
    public int getMass() {
        return 10*1000;
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
        return Math.PI*2*(2+Math.sqrt(4*4+2*2))/4;
    }
}

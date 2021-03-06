package ship.components;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ExampleComponent extends SpaceshipComponent {
    private String name;
    private ImageView img;

    public ExampleComponent() {
        img = new ImageView(new Image(getClass().getResourceAsStream("/images/example_component.png")));
    }

    public static String getName() {
        return "Example";
    }

    @Override
    public int getHeight() {
        return 50;
    }

    @Override
    public int getWidth() {
        return 100;
    }

    @Override
    public int getMass() {
        return 150;
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
        return 100000;
    }

    @Override
    public double getFrontAvgSurface(){
        return 100000;
    }
}

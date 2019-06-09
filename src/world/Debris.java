package world;

import javafx.scene.Group;
import ship.Spaceship;
import ship.components.SpaceshipComponent;

import java.util.List;


//TODO
public class Debris {
    private List <SpaceshipComponent> components;
    private double velocityX;
    private double velocityY;
    private Group drawable;
    private int numIterations = 0;

    public Debris (List<SpaceshipComponent> components, double vX, double vY) {
        this.components = components;
        velocityX = vX;
        velocityY = vY;
        drawable = new Group();
        for (SpaceshipComponent comp : components) {
            drawable.getChildren().add(comp.getImage());
        }
    }

    public Group getDrawable() {
        return drawable;
    }

    public double getTotalMass () {
        double ret = 0;
        for (SpaceshipComponent comp : components) {
            ret += comp.getMass();
        }
        return ret;
    }

    public void update () {
        ++ numIterations;
        //TODO
    }

    public int getNumIterations() {
        return numIterations;
    }
}

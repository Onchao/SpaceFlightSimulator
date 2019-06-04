package ship;

import javafx.scene.image.ImageView;
import utility.Mount;
import utility.Point;

import java.util.ArrayList;
import java.util.List;

public abstract class SpaceshipComponent {
    protected Mount upperMount = null;
    protected Mount lowerMount = null;
    protected Mount rightMount = null;
    protected Mount leftMount = null;

    protected int stageNumber;

    public abstract int getHeight();
    public abstract int getWidth();
    public abstract int getMass();
    public abstract double getDragCoefficient();

    public Mount getUpperMount() {
        return upperMount;
    }

    public Mount getLowerMount() {
        return lowerMount;
    }

    public Mount getRightMount() {
        return rightMount;
    }

    public Mount getLeftMount() {
        return leftMount;
    }

    public void setStageNumber (int n) {
        stageNumber = n;
    }

    public int getStageNumber() {
        return stageNumber;
    }

    public abstract ImageView getImage();

    public double getCenterOfMassX () {
        return getImage().getLayoutX() + 40 + getWidth()/2;
    }

    public double getCenterOfMassY () {
        return getImage().getLayoutY() + 40 + getHeight()/2;
    }

    public double getMomentOfInertiaX () {
        return getWidth()*getHeight()*getHeight()*getHeight()*getMass()/12;
    }

    public double getMomentOfInertiaY () {
        return getHeight()*getWidth()*getWidth()*getWidth()*getMass()/12;
    }

    public List<Point> getVertices () {
        List<Point> ret = new ArrayList<>();

        ret.add(new Point(getImage().getLayoutX() + 40, getImage().getLayoutY() + 40));
        ret.add(new Point(getImage().getLayoutX() + 40 + getWidth(), getImage().getLayoutY() + 40));
        ret.add(new Point(getImage().getLayoutX() + 40, getImage().getLayoutY() + 40 + getHeight()));
        ret.add(new Point(getImage().getLayoutX() + 40 + getWidth(), getImage().getLayoutY() + 40 + getHeight()));

        return ret;
    }

    public String getDescription() {
        StringBuilder builder = new StringBuilder();

        builder.append(toString());
        builder.append("\n\n");
        builder.append("Mass: ");
        builder.append(getMass());
        builder.append("kg\n");
        builder.append("Height: ");
        builder.append(getHeight()/50);
        builder.append("m\n");
        builder.append("Width: ");
        builder.append(getWidth()/50);
        builder.append("m\n");
        builder.append("Stage number: ");
        builder.append(getStageNumber());

        return builder.toString();
    }
}

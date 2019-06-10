package ship.components;

import javafx.scene.image.ImageView;
import utility.Boundaries;
import utility.Mount;
import utility.Point;

import java.util.ArrayList;
import java.util.List;

public abstract class SpaceshipComponent {
    private static int nextId = 0;
    private int id;
    protected Mount upperMount = null;
    protected Mount lowerMount = null;
    protected Mount rightMount = null;
    protected Mount leftMount = null;

    protected int stageNumber;
    protected int componentId;

    SpaceshipComponent () {
        id = nextId;
        nextId ++;
    }

    public abstract int getHeight();
    public abstract int getWidth();
    public abstract int getMass();
    public abstract double getDragCoefficient();
    public abstract double getFrontAvgSurface();
    public double getFrontAvgRadius(){
        return Math.sqrt(getFrontAvgSurface()/Math.PI);
    }

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

    public int getId () {
        return id;
    }

    public void setStageNumber (int n) {
        stageNumber = n;
    }

    public int getStageNumber() {
        return stageNumber;
    }

    public abstract ImageView getImage();

    public Point getGeoCenter () {
        return new Point(getImage().getLayoutX() + getWidth()/2 + 40, getImage().getLayoutY() + getHeight()/2 + 40);
    }

    public double getCenterOfMassX () {
        //System.out.println(getImage().getLayoutX() + 40 + getWidth()/2);

        return getImage().getLayoutX() + 40 + getWidth()/2;
    }

    public double getCenterOfMassY () {
        //System.out.println(getImage().getLayoutY() + 40 + getHeight()/2);
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
        ret.add(new Point(getImage().getLayoutX() + 40 + getWidth(), getImage().getLayoutY() + 40 + getHeight()));
        ret.add(new Point(getImage().getLayoutX() + 40, getImage().getLayoutY() + 40 + getHeight()));

        return ret;
    }

    public Boundaries getBoundaries () {
        List<Point> vert = getVertices();

        return new Boundaries(vert.get(0).getY(), vert.get(3).getY(), vert.get(0).getX(), vert.get(1).getX());
    }

    public String getDescription() {
        StringBuilder builder = new StringBuilder();

        builder.append(toString());
        builder.append("\n\n");
        builder.append("Id: ");
        builder.append(id);
        builder.append('\n');
        builder.append("Mass: ");
        builder.append(getMass());
        builder.append("kg\n");
        builder.append("Height: ");
        builder.append(getHeight()/50);
        builder.append("m\n");
        builder.append("Width: ");
        builder.append(getWidth()/50);
        builder.append("m\n");

        return builder.toString();
    }
}

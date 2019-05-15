package main_package;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

import java.util.Collections;
import java.util.List;

public class Spaceship {
    private List<List<SpaceshipComponent>> stages;
    private Point origin;

    private Group drawable;

    public Spaceship (List<List<SpaceshipComponent>> stages) {
        this.stages = stages;

        origin = getCenterOfMass();

        drawable = new Group();

        for (List<SpaceshipComponent> stage : stages) {
            for (SpaceshipComponent comp : stage) {
                drawable.getChildren().add(comp.getImage());
            }
        }
    }

    public Node getDrawable() {
        return drawable;
    }

    public double getScale() {
        return drawable.getScaleX();
    }

    public void setScale(double scale) {
        drawable.setScaleX(scale);
        drawable.setScaleY(scale);
    }

    public void setPosition (double x, double y) {     //TODO: add angle here
        double dx = x - origin.getX();
        double dy = y - origin.getY();

        origin = new Point(x, y);

        for (List<SpaceshipComponent> stage : stages) {
            for (SpaceshipComponent comp : stage) {
                comp.getImage().setLayoutX(comp.getImage().getLayoutX() + dx);
                comp.getImage().setLayoutY(comp.getImage().getLayoutY() + dy);
            }
        }
    }

    public Point getPosition () {
        return origin;
    }

    public List<SpaceshipComponent> getStage (int i) {
        return Collections.unmodifiableList(stages.get(i));
    }

    public int getStageMass (int i) {
        int ret = 0;

        List<SpaceshipComponent> curStage = stages.get(i);

        for (SpaceshipComponent comp : curStage) {
            ret += comp.getMass();
        }

        return ret;
    }

    public Point getStageCenterOfMass (int i) {
        double xs = 0;
        double ys = 0;
        double d = 0;

        List<SpaceshipComponent> curStage = stages.get(i);

        for (int j = 0; j < curStage.size(); ++ j) {
            SpaceshipComponent comp = curStage.get(j);

            xs += comp.getCenterOfMassX() * comp.getMass();
            ys += comp.getCenterOfMassY() * comp.getMass();

            d += comp.getMass();
        }

        return new Point(xs/d, ys/d);
    }

    public Point getCenterOfMass () {
        double xs = 0;
        double ys = 0;
        double d = 0;

        for (int j = 0; j < stages.size(); ++ j) {
            Point scom = getStageCenterOfMass(j);
            int smass = getStageMass(j);

            xs += scom.getX() * smass;
            ys += scom.getY() * smass;
            d += smass;
        }

        return new Point(xs/d, ys/d);
    }

    public Point getThrustCenter () {
        double xs = 0;
        double ys = 0;
        double d = 0;

        for (List<SpaceshipComponent> stage : stages) {
            for (SpaceshipComponent comp : stage) {
                if (comp instanceof Engine) {
                    xs += ((Engine) comp).getThrust() * comp.getCenterOfMassX();
                    ys += ((Engine) comp).getThrust() * comp.getCenterOfMassY();
                    d += ((Engine) comp).getThrust();
                }
            }
        }

        return new Point(xs/d, ys/d);
    }
}

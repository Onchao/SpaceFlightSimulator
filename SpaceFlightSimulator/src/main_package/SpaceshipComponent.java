package main_package;

import javafx.scene.image.ImageView;

public abstract class SpaceshipComponent {
    protected Mount upperMount = null;
    protected Mount lowerMount = null;
    protected Mount rightMount = null;
    protected Mount leftMount = null;

    protected int stageNumber;

    public abstract int getHeight();
    public abstract int getWidth();
    public abstract int getMass();

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
}

package utility;

import ship.components.SpaceshipComponent;

public class Mount {
    public double getPositionX() {
        if (getParent() == null)
            return getOffsetX();
        return getParent().getImage().getLayoutX() + getOffsetX();
    }

    public double getPositionY() {
        if (getParent() == null)
            return getOffsetY();
        return getParent().getImage().getLayoutY() + getOffsetY();
    }

    public enum Direction {
        UPPER, LOWER, RIGHT, LEFT
    }

    private SpaceshipComponent parent;
    private SpaceshipComponent attached;

    private int offsetX;
    private int offsetY;
    private int height;
    private int width;
    private Direction direction;
    private boolean used;

    public Mount(int ox, int oy, int h, int w, Direction dir, SpaceshipComponent parent) {
        offsetX = ox;
        offsetY = oy;
        height = h;
        width = w;
        direction = dir;
        used = false;

        this.parent = parent;
    }

    public int getOffsetX() {
        return offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public SpaceshipComponent getAttached() {
        return attached;
    }

    public SpaceshipComponent getParent() {
        return parent;
    }

    public Direction getDirection() {
        return direction;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used, SpaceshipComponent a) {
        if (used) {
            this.used = used;
            attached = a;
        } else {
            this.used = false;
        }
    }
}

package main_package;

public class Mount {
    private int offsetX;
    private int offsetY;
    private boolean used;

    public Mount(int x, int y) {
        offsetX = x;
        offsetY = y;
        used = false;
    }

    public int getOffsetX() {
        return offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }
}

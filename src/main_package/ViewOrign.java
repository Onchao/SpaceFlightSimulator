package main_package;

public class ViewOrign {
    private static int x = 400;
    private static int y = 300;
    public static int getX(){
        return x;
    }
    public static int getY(){
        return y;
    }
    private static void set(int x, int y){
        ViewOrign.x = x;
        ViewOrign.y = y;
    }
}

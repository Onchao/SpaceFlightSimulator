package world;

public class Const {
    public static double G = 6.67408e-11; // Nm^2/kg^2

    public static double SCALE = 500000;
    public static int originIndex = 0;
    public static int solarSystemSize;

    public static void cycleOrigin(){
        originIndex++;
        originIndex%=solarSystemSize;
    }
}
package world;


import ship.Spaceship;

import java.util.ArrayList;

public class Scale {
    private static double[] presetScales = {1, 1e-5, 5e-7, 1.5e-9 };
    private static int presetIndex = 0;
    public static double SCALE = 1;

    public static void cycleScale(){
        presetIndex = (presetIndex + 1)%4;
        SCALE = presetScales[presetIndex];
    }

    public static void zoom(double y){
        if (y > 0)
            SCALE *= 1.2;
        else if (y < 0)
            SCALE *= 0.8;
        presetIndex = -1;
    }
}
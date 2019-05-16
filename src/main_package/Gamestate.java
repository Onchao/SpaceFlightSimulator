package main_package;

public class Gamestate {
    public enum gs{
        MENU,
        BUILD,
        FLY,
        // FAIL, // ???
        // SUCCEED // ???
    }
    public static gs GS;
    public static gs getGS(){
        return GS;
    }

    public static void setGamestateMENU(){
        GS = gs.MENU;
    };
    public static void setGamestateBUILD(){
        GS = gs.BUILD;
    };
    public static void setGamestateFLY(){
        GS = gs.FLY;
    };
}

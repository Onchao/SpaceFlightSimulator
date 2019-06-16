package main_package;

public class Gamestate {
    public enum gs{
        MENU,
        BUILD,
        FLY,
        HELP,
        CONTROLS,
        EXIT
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
    public static void setGamestateHELP(){
        GS = gs.HELP;
    };
    public static void setGamestateCONTROLS(){
        GS = gs.CONTROLS;
    };
    public static void setGamestateEXIT(){
        GS = gs.EXIT;
    };
}

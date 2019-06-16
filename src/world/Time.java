package world;

public class Time {
    private static double TIME; // s
    private static double deltaTIME; //s
    private static int timeWarp;

    public static double getTIME(){
        return TIME;
    }
    public static double getDeltaTIME(){
        return deltaTIME;
    }
    public static int getTimeWarp(){
        return timeWarp;
    }

    private static double prevMillis;

    /*
    1,2,4,8 atmosphere + acceleration
    16, 64, 256, 1024
    16384, 262144

     */


    public Time(double TIME){
        this.TIME = TIME;
        timeWarp = 1;
        prevMillis = System.currentTimeMillis();
    }

    // TODO: pause
    public void stop(){
    }

    public static void updateTime(){
        double currentMillis = System.currentTimeMillis();
        deltaTIME = ((currentMillis - prevMillis)/1000)*timeWarp;
        TIME += deltaTIME;
        prevMillis = currentMillis;
    }

    private static boolean timerActive = false;
    private static long timeStart;
    private static long timerTime;
    public static void startTimer(long millis){
        timerActive = true;
        timeStart = System.currentTimeMillis();
        timerTime = millis;
    }
    public static boolean isTimerActive(){
        return timerActive;
    }
    public static boolean timePassed(){
        if(!timerActive){
            return false;
        }
        if(System.currentTimeMillis() - timeStart > timerTime){
            timerActive = false;
            return true;
        }
        return false;
    }
    public static void freeTimer(){
        timerActive = false;
    }





    public static void faster(){
        switch (timeWarp){
            case 1:
                timeWarp = 2;
                break;
            case 2:
                timeWarp = 4;
                break;
            case 4:
                timeWarp = 8;
                break;
            case 8:
                timeWarp = 16;
                break;

            case 16:
                timeWarp = 64;
                break;
            case 64:
                timeWarp = 256;
                break;
            case 256:
                timeWarp = 1024;
                break;
            case 1024:
                timeWarp = 4096;
                break;
            case 4096:
                timeWarp = 16384;
                break;
            case 16384:
                timeWarp = 65536;
                break;
            case 65536:
                timeWarp = 262144;
                break;
            case 262144:
                timeWarp = 1048576;
                break;
            case 1048576:
                timeWarp = 4194304;
                break;
        }
    }
    public static void slower(){
        switch (timeWarp){
            case 2:
                timeWarp = 1;
                break;
            case 4:
                timeWarp = 2;
                break;
            case 8:
                timeWarp = 4;
                break;

            case 16:
                timeWarp = 8;
                break;
            case 64:
                timeWarp = 16;
                break;
            case 256:
                timeWarp = 64;
                break;
            case 1024:
                timeWarp = 256;
                break;
            case 4096:
                timeWarp = 1024;
                break;
            case 16384:
                timeWarp = 4096;
                break;
            case 65536:
                timeWarp = 16384;
                break;
            case 262144:
                timeWarp = 65536;
                break;
            case 1048576:
                timeWarp = 262144;
                break;
            case 4194304:
                timeWarp = 1048576;
                break;
        }
    }
}

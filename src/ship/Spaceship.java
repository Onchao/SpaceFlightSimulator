package ship;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.transform.Rotate;
import utility.Const;
import utility.Point;
import world.CelestialBody;
import world.Time;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Spaceship {
    private List<List<SpaceshipComponent>> stages;
    private Group drawable;
    public Node getDrawable(){ return drawable; }
    public ImageView img = new ImageView(new Image("file:images/smallRocket.png"));

    // where is it?
    private CelestialBody parent;
    public CelestialBody getParent(){ return parent; }
    private Point origin;
    private double rel_pos_x; // to parent [m]
    private double rel_pos_y; // to parent [m]
    public double getAbsPos_x(){
        return parent.getAbsPos_x() + rel_pos_x;
    }
    public double getAbsPos_y(){
        return parent.getAbsPos_y() + rel_pos_y;
    }
    public double angleOnPlanet;

    // what is it doing?
    private boolean landed = true;
    private int throttle; // [0,100]
    private int throttleModifier;
    private double vel_x; // to parent [m/s]
    private double vel_y; // to parent [m/s]

    Rotate rotate = new Rotate();

    private Point convertCoordinates(Point point) {
        double dx = (point.getX() - origin.getX())/50; // 1m == 50px
        double dy = (point.getY() - origin.getY())/50;

        double r = Math.sqrt(dx*dx + dy*dy);
        double angle = Math.atan2(dy, dx) + (rotate.getAngle()*(2*Math.PI))/360;

        return new Point(r*Math.cos(angle), r*Math.sin(angle));
    }

    public Spaceship (List<List<SpaceshipComponent>> stages) {
        this.stages = stages;

        drawable = new Group();
        for (List<SpaceshipComponent> stage : stages) {
            for (SpaceshipComponent comp : stage) {
                drawable.getChildren().add(comp.getImage());
            }
        }
        //origin = getCenterOfMass();
        origin =  new Point(drawable.getLayoutBounds().getCenterX(),drawable.getLayoutBounds().getCenterY());

    }

    public void placeSpaceship(CelestialBody parent, double angleOnPlanet){
        this.parent = parent;
        this.angleOnPlanet = angleOnPlanet;
        rel_pos_x = parent.getShipPosFromAngle_x(angleOnPlanet);
        rel_pos_y = parent.getShipPosFromAngle_y(angleOnPlanet);

        drawable.getTransforms().add(rotate);
        img.getTransforms().add(rotate);
        rotate.setAngle(-angleOnPlanet + 90);
    }

    public void update(){
        updateThrottle();
        if(landed && throttle!=0)
            attemptLiftOff();

        if(landed){
            updateAngleOnPlanet();
            rel_pos_x = parent.getShipPosFromAngle_x(angleOnPlanet);
            rel_pos_y = parent.getShipPosFromAngle_y(angleOnPlanet);
            rotate.setAngle(-angleOnPlanet + 90);
        }
        else{
            rel_pos_x = rel_pos_x + vel_x* Time.deltaTIME;
            rel_pos_y = rel_pos_y + vel_y* Time.deltaTIME;

            updateAngleOnPlanet();
        }
        setPrintScale();
    }

    //TODO: changeParent (artur)
    void changeParent(){

    }

    void attemptLiftOff(){
        System.out.println("LIFT OFF");

        if(!enginesPresent() || !fuelPresent())
            return;

        double velocity = 2*parent.radius*Math.PI
                /(parent.rotationPeriod);

        vel_x = velocity * Math.cos(Math.toRadians(angleOnPlanet + 90));
        vel_y = velocity * Math.sin(Math.toRadians(angleOnPlanet + 90));


        //angleOnPlanet
        // nadac predkosc z planety

        landed = false;
    }

    //TODO: attemptLanding (artur)
    void attemptLanding(){

    }

    private boolean enginesPresent(){
        return true;
    }

    private boolean fuelPresent(){
        return true;
    }

    public double getScale() {
        return drawable.getScaleX();
    }

    public void setPrintScale() {
        drawable.setScaleX(Const.SCALE/50);
        drawable.setScaleY(Const.SCALE/50);
    }

    public void setPrintPosition(double x, double y) {
        double dx = (x - origin.getX());
        double dy = (y - origin.getY());

        origin = new Point(x, y);
        img.setX(x - 25);
        img.setY(y - 25);

        for (List<SpaceshipComponent> stage : stages) {
            for (SpaceshipComponent comp : stage) {
                comp.getImage().setLayoutX((comp.getImage().getLayoutX() + dx));
                comp.getImage().setLayoutY((comp.getImage().getLayoutY() + dy));
            }
        }
        rotate.setPivotX(x);
        rotate.setPivotY(y);
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

        return convertCoordinates(new Point(xs/d, ys/d));
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

        return convertCoordinates(new Point(xs/d, ys/d));
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

        return convertCoordinates(new Point(xs/d, ys/d));
    }

    public double getMomentOfInertia () {
        double ret = 0;

        for (List<SpaceshipComponent> stage : stages) {
            for (SpaceshipComponent comp : stage) {
                double d = getCenterOfMass().getX() - comp.getImage().getLayoutX();
                ret += comp.getMomentOfInertiaX() + comp.getMass()*d*d;
            }
        }

        return ret;
    }

    public List<Point> getVertices () {
        List<Point> ret = new ArrayList<>();

        for (List<SpaceshipComponent> stage : stages)
            for (SpaceshipComponent comp : stage)
                for (Point v : comp.getVertices())
                    ret.add(convertCoordinates(v));

        return ret;
    }

    public void maxThrottle(){
        throttle = 100;
        setThrottleModifier(0);
    }
    public void zeroThrottle(){
        throttle = 0;
        setThrottleModifier(0);
    }
    public void setThrottleModifier(int delta){
        throttleModifier = delta;
    }
    private void updateThrottle(){
        if(throttleModifier != 0){
            throttle += throttleModifier;
            if(throttle<0)
                throttle = 0;
            else if(throttle > 100)
                throttle = 100;
        }
    }

    private void updateAngleOnPlanet(){
        if(landed) {
            double angleDif = parent.getAngleDif();
            angleOnPlanet += angleDif;
        }
        else{
            angleOnPlanet = Math.toDegrees(
                    Math.tan((getAbsPos_y() - parent.getAbsPos_y())/
                            (getAbsPos_x() - parent.getAbsPos_x())));
        }
    }
}

package ship;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.transform.Rotate;
import utility.Const;
import utility.Point;
import world.CelestialBody;
import world.Scale;
import world.SolarSystem;
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
    public Point getAbsPos(){
        return new Point(parent.getAbsPos().getX() + rel_pos_x,
                parent.getAbsPos().getY() + rel_pos_y);
    }

    private double angleOnPlanet;
    public double getAngleOnPlanet(){
        return angleOnPlanet;
    }
    private double distToBottom;

    private SolarSystem solarSystem;

    // what is it doing?
    private boolean landed = true;
    private int throttle; // [0,100]
    private int throttleModifier;
    private double vel_x; // to parent [m/s]
    private double vel_y; // to parent [m/s]

    private Rotate rotate = new Rotate();
    private double turnModifier = 0;

    public void info(){
        System.out.println("pos_x: "  + getAbsPos().getX());
        System.out.println("pos_y: "  + getAbsPos().getY());
        System.out.println("vel: " + Math.sqrt(vel_x*vel_x + vel_y*vel_y));

        System.out.println("gravity_x: "  + getGravityInfluence().getX());
        System.out.println("gravity_y: "  + getGravityInfluence().getY());
        System.out.println(throttle + " / 100");
    }

    private Point convertCoordinates(Point point) {
        double dx = (point.getX() - origin.getX())/50; // 1m == 50px
        double dy = (point.getY() - origin.getY())/50;

        double r = Math.sqrt(dx*dx + dy*dy);
        double angle = Math.atan2(dy, dx) + (rotate.getAngle()*(2*Math.PI))/360;

        return new Point(r*Math.cos(angle), r*Math.sin(angle));
    }

    public Spaceship (List<List<SpaceshipComponent>> stages, CelestialBody parent, double angleOnPlanet, SolarSystem solarSystem) {
        this.stages = stages;

        drawable = new Group();
        for (List<SpaceshipComponent> stage : stages) {
            for (SpaceshipComponent comp : stage) {
                drawable.getChildren().add(comp.getImage());
            }
        }
        origin =  new Point(drawable.getLayoutBounds().getCenterX(),drawable.getLayoutBounds().getCenterY());
        double minY = 1000000000.0; // almost infinity
        for (Point v : getVertices()) {
            if (v.getY() < minY) minY = v.getY();
        }
        distToBottom = Math.abs(minY);
        System.out.println("distToBottom: " + distToBottom);

        this.parent = parent;
        this.angleOnPlanet = angleOnPlanet;
        this.solarSystem = solarSystem;

        rel_pos_x = parent.getShipPosFromAngle_x(angleOnPlanet, distToBottom);
        rel_pos_y = parent.getShipPosFromAngle_y(angleOnPlanet, distToBottom);

        drawable.getTransforms().add(rotate);
        img.getTransforms().add(rotate);
        rotate.setAngle(-angleOnPlanet + 90);
    }

    //TODO: make it with respect to "rotate"
    private double calculateDistToBottom(){
        double minY = 1000000000.0; // almost infinity
        for (Point v : getVertices()) {
            if (v.getY() < minY) minY = v.getY();
        }
        distToBottom = Math.abs(minY);
        return distToBottom;
    }

    public void update(){
        updateThrottle();
        if(landed && throttle!=0)
            attemptLiftOff();

        if(landed){
            updateAngleOnPlanet();
            rel_pos_x = parent.getShipPosFromAngle_x(angleOnPlanet, distToBottom);
            rel_pos_y = parent.getShipPosFromAngle_y(angleOnPlanet, distToBottom);
            rotate.setAngle(-angleOnPlanet + 90);
        }
        else{
            updateTurn();

            Point gravityVel = getGravityInfluence();
            Point engineVel = getEngineInfluence();
            Point aeroVel = genAeroInfluence();

            vel_x += (gravityVel.getX() + engineVel.getX() + aeroVel.getX());
            vel_y += (gravityVel.getY() + engineVel.getY() + aeroVel.getY());
            rel_pos_x = rel_pos_x + vel_x* Time.deltaTIME;
            rel_pos_y = rel_pos_y + vel_y* Time.deltaTIME;
            updateAngleOnPlanet();
            updateParent();
            distToBottom = Math.sqrt(rel_pos_x*rel_pos_x + rel_pos_y*rel_pos_y) - parent.radius;

            //detect collisions
            if(distToBottom < 0)
                attemptLanding();
        }
        setPrintScale();
    }

    private Point getGravityInfluence(){ // [m/s]
        double x = 0;
        double y = 0;
        for (CelestialBody B : solarSystem.bodies) {
            double r = B.getDistanceTo(getAbsPos().getX(),getAbsPos().getY());
            double velocity = Const.G * B.mass * Time.deltaTIME / r/r;
            double angle = Math.toDegrees(Math.atan2(getAbsPos().getY() - B.getAbsPos().getY(),
                    getAbsPos().getX() - B.getAbsPos().getX()));
            //if(B.name.equals("Earth")) System.out.println(angle);
            x -= velocity * Math.cos(Math.toRadians(angle));
            y -= velocity * Math.sin(Math.toRadians(angle));
        }
        return new Point(x, y);
    }

    private Point getEngineInfluence(){ // [m/s]
        double Fx = Math.cos(Math.toRadians(-rotate.getAngle() + 90)) * getTotalThrust();
        double Fy = Math.sin(Math.toRadians(-rotate.getAngle() + 90)) * getTotalThrust();
        return new Point(Fx*Time.deltaTIME/getTotalMass(), Fy*Time.deltaTIME/getTotalMass());
    }

    private Point genAeroInfluence() {
        double Dx = 0;
        double Dy = 0;
        for (List<SpaceshipComponent> stage : stages) {
            for (SpaceshipComponent comp : stage) {
                Dx-=(0.5*0.75*parent.getAtmDensity(distToBottom)*4*Math.PI*vel_x*vel_x);
                Dx-=(0.5*0.75*parent.getAtmDensity(distToBottom)*4*Math.PI*vel_y*vel_y);
            }
        }
        return new Point(Dx*Time.deltaTIME/getTotalMass(), Dy*Time.deltaTIME/getTotalMass());
    }

    void attemptLiftOff(){
        System.out.println("LIFT OFF");

        if(!enginesPresent() || !fuelPresent())
            return;

        double velocity = 2*parent.radius*Math.PI
                /(parent.rotationPeriod);
        vel_x = velocity * Math.cos(Math.toRadians(angleOnPlanet + 90));
        vel_y = velocity * Math.sin(Math.toRadians(angleOnPlanet + 90));
        landed = false;
    }

    void attemptLanding(){
        Point v =  parent.getPlanetVelocity();
        double delta_x = v.getX() - vel_x;
        double delta_y = v.getY() - vel_y;
        double velocity = Math.sqrt(delta_x*delta_x + delta_y*delta_y);
        if(velocity > 10){
            System.out.println("CRASH !!!");
        }
        else{
            System.out.println("LANDED SUCCESSFULLY !!!");
        }
        vel_x = 0;
        vel_y = 0;
        distToBottom = calculateDistToBottom();
        rel_pos_x = parent.getShipPosFromAngle_x(angleOnPlanet, distToBottom);
        rel_pos_y = parent.getShipPosFromAngle_y(angleOnPlanet, distToBottom);

        landed = true;

        //TODO: reset only radial velocity and calculate prograde on friciton force
        // if (stable time > cos) ---> LANDED
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
        drawable.setScaleX(Scale.SCALE/50);
        drawable.setScaleY(Scale.SCALE/50);
    }

    public void setPrintPosition(double x, double y) {
        double dx = (x - origin.getX());
        double dy = (y - origin.getY());

        origin = new Point(x, y);
        //TODO: make it better
        img.setX(x - 25);
        img.setY(y - 46);

        for (List<SpaceshipComponent> stage : stages) {
            for (SpaceshipComponent comp : stage) {
                comp.getImage().setLayoutX((comp.getImage().getLayoutX() + dx));
                comp.getImage().setLayoutY((comp.getImage().getLayoutY() + dy));
            }
        }
        rotate.setPivotX(x);
        rotate.setPivotY(y);
    }

    /*
    public Point getPosition () {
        return origin;
    }
    */

    public List<SpaceshipComponent> getStage (int i) {
        return Collections.unmodifiableList(stages.get(i));
    }

    public long getStageMass (int i) {
        long ret = 0;

        List<SpaceshipComponent> curStage = stages.get(i);

        for (SpaceshipComponent comp : curStage) {
            ret += comp.getMass();
        }

        return ret;
    }

    public long getTotalMass(){
        long ret = 0;
        for (int j = 0; j < stages.size(); ++ j) {
            long smass = getStageMass(j);
            ret += smass;
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
            long smass = getStageMass(j);

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

    public double getTotalThrust () {
        double d = 0;

        for (List<SpaceshipComponent> stage : stages) {
            for (SpaceshipComponent comp : stage) {
                if (comp instanceof Engine) {
                    d += ((Engine) comp).getThrust();
                }
            }
        }
        return d * throttle / 100;
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

    //TODO: make this based on moment and time
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

    public void setTurnModifier(double angle){
        turnModifier = angle;
    }
    private void updateTurn(){
        if(turnModifier != 0)
            rotate.setAngle(rotate.getAngle() + turnModifier);
    }

    private void updateAngleOnPlanet(){
        if(landed) {
            double angleDif = parent.getAngleDif();
            angleOnPlanet += angleDif;
        }
        else{
            angleOnPlanet = Math.toDegrees(
                    Math.atan2(getAbsPos().getY() - parent.getAbsPos().getY(),
                            getAbsPos().getX() - parent.getAbsPos().getX()));
        }
    }

    public void updateParent(){
        CelestialBody newParent = solarSystem.bodies.get(0);
        for (CelestialBody B : solarSystem.bodies) {
            double dist = B.getDistanceTo(getAbsPos().getX(), getAbsPos().getY());
            if(dist < B.getEscapeRadius()){
                newParent = B;
            }
        }
        if(newParent != parent){
            if(parent.parent == newParent ){ // earth.parent-> sun  == sun?
                System.out.println("OUT");
                Point velocity = parent.getPlanetVelocity();
                System.out.println(velocity.getX());
                System.out.println(velocity.getY());
                vel_x += velocity.getX();
                vel_y += velocity.getY();
                rel_pos_x = rel_pos_x + parent.getRelPos().getX();
                rel_pos_y = rel_pos_y + parent.getRelPos().getY();

                parent = newParent;
                System.out.println("NEW PARENT: " + parent.name);
            }
            else{
                System.out.println("IN");
                Point velocity = parent.getPlanetVelocity();
                System.out.println(velocity.getX());
                System.out.println(velocity.getY());
                vel_x += velocity.getX();
                vel_y += velocity.getY();
                rel_pos_x = rel_pos_x - newParent.getRelPos().getX();
                rel_pos_y = rel_pos_y - newParent.getRelPos().getY();

                parent = newParent;
                System.out.println("NEW PARENT: " + parent.name);
            }
            System.out.println();
        }
    }

    public double getDistToBottom() {
        return distToBottom;
    }
}

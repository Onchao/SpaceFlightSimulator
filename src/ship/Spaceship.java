package ship;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.transform.Rotate;
import ship.components.*;
import utility.DiGraph;
import utility.Direction;
import utility.Force;
import utility.Point;
import world.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class Spaceship {

    public static class ComponentWithCenter {
        private SpaceshipComponent component;
        private Point center;

        public ComponentWithCenter (SpaceshipComponent comp, Point p) {
            component = comp;
            center = p;
        }

        public SpaceshipComponent getComponent() {
            return component;
        }

        public Point getCenter() {
            return center;
        }
    }

    private List<List<SpaceshipComponent>> stages;
    private List<List<ActiveComponent>> activationQueue;
    private DiGraph<Integer> stagesGraph;
    private List<Debris> shipDebris;
    private int finalStage;
    public List<List<SpaceshipComponent>> getStages(){
        return stages;
    }

    private Group drawable;
    public Node getDrawable(){ return drawable; }
    public ImageView img = new ImageView(new Image("file:images/smallRocket.png"));
    private ForceInfluence forceInfluence;


    // where is it?
    private CelestialBody parent;
    public CelestialBody getParent(){ return parent; }
    private Point origin;
    private double distToBottom;
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

    private SolarSystem solarSystem;

    // what is it doing?
    private boolean landed = true;
    private int throttle; // [0,100]
    private int throttleModifier;
    private double vel_x; // to parent [m/s]
    public double getVel_x(){
        return vel_x;
    }
    private double vel_y; // to parent [m/s]
    public double getVel_y(){
        return vel_y;
    }


    private Rotate rotate = new Rotate();
    public Rotate getRotate(){
        return rotate;
    }
    private double turnModifier = 0;

    public void info(){
        System.out.println("pos_x: "  + getAbsPos().getX());
        System.out.println("pos_y: "  + getAbsPos().getY());
        System.out.println("vel: " + Math.sqrt(vel_x*vel_x + vel_y*vel_y));

        //System.out.println("gravity_x: "  + getGravityInfluence().getX());
        //System.out.println("gravity_y: "  + getGravityInfluence().getY());
        System.out.println(throttle + " / 100");
    }

    private void recalculateOrigin () {
        origin =  new Point(drawable.getBoundsInLocal().getCenterX(),drawable.getBoundsInLocal().getCenterY());
    }

    private Point convertCoordinates(Point point) {
        double dx = (point.getX() - origin.getX())/50; // 1m == 50px
        double dy = (point.getY() - origin.getY())/50;

        double r = Math.sqrt(dx*dx + dy*dy);
        double angle = Math.atan2(dy, dx) + Math.toRadians(rotate.getAngle());// (rotate.getAngle()*(2*Math.PI))/360;

        return new Point(r*Math.cos(angle), r*Math.sin(angle));
    }

    public Spaceship (List<List<SpaceshipComponent>> stages, List<List<ActiveComponent>> activationQueue, CelestialBody parent, double angleOnPlanet, SolarSystem solarSystem) {
        this.stages = stages;
        this.activationQueue = activationQueue;
        shipDebris = new ArrayList<>();

        stagesGraph = new DiGraph<>();
        for (int i = 0; i < this.stages.size(); ++ i) stagesGraph.addVertex(i);
        for (int i = 0; i < this.stages.size(); ++ i) {
            for (SpaceshipComponent comp : this.stages.get(i)) {
                if (comp instanceof CircularDecouplerComponent && comp.getUpperMount().isUsed()) {
                    stagesGraph.addEdge(comp.getUpperMount().getAttached().getStageNumber(), comp.getStageNumber());
                }
                if (comp instanceof RadialDecouplerComponent) {
                    if (((RadialDecouplerComponent) comp).getDirection() == Direction.RIGHT && comp.getLeftMount().isUsed()) {
                        stagesGraph.addEdge(comp.getLeftMount().getAttached().getStageNumber(), comp.getStageNumber());
                    } else if (((RadialDecouplerComponent) comp).getDirection() == Direction.LEFT && comp.getRightMount().isUsed()) {
                        stagesGraph.addEdge(comp.getRightMount().getAttached().getStageNumber(), comp.getStageNumber());
                    }
                }
            }
        }
        finalStage = stagesGraph.getRoot();

        drawable = new Group();
        for (List<SpaceshipComponent> stage : stages) {
            for (SpaceshipComponent comp : stage) {
                drawable.getChildren().add(comp.getImage());
            }
        }

        //origin =  new Point(drawable.getLayoutBounds().getCenterX(),drawable.getLayoutBounds().getCenterY());
        recalculateOrigin();
        calculateDistToBottom();

        forceInfluence = new ForceInfluence(this, solarSystem);
        this.parent = parent;
        this.angleOnPlanet = angleOnPlanet;
        this.solarSystem = solarSystem;


        rel_pos_x = parent.getShipPosFromAngle_x(angleOnPlanet, distToBottom);
        rel_pos_y = parent.getShipPosFromAngle_y(angleOnPlanet, distToBottom);

        drawable.getTransforms().add(rotate);
        img.getTransforms().add(rotate);
        rotate.setAngle(-angleOnPlanet + 90);
    }

    public void activateNext () {
        for (ActiveComponent comp : activationQueue.get(0)) {
            ComponentAction action = comp.activate();
            if (action.getType() == ComponentAction.ActionType.DETACH_STAGE) {
                Integer u = ((SpaceshipComponent) comp).getStageNumber();
                Integer v = null;
                if ((comp instanceof CircularDecouplerComponent) && ((CircularDecouplerComponent) comp).getUpperMount().isUsed()) {
                    v = ((CircularDecouplerComponent) comp).getUpperMount().getAttached().getStageNumber();
                } else if (comp instanceof RadialDecouplerComponent) {
                    if (((RadialDecouplerComponent) comp).getDirection() == Direction.RIGHT
                            && ((RadialDecouplerComponent) comp).getLeftMount().isUsed()) {
                        v = ((RadialDecouplerComponent) comp).getLeftMount().getAttached().getStageNumber();
                    } else if (((RadialDecouplerComponent) comp).getDirection() == Direction.LEFT
                            && ((RadialDecouplerComponent) comp).getRightMount().isUsed()) {
                        v = ((RadialDecouplerComponent) comp).getRightMount().getAttached().getStageNumber();
                    }
                }
                stagesGraph.removeEdge(v, u);
                Set<Integer> stillAttached = stagesGraph.getReachable(finalStage);
                List <SpaceshipComponent> createdDebris = new ArrayList<>();
                for (int i = 0; i < stages.size(); ++ i) {
                    if (!stillAttached.contains(i)) {
                        createdDebris.addAll(stages.get(i));
                        for (SpaceshipComponent toRemove : stages.get(i)) drawable.getChildren().remove(toRemove.getImage());
                        stages.get(i).clear();
                    }
                }
                shipDebris.add (new Debris(createdDebris));
            }
        }
        activationQueue.remove(0);
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
            Force F = forceInfluence.getCombinedForces();
            //TODO: add momentum
            vel_x += F.getFx()*Time.deltaTIME/getTotalMass();
            vel_y += F.getFy()*Time.deltaTIME/getTotalMass();
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

        Circle redCircle;
        redCircle = new Circle();
        redCircle.setFill(Color.RED);
        redCircle.setRadius(5);
        redCircle.setCenterX(origin.getX());
        redCircle.setCenterY(origin.getY());
        drawable.getChildren().add(redCircle);
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

    private Circle yellowCircle = null;
    public Point getCenterOfMass () {
        double xs = 0;
        double ys = 0;
        double d = 0;

        for (List<SpaceshipComponent> stage : stages) {
            for (SpaceshipComponent comp : stage) {
                xs += comp.getCenterOfMassX()*comp.getMass();
                ys += comp.getCenterOfMassY()*comp.getMass();

                d += comp.getMass();
            }
        }

        Point ret = convertCoordinates(new Point(xs/d, ys/d));

        if (yellowCircle == null) {
            yellowCircle = new Circle();
            yellowCircle.setFill(Color.YELLOW);
            yellowCircle.setRadius(7);
            drawable.getChildren().add(yellowCircle);
        }
        yellowCircle.setCenterX(xs/d);
        yellowCircle.setCenterY(ys/d);

        return ret;
    }

    private Circle blueCircle = null;
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
        if (blueCircle == null) {
            blueCircle = new Circle();
            blueCircle.setFill(Color.BLUE);
            blueCircle.setRadius(7);
            drawable.getChildren().add(blueCircle);
        }
        blueCircle.setCenterX(xs/d);
        blueCircle.setCenterY(ys/d);

        Point ret = convertCoordinates(new Point(xs/d, ys/d));

        return ret;
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

    public double getFuelInStage (int stageNo) {
        int numTanks = 0;
        double fuelStatesSum = 0.0;
        for (SpaceshipComponent comp : stages.get(stageNo)) {
            if (comp instanceof FuelTankComponent) {
                ++ numTanks;
                fuelStatesSum += ((FuelTankComponent) comp).getFuelState();
            }
        }
        return fuelStatesSum/numTanks;
    }

    public double getFuelTotal () {
        int numTanks = 0;
        double fuelStatesSum = 0.0;
        for (List<SpaceshipComponent> stage : stages) {
            for (SpaceshipComponent comp : stage) {
                if (comp instanceof FuelTankComponent) {
                    ++numTanks;
                    fuelStatesSum += ((FuelTankComponent) comp).getFuelState();
                }
            }
        }
        return fuelStatesSum/numTanks;
    }

    public List<ComponentWithCenter> getComponentCenters () {
        List<ComponentWithCenter> ret = new ArrayList<>();

        for (List <SpaceshipComponent> stage : stages)
            for (SpaceshipComponent comp : stage)
                ret.add(new ComponentWithCenter(comp, convertCoordinates(comp.getGeoCenter())));
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

    public int getThrottle(){
        return throttle;
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
            Point velocity = parent.getPlanetVelocity();
            vel_x += velocity.getX();
            vel_y += velocity.getY();
            if(parent.parent == newParent ){ // earth.parent-> sun  == sun?
                rel_pos_x = rel_pos_x + parent.getRelPos().getX();
                rel_pos_y = rel_pos_y + parent.getRelPos().getY();
            }
            else{
                rel_pos_x = rel_pos_x - newParent.getRelPos().getX();
                rel_pos_y = rel_pos_y - newParent.getRelPos().getY();
            }
            parent = newParent;
            System.out.println("NEW PARENT: " + parent.name);
        }
    }

    public void calculateDistToBottom() {
        double minY = 1000000000.0; // almost infinity
        for (Point v : getVertices()) {
            if (v.getY() < minY) minY = v.getY();
        }
        System.out.println(Math.abs(minY));
        distToBottom = Math.abs(minY);
    }

    public double getDistToBottom() {
        return distToBottom;
    }
}

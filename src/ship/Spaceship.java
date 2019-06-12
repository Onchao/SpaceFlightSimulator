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
    public ImageView img = new ImageView(new Image(getClass().getResourceAsStream("/images/smallRocket.png")));
    private ForceInfluence forceInfluence;


    // where is it?
    private CelestialBody parent;
    public CelestialBody getParent(){ return parent; }
    private Point origin;
    private double distToBottom;
    private Point pos; // to parent [m]
    public Point gestPos(){
        return pos;
    }

    public Point getAbsPos(){
        return new Point(parent.getAbsPos().getX() + pos.getX(),
                parent.getAbsPos().getY() + pos.getY());
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
    private Point vel = new Point(0.0,0.0); // to parent [m/s]
    public Point getVel(){
        return vel;
    }

    private Rotate rotate = new Rotate();
    public Rotate getRotate(){
        return rotate;
    }
    private Rotate chuteRotate = new Rotate();
    private double turnMomentum = 0;
    private double forceMomentum = 0;
    private double turnSpeed = 0; // deg/s


    public double getOrbitalSpeed(){
        return Math.sqrt(vel.getX()* vel.getX() + vel.getY()* vel.getY());
    }

    public Point getVelocityTakingWind(){
        Point p = parent.getWindVelocity(this);
        return new Point(vel.getX() - p.getX(), vel.getX() - p.getY());
    }

    //OK
    public double getVerticalOrbitalSpeed(){
        double alpha = pos.getAngle();
        double beta = vel.getAngle();
        double gamma = beta - alpha;
        return vel.getModule()*Math.cos(gamma);
    }

    //OK
    public double getHorizontalOrbitalSpeed(){
        double alpha = pos.getAngle();
        double beta = vel.getAngle();
        double gamma = beta - alpha;
        return vel.getModule()*Math.sin(gamma);
    }


    public double getHorizontalSurfaceSpeed(){
        return getHorizontalOrbitalSpeed() - parent.getPlanetSurfaceSpeed();
    }

    public Point getVerticalOrbitalVelocity(){
        double v = getVerticalOrbitalSpeed();
        return new Point(v * Math.cos(pos.getAngle()), v * Math.sin(pos.getAngle()));
    }


    public Point getHorizontalOrbitalVelocity(){
        double v = getHorizontalOrbitalSpeed();
        return new Point( v * Math.cos(Math.toRadians(Math.toDegrees(pos.getAngle())+90)),
                         v * Math.sin(Math.toRadians(Math.toDegrees(pos.getAngle())+90)));
    }

    public Point getHorizontalSurfaceVelocity(){
        double v = getHorizontalSurfaceSpeed();
        return new Point( v * Math.cos(Math.toRadians(Math.toDegrees(pos.getAngle())+90)),
                v * Math.sin(Math.toRadians(Math.toDegrees(pos.getAngle())+90)));
    }

    public double getAltitude(){
        double result = parent.getDistanceTo(getAbsPos().getX(), getAbsPos().getY());
        result -= parent.radius;




        return result;
    }

    public void info(){
        System.out.println();
        System.out.println(getOrbitalSpeed());
        System.out.println(getHorizontalOrbitalSpeed());
        System.out.println(parent.getPlanetSurfaceSpeed());
        System.out.println(getVerticalOrbitalSpeed());
        System.out.println(vel.getX());
        System.out.println(vel.getY());

        System.out.println();
        System.out.println(getHorizontalSurfaceSpeed());
        System.out.println(getHorizontalOrbitalVelocity().getX());
        System.out.println(getHorizontalOrbitalVelocity().getY());
        System.out.println();
        System.out.println(getVerticalOrbitalVelocity().getX() + " " + getVerticalOrbitalVelocity().getY());
        System.out.println(getHorizontalSurfaceVelocity().getX() + " " + getHorizontalSurfaceVelocity().getY());

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

        recalculateOrigin(400, 400);
        calculateDistToBottom();

        forceInfluence = new ForceInfluence(this, solarSystem);
        this.parent = parent;
        this.angleOnPlanet = angleOnPlanet;
        this.solarSystem = solarSystem;

        pos = parent.getShipPosFromAngle(angleOnPlanet, distToBottom);

        drawable.getTransforms().add(rotate);
        img.getTransforms().add(rotate);
        rotate.setAngle(-angleOnPlanet + 90);

        chuteRotate.setPivotX(420);
        chuteRotate.setPivotY(820);
        chuteRotate.setAngle(0);
    }

    public synchronized void activateNext () {
        if (activationQueue.isEmpty()) return;
        for (ActiveComponent comp : activationQueue.get(0)) {
            ImageView oldImg = ((SpaceshipComponent) comp).getImage();
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
                shipDebris.add (new Debris(createdDebris, vel.getX(), vel.getY()));

            } else if (action.getType() == ComponentAction.ActionType.OPEN_PARACHUTE) {
                drawable.getChildren().remove(oldImg);
                ImageView img = ((SpaceshipComponent) comp).getImage();
                img.getTransforms().add(chuteRotate);
                drawable.getChildren().add(img);
            }
            recalculateOrigin(400, 400);

        }
        activationQueue.remove(0);
    }

    public synchronized void update(){
        //System.out.println(getVelocityTakingWind().getY());

        chuteRotate.setAngle(-rotate.getAngle() - Math.toDegrees(Math.atan2(-vel.getY(), -vel.getX())) + 90);
        //System.out.println("Vel: " + vel_x + " " + vel_y + ", Angle: " + Math.toDegrees(Math.atan2(-vel_y, -vel_x)));

        updateThrottle();
        if(landed && throttle!=0)
            attemptLiftOff();

        if(landed){
            updateAngleOnPlanet();
            pos = new Point(parent.getShipPosFromAngle(angleOnPlanet, distToBottom).getX(),
                    parent.getShipPosFromAngle(angleOnPlanet, distToBottom).getY());
            rotate.setAngle(-angleOnPlanet + 90);
        }
        else{
            updateTurn();
            Force F = forceInfluence.getCombinedForces();

            vel = new Point(vel.getX() + F.getFx()*Time.deltaTIME/getTotalMass(),
                    vel.getY() + F.getFy()*Time.deltaTIME/getTotalMass());

            pos = new Point(pos.getX() + vel.getX() * Time.deltaTIME,
                    pos.getY() + vel.getY() * Time.deltaTIME);

            updateAngleOnPlanet();
            updateParent();
            distToBottom = parent.getDistanceTo(getAbsPos().getX(), getAbsPos().getY());
            //detect collisions
            if(distToBottom < 0)
                attemptLanding();
        }

        for (List<SpaceshipComponent> stage : stages) {
            for (SpaceshipComponent comp : stage) {
                if ((comp instanceof Engine) && ((Engine) comp).getThrust() != 0) {
                    ((Engine) comp).burnFuel(((Engine) comp).getFuelConsumption()*throttle/100*Time.deltaTIME);
                }
            }
        }

        setPrintScale();

        List<Debris> toRemove = new ArrayList<>();
        for (Debris debris : shipDebris) {
            debris.update();
            if (debris.getNumIterations() > 1000) toRemove.add(debris);
        }
        shipDebris.removeAll(toRemove);

        recalculateOrigin(400,400);
    }


    void attemptLiftOff(){
        System.out.println("LIFT OFF");

        if(!enginesPresent() || !fuelPresent())
            return;
        Point p = parent.getPlanetSurfaceVelocity(this);

        vel = new Point(p.getX(), p.getY());
        System.out.println("nice: " + p.getX() + " " + p.getY());

        System.out.println(getHorizontalOrbitalSpeed());
        landed = false;
    }

    void attemptLanding(){
        // TODO: ???????????????????????
        Point v =  parent.getPlanetVelocity();
        //????????????????????????????
/*
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
        pos = parent.getShipPosFromAngle(angleOnPlanet, distToBottom);

        landed = true;
*/
        //TODO: reset only radial velocity and calculate prograde on friciton force
        // if (stable time > cos) ---> LANDED
    }

    private boolean enginesPresent(){
        return true;
    }

    private boolean fuelPresent(){
        return true;
    }


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
    private Circle BIGyellowCircle = null;
    public Point getCenterOfMass(){
        return convertCoordinates(getPixelCenterOfMass());
    }

    public Point getPixelCenterOfMass () {
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
        return new Point(xs/d, ys/d);
    }

    public void setPrintScale() {
        drawable.setScaleX(Scale.SCALE/50);
        drawable.setScaleY(Scale.SCALE/50);
    }

    public void recalculateOrigin (double x, double y) {
        origin = new Point(x, y);

        Point center = getPixelCenterOfMass();
        if (yellowCircle == null) {
            yellowCircle = new Circle();
            yellowCircle.setFill(Color.rgb(255, 255, 0, 1));
            yellowCircle.setRadius(7);
            drawable.getChildren().add(yellowCircle);
        }
        yellowCircle.setCenterX(center.getX());
        yellowCircle.setCenterY(center.getY());

        if (BIGyellowCircle == null) {
            BIGyellowCircle = new Circle();
            BIGyellowCircle.setFill(Color.rgb(255, 255, 0, 0.2));
            //TODO: take the farthest point
            double maxDist = 0;
            Point convCenter = convertCoordinates(center);
            for (Point p : getVertices()) {
                maxDist = Math.max(maxDist, p.distFrom(convCenter));
            }
            BIGyellowCircle.setRadius(maxDist*50 + 700);
            drawable.getChildren().add(0, BIGyellowCircle);
        }
        BIGyellowCircle.setCenterX(center.getX());
        BIGyellowCircle.setCenterY(center.getY());

        double dx = (x - drawable.getBoundsInLocal().getCenterX());
        double dy = (y - drawable.getBoundsInLocal().getCenterY());

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

        if (d == 0) {
            drawable.getChildren().remove(blueCircle);
            blueCircle = null;
            return new Point(0, 0);
        }
        blueCircle.setCenterX(xs/d);
        blueCircle.setCenterY(ys/d);

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

    public void setForceMomentum(double m){
        forceMomentum = m;
    }
    public void setTurnMomentum(double angle){
        turnMomentum = angle;
    }
    private void updateTurn(){
        double totalMomentum = turnMomentum + forceMomentum;
        double angleAcceleration = Math.toDegrees(totalMomentum / getMomentOfInertia());
        double turnDelta = angleAcceleration*Time.deltaTIME;
        turnSpeed +=turnDelta;
        //System.out.println(turnDelta);
        rotate.setAngle(rotate.getAngle() + turnSpeed);
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

            vel = new Point(vel.getX() + velocity.getX(), vel.getY() + velocity.getY());

            if(parent.parent == newParent ){ // earth.parent-> sun  == sun?
                pos = new Point(pos.getX() + parent.getRelPos().getX(),
                pos.getY() +  parent.getRelPos().getY());
            }
            else{
                pos = new Point(pos.getX() - parent.getRelPos().getX(),
                        pos.getY() -  parent.getRelPos().getY());
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

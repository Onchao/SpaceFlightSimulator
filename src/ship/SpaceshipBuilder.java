package ship;

import ship.components.*;
import utility.Direction;
import utility.Graph;
import utility.Mount;
import world.CelestialBody;
import world.SolarSystem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SpaceshipBuilder {

    private List<SpaceshipComponent> components;
    private Graph<SpaceshipComponent> componentGraph = null;

    public SpaceshipBuilder() {
        components = new ArrayList<>();
    }

    public List<Mount> getMountPoints (SpaceshipComponentFactory fact) {
        ArrayList<Mount> ret = new ArrayList<>();

        Class cls = fact.getComponentClass();

        if (cls == RadialDecouplerComponent.class || cls == LandingStrutsComponent.class) {
            for (SpaceshipComponent c : components) {
                if (c.getRightMount() != null && !c.getRightMount().isUsed()) ret.add(c.getRightMount());
                if (c.getLeftMount() != null && !c.getLeftMount().isUsed()) ret.add(c.getLeftMount());
            }
        } else {
            if (cls == FuelTankComponent.class) {
                for (SpaceshipComponent c : components) {
                    if (c.getRightMount() != null && !c.getRightMount().isUsed()) ret.add(c.getRightMount());
                    if (c.getLeftMount() != null && !c.getLeftMount().isUsed()) ret.add(c.getLeftMount());
                }
            }

            if (components.isEmpty()) {
                ret.add(new Mount(150, 720, 50, 200, Mount.Direction.UPPER, null));
            } else {
                for (SpaceshipComponent c : components) {
                    if (c.getUpperMount() != null && !c.getUpperMount().isUsed()) ret.add(c.getUpperMount());
                    if (c.getLowerMount() != null && !c.getLowerMount().isUsed()) ret.add(c.getLowerMount());
                }
            }
        }

        return ret;
    }

    public void addComponent (SpaceshipComponent c) {
        components.add(c);
    }

    public List<SpaceshipComponent> getComponents() {
        return Collections.unmodifiableList(components);
    }

    public void removeComponent (SpaceshipComponent c) {
        components.remove(c);
    }

    private void makeComponentGraph () {
        componentGraph = new Graph<>();
        for (SpaceshipComponent comp : components) {
            componentGraph.addVertex(comp);
            if (comp.getLeftMount() != null && comp.getLeftMount().isUsed()) {
                componentGraph.addEdge(comp, comp.getLeftMount().getAttached());
            }
            if (comp.getRightMount() != null && comp.getRightMount().isUsed()) {
                componentGraph.addEdge(comp, comp.getRightMount().getAttached());
            }
            if (comp.getUpperMount() != null && comp.getUpperMount().isUsed()) {
                componentGraph.addEdge(comp, comp.getUpperMount().getAttached());
            }
            if (comp.getLowerMount() != null && comp.getLowerMount().isUsed()) {
                componentGraph.addEdge(comp, comp.getLowerMount().getAttached());
            }
        }
    }

    private boolean isConnected () {
        return componentGraph.getCC().size() == 1;
    }

    private List<List<SpaceshipComponent>> divideIntoStages () {
        Graph<SpaceshipComponent> stagesGraph = new Graph<>();
        for (SpaceshipComponent comp : components) {
            if ((comp instanceof CircularDecouplerComponent) || (comp instanceof RadialDecouplerComponent))
                continue;
            stagesGraph.addVertex(comp);
            if (comp.getLeftMount() != null
                    && comp.getLeftMount().isUsed()
                    && !(comp.getLeftMount().getAttached() instanceof RadialDecouplerComponent)) {
                stagesGraph.addEdge(comp, comp.getLeftMount().getAttached());
            }
            if (comp.getRightMount() != null
                    && comp.getRightMount().isUsed()
                    && !(comp.getRightMount().getAttached() instanceof RadialDecouplerComponent)) {
                stagesGraph.addEdge(comp, comp.getRightMount().getAttached());
            }
            if (comp.getUpperMount() != null
                    && comp.getUpperMount().isUsed()
                    && !(comp.getUpperMount().getAttached() instanceof CircularDecouplerComponent)) {
                stagesGraph.addEdge(comp, comp.getUpperMount().getAttached());
            }
            if (comp.getLowerMount() != null
                    && comp.getLowerMount().isUsed()
                    && !(comp.getLowerMount().getAttached() instanceof CircularDecouplerComponent)) {
                stagesGraph.addEdge(comp, comp.getLowerMount().getAttached());
            }
        }

        List<List<SpaceshipComponent>> ret = stagesGraph.getCC();

        for (int id = 0; id < ret.size(); ++ id) {
            for (SpaceshipComponent comp : ret.get(id)) comp.setStageNumber(id);
        }

        for (SpaceshipComponent comp : components) {
            if (comp instanceof CircularDecouplerComponent) {
                if (comp.getLowerMount().isUsed()) {
                    ret.get(comp.getLowerMount().getAttached().getStageNumber()).add(comp);
                    comp.setStageNumber(comp.getLowerMount().getAttached().getStageNumber());
                } else {
                    ret.add(new ArrayList<>());
                    ret.get(ret.size()-1).add(comp);
                    comp.setStageNumber(ret.size()-1);
                }
            }
            if (comp instanceof RadialDecouplerComponent) {
                if (((RadialDecouplerComponent) comp).getDirection() == Direction.RIGHT && comp.getRightMount().isUsed()) {
                    ret.get(comp.getRightMount().getAttached().getStageNumber()).add(comp);
                    comp.setStageNumber(comp.getRightMount().getAttached().getStageNumber());
                } else if (((RadialDecouplerComponent) comp).getDirection() == Direction.LEFT && comp.getLeftMount().isUsed()) {
                    ret.get(comp.getLeftMount().getAttached().getStageNumber()).add(comp);
                    comp.setStageNumber(comp.getLeftMount().getAttached().getStageNumber());
                } else {
                    ret.add(new ArrayList<>());
                    ret.get(ret.size()-1).add(comp);
                    comp.setStageNumber(ret.size()-1);
                }
            }
        }

        return ret;
    }

    public Spaceship spaceship;
    public Spaceship build(CelestialBody parent, double angleOnPlanet,SolarSystem solarSystem) {
        makeComponentGraph();
        if (!isConnected()) {
            System.out.println("Opsss...");
            return null;
        }
        spaceship =  new Spaceship(divideIntoStages(), parent, angleOnPlanet, solarSystem);
        return spaceship;
    }
    public Spaceship getSpaceship(){
        return spaceship;
    }

}

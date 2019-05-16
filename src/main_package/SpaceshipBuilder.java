package main_package;

import java.util.ArrayList;
import java.util.List;

public class SpaceshipBuilder {

    List<SpaceshipComponent> components;
    List<List<SpaceshipComponent>> stages;

    public SpaceshipBuilder() {
        components = new ArrayList<>();
        stages = new ArrayList<>();
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

    public void addComponent (SpaceshipComponent c, Mount mount) {
        if (mount.getParent() == null) {
            c.setStageNumber(0);
        } else if (!(mount.getParent() instanceof RadialDecouplerComponent) && !(mount.getParent() instanceof CircularDecouplerComponent)) {
            c.setStageNumber(mount.getParent().getStageNumber());
        } else {
            c.setStageNumber(mount.getParent().getStageNumber() + 1);
        }

        if (c.getStageNumber() >= stages.size()) {
            stages.add(new ArrayList<>());
        }
        components.add(c);
        stages.get(c.getStageNumber()).add(c);
    }

    public Spaceship build() {
        //TODO: sanity checks
        return new Spaceship(stages);
    }
}

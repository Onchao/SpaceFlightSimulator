package main_package;

import java.util.ArrayList;
import java.util.List;

public class SpaceshipBuilder {

    List<SpaceshipComponent> components;

    public SpaceshipBuilder() {
        components = new ArrayList<>();
    }

    public List<Mount> getMountPoints (SpaceshipComponentFactory fact) {
        ArrayList<Mount> ret = new ArrayList<>();

        Class cls = fact.getComponentClass();

        if (cls == RadialDecouplerComponent.class || cls == LandingStrutsComponent.class) {
            //TODO
        } else {
            if (cls == FuelTankComponent.class) {
                //TODO
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
}

package ship.components;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import utility.Mount;

import java.util.*;

public class PowerfulAtmosphericEngineComponent extends SpaceshipComponent implements Engine, ActiveComponent {
    private ImageView img;

    private double maxThrust = 20000.0*1000; // N;

    private final double fuelConsumption = 1600;  // kg/s
    private boolean isActive;
    private int activationNumber = 0;

    private SortedMap<Integer, List<FuelTankComponent>> availableTanks;

    private void detectTanks() {
        availableTanks = new TreeMap<>(Collections.reverseOrder());
        class QueueElem {
            public SpaceshipComponent component;
            public int height;
            public QueueElem (SpaceshipComponent component, int height) {
                this.component = component;
                this.height = height;
            }
        }
        Set<SpaceshipComponent> visited = new HashSet<>();
        List<QueueElem> queue = new ArrayList<>();
        queue.add(new QueueElem(this, 0));

        while (!queue.isEmpty()) {
            QueueElem e = queue.get(0);
            queue.remove(0);
            if ((e.component instanceof CircularDecouplerComponent) || e.component instanceof RadialDecouplerComponent)
                continue;

            if (e.component instanceof FuelTankComponent) {
                if (!availableTanks.containsKey(e.height)) availableTanks.put(e.height, new ArrayList<>());
                availableTanks.get(e.height).add((FuelTankComponent) e.component);
            }

            if (e.component.getLeftMount() != null && e.component.getLeftMount().isUsed()
                    && !visited.contains(e.component.getLeftMount().getAttached())) {
                queue.add(new QueueElem(e.component.getLeftMount().getAttached(), e.height));
                visited.add(e.component.getLeftMount().getAttached());
            }
            if (e.component.getRightMount() != null && e.component.getRightMount().isUsed()
                    && !visited.contains(e.component.getRightMount().getAttached())) {
                queue.add(new QueueElem(e.component.getRightMount().getAttached(), e.height));
                visited.add(e.component.getRightMount().getAttached());
            }
            if (e.component.getUpperMount() != null && e.component.getUpperMount().isUsed()
                    && !visited.contains(e.component.getUpperMount().getAttached())) {
                queue.add(new QueueElem(e.component.getUpperMount().getAttached(), e.height + 1));
                visited.add(e.component.getUpperMount().getAttached());
            }
        }
    }

    public PowerfulAtmosphericEngineComponent() {
        img = new ImageView(new Image(getClass().getResourceAsStream("/images/atmEngine.png")));

        super.upperMount = new Mount(40, -20, 50, 200, Mount.Direction.UPPER, this);
        super.lowerMount = new Mount(40, 250, 50, 200, Mount.Direction.LOWER, this);
    }

    public static String getName() {
        return "Powerful Atmospheric Engine";
    }

    @Override
    public int getHeight() {
        return 200;
    }

    @Override
    public int getWidth() {
        return 200;
    }

    @Override
    public int getMass() {
        return 70*1000;
    }

    @Override
    public ImageView getImage() {
        return img;
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public double getThrust() {
        if (!isActive) return 0.0;
        return maxThrust;
    }

    @Override
    public double maxThrust() {
        return maxThrust;
    }

    @Override
    public void setThrust(double val) {
        maxThrust = val;
    }

    @Override
    public double getDragCoefficient() {
        return 1.0;
    }

    @Override
    public double getFrontAvgSurface(){
        return 2*Math.PI*1.2*(1.2+4)/4;
    }

    @Override
    public double getFuelConsumption() {
        return fuelConsumption;
    }

    @Override
    public double burnFuel(double amount) {
        double leftToBurn = amount;

        for (Map.Entry<Integer, List<FuelTankComponent>> entry : availableTanks.entrySet()) {
            double fuelLeft = entry.getValue().get(0).getFuelState();
            if (fuelLeft * entry.getValue().size() > amount) {
                double sub = amount/entry.getValue().size();
                for (FuelTankComponent tank : entry.getValue()) tank.setFuelState(tank.getFuelState() - sub);
                leftToBurn = 0;
                break;
            } else {
                leftToBurn -= fuelLeft*entry.getValue().size();
                for (FuelTankComponent tank : entry.getValue()) tank.setFuelState(0);
            }
        }

        if (leftToBurn > amount/2)
            maxThrust = 0;

        return amount - leftToBurn;  // == 0 if we're out of fuel
    }

    @Override
    public ComponentAction activate() {
        isActive = true;
        detectTanks();
        return new ComponentAction(ComponentAction.ActionType.ACTIVATE_ENGINE, stageNumber);
    }

    @Override
    public boolean isActive() {
        return isActive;
    }

    @Override
    public void setActivationNumber(int num) {
        activationNumber = num;
    }

    @Override
    public int getActivationNumber() {
        return activationNumber;
    }
}
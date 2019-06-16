package ship.components;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import utility.Mount;

import java.util.*;

public class HighEfficiencyVaccumEngineComponent extends SpaceshipComponent implements Engine, ActiveComponent {
    private ImageView img;

    private final double maxThrust = 2000.0*1000;
    private double curThrust = 0.0;
    private final double maxFuelConsumption = 200.0;
    private boolean isActive;
    private int activationNumber = 0;

    private SortedMap<Integer, List <FuelTankComponent>> availableTanks;

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

    public HighEfficiencyVaccumEngineComponent() {
        img = new ImageView(new Image(getClass().getResourceAsStream("/images/vacEngine.png")));

        super.upperMount = new Mount(40, -20, 50, 200, Mount.Direction.UPPER, this);
        super.lowerMount = new Mount(40, 130, 50, 200, Mount.Direction.LOWER, this);
    }

    public static String getName() {
        return "Vacuum Engine";
    }

    @Override
    public int getHeight() {
        return 80;
    }

    @Override
    public int getWidth() {
        return 200;
    }

    @Override
    public int getMass() {
        return 20*1000;
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
    public double getThrust(double density) {
        return curThrust*(1-(density/2));
    }

    @Override
    public double maxThrust() {
        return maxThrust;
    }

    @Override
    public void setThrust(double val) {
        if (val > maxThrust) return;
        curThrust = val;
    }

    @Override
    public double getFuelConsumption() {
        return maxFuelConsumption;
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

        if (leftToBurn > amount/2) curThrust = 0;

        return amount - leftToBurn;  // == 0 if we're out of fuel
    }

    @Override
    public double getDragCoefficient() {
        return 1.0;
    }

    @Override
    public double getFrontAvgSurface(){
        return Math.PI*1.2*2;
    }

    @Override
    public ComponentAction activate() {
        isActive = true;
        detectTanks();
        setThrust(maxThrust);
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

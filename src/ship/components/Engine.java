package ship.components;

public interface Engine {
    double getThrust(double density);
    double maxThrust();
    void setThrust(double val);
    double getFuelConsumption();
    double burnFuel(double amount);
}

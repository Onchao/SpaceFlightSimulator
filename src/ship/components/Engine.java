package ship.components;

public interface Engine {
    double getThrust();
    double maxThrust();
    void setThrust(double val);
    double getFuelConsumption();
    double burnFuel(double amount);
}

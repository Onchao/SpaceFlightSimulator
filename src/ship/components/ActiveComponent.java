package ship.components;

public interface ActiveComponent {
    ComponentAction activate();
    boolean isActive();
    void setActivationNumber(int num);
    int getActivationNumber();
}

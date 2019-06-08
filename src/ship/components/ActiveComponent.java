package ship.components;

public interface ActiveComponent {
    ComponentAction activate ();
    boolean isActive();
}

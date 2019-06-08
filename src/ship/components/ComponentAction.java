package ship.components;

public class ComponentAction {
    public ActionType getType() {
        return type;
    }

    public int getStageNumber() {
        return stageNumber;
    }

    public enum ActionType {
        ACTIVATE_ENGINE, DETACH_STAGE, OPEN_PARACHUTE, NO_ACTION
    }

    private final ActionType type;
    private final int stageNumber;

    public ComponentAction (ActionType type, int stageNo) {
        this.type = type;
        this.stageNumber = stageNo;
    }
}

package fr.cnamts.cpam33.ordonnance.infrastructure.actuators.watchers;

public enum WatcherStatus {

    STARTED("started"),
    RUNNING("running"),
    STOPPED("stopped"),
    UNKNOWN("unknown");

    private final String state;

    WatcherStatus(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }

}

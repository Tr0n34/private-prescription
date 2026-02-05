package fr.cnamts.cpam33.ordonnance.infrastructure.actuators.watchers;

public enum WatcherAction {

    START("start"),
    STOP("stop"),
    UNKNOWN("unknown");

    private final String value;

    WatcherAction(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static WatcherAction fromString(String action) {
        WatcherAction result = UNKNOWN;
        for ( WatcherAction wa : values() ) {
            if ( wa.value.equalsIgnoreCase(action) ) {
                result = wa;
                break;
            }
        }
        return result;
    }

}

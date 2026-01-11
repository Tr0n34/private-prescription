package fr.cnamts.cpam33.ordonnance.infrastructure.in.adapters.controllers.routes;

public final class WatcherCommand {

    public static final String START = "/start";
    public static final String STOP = "/stop";
    public static final String STATUS=  "/status";

    private WatcherCommand() {
        throw new UnsupportedOperationException();
    }

}

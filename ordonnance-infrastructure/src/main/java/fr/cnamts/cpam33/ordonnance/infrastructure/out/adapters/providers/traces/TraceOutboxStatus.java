package fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.providers.traces;

public enum TraceOutboxStatus {

    PENDING,
    RETRYING,
    SENT,
    DEAD

}
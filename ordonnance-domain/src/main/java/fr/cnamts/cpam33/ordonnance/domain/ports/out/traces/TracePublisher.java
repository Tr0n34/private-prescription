package fr.cnamts.cpam33.ordonnance.domain.ports.out.traces;

import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.tracabilite.Trace;

public interface TracePublisher {

    void publish(Trace trace);

}

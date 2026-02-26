package fr.cnamts.cpam33.ordonnance.infrastructure.akernel.traces;

import fr.cnamts.cpam33.ordonnance.infrastructure.out.dto.traces.TracePublicationContext;

public interface TracePublicationContextProvider {

    TracePublicationContext current();

}

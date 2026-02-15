package fr.cnamts.cpam33.ordonnance.domain.ports.out.traces;

import fr.cnamts.cpam33.ordonnance.domain.models.tracabilite.TraceId;

import java.time.LocalDate;
import java.util.List;

public interface ArchiverTracePort {

    void archiver(TraceId traceId);

    void archiver(List<TraceId> traceIds);

    void archiver(LocalDate before);

}

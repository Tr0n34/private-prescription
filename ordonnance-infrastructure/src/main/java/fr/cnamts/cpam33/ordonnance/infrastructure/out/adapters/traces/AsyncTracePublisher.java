package fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.traces;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.tracabilite.Trace;
import fr.cnamts.cpam33.ordonnance.domain.ports.out.traces.TracePublisher;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.repositories.traces.ActeMetierJpaRepository;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.repositories.traces.TraceJpaRepository;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.mappers.traces.TraceEntityMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

public class AsyncTracePublisher implements TracePublisher {

    private static final Logger logger = LoggerFactory.getLogger(AsyncTracePublisher.class);

    private final TraceJpaRepository traceJpaRepository;
    private final TraceEntityMapper traceEntityMapper;
    private final ActeMetierJpaRepository acteMetierJpaRepository;
    @Qualifier("traceObjectMapper")
    private final ObjectMapper traceObjectMapper;

    public AsyncTracePublisher(TraceJpaRepository traceJpaRepository,
                               TraceEntityMapper traceEntityMapper,
                               ActeMetierJpaRepository acteMetierJpaRepository,
                               ObjectMapper traceObjectMapper) {
        this.traceJpaRepository = traceJpaRepository;
        this.traceEntityMapper = traceEntityMapper;
        this.acteMetierJpaRepository = acteMetierJpaRepository;
        this.traceObjectMapper = traceObjectMapper;
    }

    @Override
    public void publish(Trace trace) {

    }

}

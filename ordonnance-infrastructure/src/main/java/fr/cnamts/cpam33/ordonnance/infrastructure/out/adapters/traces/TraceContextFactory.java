package fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.traces;

import fr.cnamts.cpam33.ordonnance.domain.kernel.traces.Traceable;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.tracabilite.TraceContext;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.tracabilite.TraceIn;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.tracabilite.TraceOut;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class TraceContextFactory {

    private static final Logger logger = LoggerFactory.getLogger(TraceContextFactory.class.getName());

    private final TraceInBuilder traceInBuilder;
    private final TraceOutBuilder traceOutBuilder;

    public TraceContextFactory(TraceInBuilder traceInBuilder,
                               TraceOutBuilder traceOutBuilder) {
        this.traceInBuilder = traceInBuilder;
        this.traceOutBuilder = traceOutBuilder;
    }

    public TraceContext build(ProceedingJoinPoint pjp,
                              Traceable command,
                              Object result,
                              Throwable error) {
        TraceIn in = traceInBuilder.build(pjp, command);
        TraceOut out = traceOutBuilder.build(result, error);
        logger.trace("TraceContextFactory.build(): in={}, out={}", in, out);
        return new TraceContext(in, out);
    }

}

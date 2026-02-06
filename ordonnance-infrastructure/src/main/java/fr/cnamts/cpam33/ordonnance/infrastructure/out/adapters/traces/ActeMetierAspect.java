package fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.traces;

import fr.cnamts.cpam33.ordonnance.domain.models.events.ActeMetierEvent;
import fr.cnamts.cpam33.ordonnance.domain.models.events.TraceCommand;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.tracabilite.Trace;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.tracabilite.TraceAttribute;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.tracabilite.TraceContext;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.tracabilite.TraceValue;
import fr.cnamts.cpam33.ordonnance.domain.ports.out.traces.TracePublisher;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Aspect
@Component
public class ActeMetierAspect {

    private static final Logger logger = LoggerFactory.getLogger(ActeMetierAspect.class);

    private final TracePublisher publisher;
    private final Clock  clock;

    public ActeMetierAspect(TracePublisher publisher, Clock clock) {
        this.publisher = publisher;
        this.clock = clock;
    }

    @Around("@within(acteMetier)")
    public Object around(ProceedingJoinPoint pjp, ActeMetierEvent acteMetier) throws Throwable {
        logger.trace("Entering around(ProceedingJoinPoint,ActeMetierEvent)");
        Object result = pjp.proceed();
        Object[] args = pjp.getArgs();
        if ( args.length > 0 && args[0] instanceof TraceCommand traceCommand ) {
            Trace trace = Trace.of(
                    acteMetier.value(),
                    traceCommand.utilisateurId(),
                    buildTraceContext(args, result),
                    LocalDateTime.now(clock),
                    clock
            );
            publisher.publish(trace);
        }
        return result;
    }

    private TraceContext buildTraceContext(Object[] args, Object result) {
        List<TraceAttribute> attributes = new ArrayList<>();
        for ( int i = 0; i < args.length; i++ ) {
            Object arg = args[i];
            if ( arg != null ) {
                attributes.add(new TraceAttribute("arg" + i, new TraceValue(arg)));
            }
        }
        if ( result != null ) {
            attributes.add(new TraceAttribute("result", new TraceValue(result)));
        }
        logger.trace("TraceContext build complete");
        return new TraceContext(attributes);
    }

}

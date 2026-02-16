package fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.traces;

import fr.cnamts.cpam33.ordonnance.domain.kernel.events.ActeMetierEvent;
import fr.cnamts.cpam33.ordonnance.domain.kernel.events.TraceCommand;
import fr.cnamts.cpam33.ordonnance.domain.models.tracabilite.Trace;
import fr.cnamts.cpam33.ordonnance.domain.models.tracabilite.TraceAttribute;
import fr.cnamts.cpam33.ordonnance.domain.models.tracabilite.TraceContext;
import fr.cnamts.cpam33.ordonnance.domain.models.tracabilite.TraceValue;
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
        Object result = null;
        Throwable error = null;
        try {
            result = pjp.proceed();
        } catch (Throwable t) {
            error = t;
        }
        publishTrace(pjp.getArgs(), result, error, acteMetier);
        if ( error != null ) {
            throw error;
        }
        return result;
    }

    private void publishTrace(Object[] arguments, Object result, Throwable error, ActeMetierEvent event) {
        boolean hasToBePublish = true;
        Trace trace = null;
        if ( arguments == null || arguments.length == 0) {
            hasToBePublish = false;
        } else if ( !(arguments[0] instanceof TraceCommand traceCommand )) {
            hasToBePublish = false;
        } else {
            trace = Trace.of(
                    event.value(),
                    traceCommand.utilisateurId(),
                    buildTraceContext(arguments, result, error),
                    LocalDateTime.now(clock),
                    clock
            );
        }
        logger.trace("Trace acteMetier : {}, {}", event, hasToBePublish);
        if ( hasToBePublish ) {
            try {
                publisher.publish(trace);
            } catch (Exception ex) {
                // volontairement ignoré : la traçabilité ne doit jamais casser le métier
                logger.warn("Unable to publish trace", ex);
            }
        }
    }

    private TraceContext buildTraceContext(Object[] arguments, Object result, Throwable error) {
        List<TraceAttribute> attributes = new ArrayList<>();
        for ( Object argument : arguments ) {
            if ( argument != null ) {
                attributes.add(new TraceAttribute(argument.getClass().getSimpleName(), new TraceValue(argument)));
            }
        }
        if ( result != null ) {
            attributes.add(new TraceAttribute("result", new TraceValue(result)));
        }
        if ( error != null ) {
            attributes.add(new TraceAttribute("status", new TraceValue("ERROR")));
            attributes.add(new TraceAttribute("errorType", new TraceValue(error.getClass().getName())));
            attributes.add(new TraceAttribute("errorMessage", new TraceValue(String.valueOf(error.getMessage()))));
        } else {
            attributes.add(new TraceAttribute("status", new TraceValue("SUCCESS")));
        }
        logger.trace("TraceContext build complete");
        return new TraceContext(attributes);
    }

}

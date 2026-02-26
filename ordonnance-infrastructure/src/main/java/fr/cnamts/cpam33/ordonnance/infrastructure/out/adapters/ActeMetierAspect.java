package fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters;

import fr.cnamts.cpam33.ordonnance.domain.kernel.events.ActeMetierEvent;
import fr.cnamts.cpam33.ordonnance.domain.kernel.traces.Traceable;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.tracabilite.*;
import fr.cnamts.cpam33.ordonnance.domain.ports.out.traces.ActeMetierRepository;
import fr.cnamts.cpam33.ordonnance.domain.ports.out.traces.TraceNumGenerator;
import fr.cnamts.cpam33.ordonnance.domain.ports.out.traces.TracePublisher;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.providers.traces.TraceContextFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.Instant;

@Aspect
@Component
public class ActeMetierAspect {

    private static final Logger logger = LoggerFactory.getLogger(ActeMetierAspect.class);

    private final ActeMetierRepository acteMetierRepository;
    private final TracePublisher publisher;
    private final Clock clock;
    private final TraceContextFactory traceContextFactory;
    private final TraceNumGenerator traceNumGenerator;

    public ActeMetierAspect(ActeMetierRepository acteMetierRepository,
                            TracePublisher publisher,
                            TraceContextFactory traceContextFactory,
                            TraceNumGenerator traceNumGenerator,
                            Clock clock) {
        this.acteMetierRepository = acteMetierRepository;
        this.publisher = publisher;
        this.clock = clock;
        this.traceContextFactory = traceContextFactory;
        this.traceNumGenerator = traceNumGenerator;
    }

    @Around("@within(acteMetier)")
    public Object around(ProceedingJoinPoint pjp, ActeMetierEvent acteMetier) throws Throwable {
        Object result = null;
        Throwable error = null;
        try {
            result = pjp.proceed();
            return result;
        } catch (Throwable t) {
            error = t;
            throw t;
        } finally {
            publishTrace(pjp, result, error, acteMetier);
        }
    }

    private void publishTrace(ProceedingJoinPoint pjp, Object result, Throwable error, ActeMetierEvent event) {
        Traceable trace = findTraceCommand(pjp.getArgs());
        boolean hasToBePublish = (trace != null);
        logger.trace("Trace acteMetier={}, hasToBePublish={}", event, hasToBePublish);
        FonctionId fonctionId = acteMetierRepository.findFonctionByActeMetierId(new ActeMetierId(event.value().name())).fonctionId();
        if ( hasToBePublish ) {
            TraceContext traceContext = traceContextFactory.build(pjp, trace, result, error);
            Trace publishedTrace = Trace.of(
                    new TraceId(traceNumGenerator.generate()),
                    null,
                    event.value(),
                    fonctionId,
                    trace.utilisateurId(),
                    result != null ? result.getClass().getSimpleName() : "UNKNOWN_ERROR",
                    null,
                    traceContext,
                    Instant.now(clock),
                    clock
            );
            logger.trace("OUT status={}, attrs={}", traceContext.out().status(), traceContext.out().traceAttributes().size());
            try {
                publisher.publish(publishedTrace);
            } catch (Exception ex) {
                logger.warn("Unable to publish trace", ex);
            }
        }
    }

    private Traceable findTraceCommand(Object[] arguments) {
        Traceable traceCommand = null;
        if ( arguments != null ) {
            for ( int i = 0; i < arguments.length && traceCommand == null; i++ ) {
                if ( arguments[i] instanceof Traceable foundTraceCommand ) {
                    traceCommand = foundTraceCommand;
                }
            }
        }
        return traceCommand;
    }

}

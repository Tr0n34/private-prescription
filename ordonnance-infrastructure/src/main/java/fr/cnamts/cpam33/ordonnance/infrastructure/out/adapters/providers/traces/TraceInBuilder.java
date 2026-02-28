package fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.providers.traces;

import fr.cnamts.cpam33.ordonnance.domain.kernel.traces.Traceable;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.tracabilite.TraceAttribute;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.tracabilite.TraceIn;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.tracabilite.TraceValue;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.RecordComponent;
import java.util.ArrayList;
import java.util.List;

@Component
public class TraceInBuilder {

    private static final Logger logger = LoggerFactory.getLogger(TraceInBuilder.class);

    private final TraceSanitizer sanitizer;

    public TraceInBuilder(TraceSanitizer sanitizer) {
        this.sanitizer = sanitizer;
    }

    public TraceIn build(ProceedingJoinPoint pjp, Traceable command) {
        return new TraceIn(
                pjp.getSignature().getName(),
                pjp.getSignature().toShortString(),
                buildInParams(command)
        );
    }

    private List<TraceAttribute> buildInParams(Traceable command) {
        List<TraceAttribute> inParameters = List.of();
        if ( command != null ) {
            inParameters = new ArrayList<>();
            inParameters.add(createAttribute("commandType", command.getClass().getSimpleName()));
            inParameters.add(createAttribute("utilisateurId", String.valueOf(command.utilisateurId())));
            Class<?> type = command.getClass();
            if ( type.isRecord() ) {
                for ( RecordComponent rc : type.getRecordComponents() ) {
                    TraceAttribute a = extractRecordParam(rc, command);
                    if ( a != null ) {
                        inParameters.add(a);
                    }
                }
            }
        }
        return List.copyOf(inParameters);
    }

    private TraceAttribute extractRecordParam(RecordComponent rc, Object recordInstance) {
        TraceAttribute traceAttribute = null;
        String name = rc.getName();
        if ( !"utilisateurId".equals(name) ) {
            Object raw = invokeAccessor(rc, recordInstance);
            if ( raw != null ) {
                Object safe = sanitizer.sanitize(raw);
                if ( safe != null ) {
                    traceAttribute = new TraceAttribute(name, new TraceValue(safe));
                }
            }
        }
        return traceAttribute;
    }

    private Object invokeAccessor(RecordComponent rc, Object target) {
        Object result = null;
        try {
            result = rc.getAccessor().invoke(target);
        } catch (Exception ex) {
            logger.warn(ex.getMessage(), ex);
        }
        return result;
    }

    private TraceAttribute createAttribute(String name, Object value) {
        return new TraceAttribute(name, new TraceValue(value == null ? "null" : value));
    }
}
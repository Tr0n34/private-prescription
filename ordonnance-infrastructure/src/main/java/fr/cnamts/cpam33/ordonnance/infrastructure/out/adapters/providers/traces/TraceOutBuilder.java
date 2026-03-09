package fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.providers.traces;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.cnamts.cpam33.ordonnance.domain.abstracts.exceptions.DomainException;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.tracabilite.TraceAttribute;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.tracabilite.TraceFailure;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.tracabilite.TraceOut;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.tracabilite.TraceValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TraceOutBuilder {

    private static final Logger logger = LoggerFactory.getLogger(TraceOutBuilder.class);

    private static final int MAX_RESULT_JSON_LENGTH = 4000;

    private final ObjectMapper traceObjectMapper;
    private final TraceSanitizer sanitizer;

    public TraceOutBuilder(@Qualifier("traceContextMapper") ObjectMapper traceObjectMapper,
                           TraceSanitizer sanitizer) {
        this.traceObjectMapper = traceObjectMapper;
        this.sanitizer = sanitizer;
    }

    public TraceOut build(Object result, Throwable error) {
        TraceOut out;
        if ( error != null ) {
            List<TraceAttribute> traceAttributes = buildFailureAttributes(error);
            out = TraceOut.failure(buildError(error), traceAttributes);
        } else {
            out = TraceOut.success(buildOutAttributes(result));
        }
        return out;
    }

    private List<TraceAttribute> buildFailureAttributes(Throwable error) {
        List<TraceAttribute> attrs = new ArrayList<>();
        attrs.add(createAttribute("exceptionType", error.getClass().getName()));
        attrs.add(createAttribute("exceptionMessage", sanitizer.truncate(String.valueOf(error.getMessage()), 512)));
        Throwable cause = error.getCause();
        if ( cause != null ) {
            attrs.add(createAttribute("causeType", cause.getClass().getName()));
            attrs.add(createAttribute("causeMessage", sanitizer.truncate(String.valueOf(cause.getMessage()), 512)));
        }
        return List.copyOf(attrs);
    }

    private List<TraceAttribute> buildOutAttributes(Object result) {
        List<TraceAttribute> outAttributes = List.of();
        if ( result != null ) {
            List<TraceAttribute> out = new ArrayList<>();
            out.add(createAttribute("resultType", result.getClass().getSimpleName()));
            TraceAttribute resultAttr = buildResultAttribute(result);
            if ( resultAttr != null ) {
                out.add(resultAttr);
            }
            TraceAttribute truncatedAttr = buildTruncatedAttribute(result);
            if ( truncatedAttr != null ) {
                out.add(truncatedAttr);
            }
            outAttributes = List.copyOf(out);
        }
        return outAttributes;
    }

    private TraceAttribute buildResultAttribute(Object result) {
        TraceAttribute attribute = null;
        if ( sanitizer.isSimpleValue(result) ) {
            attribute = createAttribute("result", String.valueOf(result));
        } else {
            Object tree = sanitizer.sanitize(result);
            String json = trySerialize(tree);
            if (json != null && json.length() <= MAX_RESULT_JSON_LENGTH) {
                attribute = new TraceAttribute("result", new TraceValue(tree));
            }
        }
        return attribute;
    }

    private TraceAttribute buildTruncatedAttribute(Object result) {
        TraceAttribute attribute = null;
        if ( !sanitizer.isSimpleValue(result) ) {
            Object tree = sanitizer.sanitize(result);
            String json = trySerialize(tree);
            if ( json != null && json.length() > MAX_RESULT_JSON_LENGTH ) {
                attribute = createAttribute("resultTruncated", true);
            }
        }
        return attribute;
    }

    private String trySerialize(Object tree) {
        String json = null;
        if ( tree != null ) {
            try {
                json = traceObjectMapper.writeValueAsString(tree);
            } catch (Exception e) {
                logger.warn(e.getMessage());
            }
        }
        return json;
    }

    private TraceAttribute createAttribute(String name, Object value) {
        return new TraceAttribute(name, new TraceValue(value == null ? "null" : value));
    }

    private TraceFailure buildError(Throwable error) {
        TraceFailure failure;
        if ( error instanceof DomainException de ) {
            failure = new TraceFailure(
                    de.getClass().getSimpleName(),
                    de.getMessage(),
                    String.valueOf(de.getCode()),
                    String.valueOf(de.getPlaceHolders())
            );
        } else {
            failure = new TraceFailure(
                    error.getClass().getName(),
                    String.valueOf(error.getMessage()),
                    null,
                    null
            );
        }
        return failure;
    }

}

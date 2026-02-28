package fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.providers.traces;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.cnamts.cpam33.ordonnance.infrastructure.technical.mappers.TraceValueNormalizer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.temporal.Temporal;
import java.util.UUID;

@Component
public class TraceSanitizer {

    private final ObjectMapper traceObjectMapper;

    public TraceSanitizer(@Qualifier("traceContextMapper") ObjectMapper traceObjectMapper) {
        this.traceObjectMapper = traceObjectMapper;
    }

    public Object sanitize(Object raw) {
        return raw != null
                ? TraceValueNormalizer.normalize(raw, traceObjectMapper)
                : null;
    }

    public boolean isSimpleValue(Object value) {
        boolean simple = false;
        if ( value == null ) {
            simple = true;
        } else {
            Class<?> type = value.getClass();
            simple = type.isPrimitive()
                    || Number.class.isAssignableFrom(type)
                    || CharSequence.class.isAssignableFrom(type)
                    || Boolean.class.isAssignableFrom(type)
                    || UUID.class.isAssignableFrom(type)
                    || type.isEnum()
                    || Temporal.class.isAssignableFrom(type)
                    || type.getSimpleName().endsWith("Id")
                    || (type.isRecord() && type.getRecordComponents().length == 1);
        }
        return simple;
    }

    public String truncate(String s, int max) {
        return s == null || s.length() <= max
                ? s
                : s.substring(0, max) + "...";
    }

}